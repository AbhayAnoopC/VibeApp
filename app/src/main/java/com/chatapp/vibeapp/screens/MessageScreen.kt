package com.chatapp.vibeapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.chatapp.vibeapp.data.Message
import com.chatapp.vibeapp.viewmodels.ChatViewModel
//import com.google.type.Date
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
//import java.util.Locale

@Composable
fun MessageItem(message: Message) {
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = message.content, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(message.timestamp)), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun ChatRoomScreen(chatRoomId: String, viewModel: ChatViewModel) {
    val messages by viewModel.getMessagesForChatRoom(chatRoomId).collectAsState(initial = emptyList())
    var messageText by remember { mutableStateOf("") }

    Column {
        LazyColumn(modifier = Modifier.padding(2.dp)) {
            items(messages) { message ->
                MessageItem(message = message)
            }
        }
        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") }
            )
            Button(onClick = {
                viewModel.sendMessage(chatRoomId, messageText)
                messageText = "" // Clear input after sending
            }) {
                Text("Send")
            }
        }
    }
}