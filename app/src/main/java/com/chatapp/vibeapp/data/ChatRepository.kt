package com.chatapp.vibeapp.data

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ChatRepository (private val firestore: FirebaseFirestore){

    suspend fun createChatRoom(participantIds: List<String>): String {
        val chatRoom = hashMapOf(
            "participantIds" to participantIds,
//            "lastMessage" to "",
//            "lastMessageTime" to System.currentTimeMillis()
        )

        val chatRoomRef = firestore.collection("chatRooms").add(chatRoom).await()
        val chatRoomId = chatRoomRef.id

        //maybe dont need to update the chatroom idback to firestore. //decide later
        firestore.collection("chatRooms").document(chatRoomId).update("chatRoomId", chatRoomId)

        return chatRoomId
    }

    suspend fun sendMessage(chatRoomId: String, senderId: String, content: String) {
        val message = hashMapOf(
            "senderId" to senderId,
            "content" to content,
            "timestamp" to System.currentTimeMillis()
        )

        val updates = mapOf(
            "lastMessage" to content,
            "lastMessageTime" to System.currentTimeMillis())


        firestore.collection("chatRooms").document(chatRoomId)
            .collection("messages").add(message).await()

        //this stuff is redundunt. use updateChatRoomAfterMessage call instead
//        firestore.collection("chatRooms").document(chatRoomId)
//            .update(updates).await()
            //.update("lastMessage" to content, "lastMessageTime" to System.currentTimeMillis())

        // Call updateChatRoomAfterMessage to update the chat room's last message details
        updateChatRoomAfterMessage(chatRoomId, Message(content = content, timestamp = System.currentTimeMillis(), senderId = senderId))


    }

    suspend fun updateChatRoomAfterMessage(chatRoomId: String, message: Message) {
        val updates = mapOf(
            "lastMessageContent" to message.content,
            "lastMessageTime" to message.timestamp
        )

        firestore.collection("chatRooms").document(chatRoomId)
            .update(updates).await()
    }

    fun listenForMessages(chatRoomId: String, onNewMessage: (Message) -> Unit) {
        firestore.collection("chatRooms").document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                snapshot?.documentChanges?.forEach { change ->
                    if (change.type == DocumentChange.Type.ADDED) {
                        val message = change.document.toObject(Message::class.java)
                        onNewMessage(message)
                    }
                }
            }
    }

//    suspend fun fetchChatRooms(userId: String): List<ChatRoom> {
//        return try {
//            // Query chat rooms where the current user is a participant
//            val querySnapshot = firestore.collection("chatRooms")
//                .whereArrayContains("participantIds", userId)
//                .get()
//                .await()
//
//            // Map the documents to ChatRoom objects
//            querySnapshot.documents.mapNotNull { document ->
//                document.toObject(ChatRoom::class.java)?.apply {
//                    chatRoomId = document.id // Ensure the chatRoomId is set from the document ID
//                }
//            }
//        } catch (e: Exception) {
//            // Handle or log the error
//            emptyList()
//        }
//    }
    suspend fun fetchChatRooms(userId: String): List<ChatRoom> {
        return try {
            val querySnapshot = firestore.collection("chatRooms")
                .whereArrayContains("participantIds", userId)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { document ->
                val chatRoom = document.toObject(ChatRoom::class.java)
                chatRoom?.chatRoomId = document.id // Only works if chatRoomId is var
                chatRoom // This line returns the chatRoom object from the mapNotNull lambda
            }
        } catch (e: Exception) {
            emptyList()
        }
    }




        suspend fun getOrCreateChatSessionId(currentUserUid: String, contactPhoneNumber: String): String {
            // First, find the user ID corresponding to the contact's phone number
            val usersCollection = firestore.collection("users")
            val contactQuerySnapshot = usersCollection
                .whereEqualTo("phoneNumber", contactPhoneNumber)
                .limit(1)
                .get()
                .await()

            val contactUserDocument = contactQuerySnapshot.documents.firstOrNull()
            val contactUserId = contactUserDocument?.id ?: return ""

            // Use currentUserUid and contactUserId to check for an existing chat session
            val chatRoomsCollection = firestore.collection("chatRooms")
            val chatRoomQuerySnapshot = chatRoomsCollection
                .whereArrayContains("participantIds", currentUserUid)
                .get()
                .await()

            val existingChatRoom = chatRoomQuerySnapshot.documents.firstOrNull { document ->
                val chatRoom = document.toObject(ChatRoom::class.java)
                chatRoom?.participantIds?.contains(contactUserId) == true
            }

            existingChatRoom?.let { return it.id }

            // If no existing session, create a new one
            val newChatRoom = ChatRoom(
                participantIds = listOf(currentUserUid, contactUserId),
                lastMessageContent = "",
                lastMessageTime = System.currentTimeMillis()
            )
            val newChatRoomRef = chatRoomsCollection.add(newChatRoom).await()

            return newChatRoomRef.id
        }




}