package com.chatapp.vibeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chatapp.vibeapp.data.AuthViewModel
import com.chatapp.vibeapp.data.Screen
import com.chatapp.vibeapp.screens.ChatRoomScreen
import com.chatapp.vibeapp.screens.ChatScreen
import com.chatapp.vibeapp.screens.ContactsScreen
import com.chatapp.vibeapp.screens.LoginScreen
import com.chatapp.vibeapp.screens.ProfileScreen
import com.chatapp.vibeapp.screens.SignUpScreen
import com.chatapp.vibeapp.screens.StartupScreen
import com.chatapp.vibeapp.ui.theme.VibeAppTheme
import com.chatapp.vibeapp.viewmodels.ChatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            VibeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navController=navController, authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.StartupScreen.route
    ) {
        composable(Screen.StartupScreen.route) {
            StartupScreen(
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) },
                onNavigateToSignup = { navController.navigate(Screen.SignupScreen.route) }
            )
        }
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) },
                onNavigateToProfile = {navController.navigate(Screen.ProfileScreen.route)}
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignup = { navController.navigate(Screen.SignupScreen.route) }
            ) {
                navController.navigate(Screen.ChatScreen.route)
            }
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(
                onNavigateToChat = { navController.navigate(Screen.ChatScreen.route) }
            )
        }

        composable(Screen.ChatScreen.route) {
            ChatScreen (
                //onNavigateToContacts = { navController.navigate(Screen.ContactsScreen.route) }
            )
        }
        composable(Screen.ContactsScreen.route) {
            ContactsScreen (
                contactsViewModel = ViewModel, navController = NavController
                //onNavigateToContacts = { navController.navigate(Screen.ContactsScreen.route) }
            )
        }

        composable(
            route = "chatRoom/{chatRoomId}",
            arguments = listOf(navArgument("chatRoomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel: ChatViewModel = viewModel()
            ChatRoomScreen(
                chatRoomId = backStackEntry.arguments?.getString("chatRoomId") ?: "",
                viewModel = viewModel
            )
        }


    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    VibeAppTheme {
//        Greeting("Android")
//    }
//}