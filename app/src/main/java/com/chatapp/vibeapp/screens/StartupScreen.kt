package com.chatapp.vibeapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chatapp.vibeapp.R

@Composable
fun StartupScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignup: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Align children in the center horizontally
        verticalArrangement = Arrangement.Center // Center the content vertically
    ) {

        Image(
            painter = painterResource(R.drawable.logo1),
            contentDescription = "App Logo",
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    //navigate to sign up screen
                    onNavigateToLogin()
                },
                modifier = Modifier
                    //.fillMaxWidth()
                    .padding(8.dp)
                //.clickable { onNavigateToLogin() }
            ) {
                Text("Sign In")
            }

            Button(
                onClick = {
                    //navigate to signup screen
                    onNavigateToSignup()
                },
                modifier = Modifier
                    //.fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Sign Up")
            }
        }
    }


}