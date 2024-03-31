package com.chatapp.vibeapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chatapp.vibeapp.data.ChatRoom
import com.chatapp.vibeapp.ui.theme.Purple40
import com.chatapp.vibeapp.viewmodels.ChatViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(viewModel: ChatViewModel,
               navController: NavHostController,
               onNavigateToContacts: () -> Unit) {

    Scaffold (
        topBar = { AppBarView(title = "VibeApp") },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 20.dp),
                    //.background(Purple40),
                contentColor = Color.White,
                containerColor = Purple40,
                //Color = Color.Purple,
                onClick = {
                    //navController.navigate(Screen.ContactsScreen.route)
                    onNavigateToContacts()
                }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            }
        }
    ) {
        ChatListScreen(viewModel = viewModel, navController = navController)
    }
}


@Composable
fun ChatRoomItem(chatRoom: ChatRoom, onChatRoomClick: (String) -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clickable { onChatRoomClick(chatRoom.chatRoomId) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )) {

        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Chat with: ${chatRoom.participantIds.joinToString()}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Last message: ${chatRoom.lastMessageContent}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ChatListScreen(viewModel: ChatViewModel,
                   //onChatRoomClick: (String) -> Unit,
                   navController: NavHostController
) {
    val chatRooms by viewModel.chatRooms.collectAsState(initial = emptyList())

    LazyColumn {
        items(chatRooms) { chatRoom ->
            ChatRoomItem(chatRoom = chatRoom, onChatRoomClick = { chatRoomId ->
                navController.navigate("chatRoom/$chatRoomId")
            })

        }
    }
}