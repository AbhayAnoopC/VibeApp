package com.chatapp.vibeapp.data

data class Message(
    var messageId: String = "",
    val content: String,
    val senderId: String,
    val receiverId: String? = null, // Null if it's a group chat
    val timestamp: Long = System.currentTimeMillis(), // Default to current time,
    val read: Boolean = false
)

