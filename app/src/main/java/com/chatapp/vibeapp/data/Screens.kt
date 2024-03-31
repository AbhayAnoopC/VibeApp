package com.chatapp.vibeapp.data

sealed class Screen(val route:String){
    object StartupScreen:Screen("startupscreen")
    object LoginScreen:Screen("loginscreen")
    object SignupScreen:Screen("signupscreen")
    object ChatRoomsScreen:Screen("chatroomscreen")
    object ChatScreen:Screen("chatscreen")
    object ProfileScreen:Screen("profilescreen")
    object ContactsScreen:Screen("contactsscreen")
}