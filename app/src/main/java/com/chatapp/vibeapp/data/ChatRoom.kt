package com.chatapp.vibeapp.data

data class ChatRoom(
    var chatRoomId: String = "", // Default to an empty string, Firestore fills it in
    val participantIds: List<String>, // User IDs of participants
    var lastMessageContent: String = "", // Store just the content of the last message
    var lastMessageTime: Long = 0 // Store the timestamp of the last message
)
