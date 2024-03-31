package com.chatapp.vibeapp.screens

import android.provider.Contacts.ContactMethods
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chatapp.vibeapp.data.Contact
import com.chatapp.vibeapp.viewmodels.ContactsViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ContactsScreen(contactsViewModel: ContactsViewModel = viewModel(), navController: NavController) {
    val contacts by contactsViewModel.contacts.observeAsState(initial = emptyList())
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val navigationEvent by contactsViewModel.navigationEvent.observeAsState()

    // Trigger loading contacts
    LaunchedEffect(key1 = true) {
        contactsViewModel.loadContacts()
    }

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            // Navigate to the chat screen with the chatSessionId
            navController.navigate("chatScreen/$it")
            contactsViewModel.clearNavigationEvent()
        }
    }


    Scaffold (topBar = { AppBarView(title = "Contacts") }
    ) {
       LazyColumn(modifier = Modifier
           .fillMaxSize()
           .padding(it)) 
       {
           items(contacts) { contact ->
               ContactItem(contact = contact, currentUser = currentUser, navController = navController, viewModel = contactsViewModel)
           }
       }

    }

}

@Composable
fun ContactItem(contact: Contact,currentUser: String, navController: NavController, viewModel: ContactsViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(contact.name, modifier = Modifier.weight(1f))
        Button(onClick = {
            if (contact.isUser) {
                // Navigate to Chat Screen with this contact
                val chatSessionId =
                    viewModel.getOrCreateChatSessionId(currentUser, contact.phoneNumber)
                navController.navigate("chatScreen/${chatSessionId}")
            } else {
                // Handle invite action
                // For example, sending an SMS invite or opening an invite screen
            }
        })
        {
            Text(if (contact.isUser) "Chat" else "Invite")
        }
    }
}