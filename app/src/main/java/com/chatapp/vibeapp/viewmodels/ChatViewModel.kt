package com.chatapp.vibeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.chatapp.vibeapp.data.ChatRepository
import com.chatapp.vibeapp.data.ChatRoom
import com.chatapp.vibeapp.data.Message
import com.google.firebase.auth.FirebaseAuth

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRooms: StateFlow<List<ChatRoom>> = _chatRooms

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    fun getMessagesForChatRoom(chatRoomId: String): StateFlow<List<Message>> {
        viewModelScope.launch {
            chatRepository.listenForMessages(chatRoomId) { newMessage ->
                _messages.value = _messages.value + newMessage
            }
        }
        return _messages
    }

    fun fetchChatRooms() {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val rooms = chatRepository.fetchChatRooms(userId)
                    _chatRooms.value = rooms
                } else {
                    // Handle the case where there is no logged-in user
                }
            } catch (e: Exception) {
                // Handle or log error
            }
        }
    }
    //}


//    fun sendMessage(chatRoomId: String, content: String) {
//        viewModelScope.launch {
//            // Assuming you have the current user's ID available
//            //val currentUserId = "currentUserSenderId" // Replace with actual user ID retrieval logic
//            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
//
//            chatRepository.sendMessage(chatRoomId, currentUserId, content)
//
//            // Optionally, update the chat room's last message after sending
//            chatRepository.updateChatRoomAfterMessage(chatRoomId, Message(content = content, timestamp = System.currentTimeMillis(), senderId = currentUserId))
//        }
//    }

    fun sendMessage(chatRoomId: String, content: String) {
        viewModelScope.launch {
            FirebaseAuth.getInstance().currentUser?.uid?.let { currentUserId ->
                chatRepository.sendMessage(chatRoomId, currentUserId, content)

                // Create a Message object. Adjust this part according to your actual constructor.
                val message = Message(
                    messageId = "", // Assuming Firestore generates this ID.
                    content = content,
                    senderId = currentUserId,
                    timestamp = System.currentTimeMillis(),
                    read = false // Default value, adjust if needed.
                )

                // Optionally, update the chat room's last message after sending
                chatRepository.updateChatRoomAfterMessage(chatRoomId, message)
            } ?: run {
                // Handle the case where there is no logged-in user.
                // This might involve showing an error message or redirecting to a login screen.
            }
        }
    }

}
