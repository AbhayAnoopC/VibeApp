package com.chatapp.vibeapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch



@Composable
fun ProfileScreen(
    onNavigateToChat: () -> Unit,

) {
    var nickName = ""
    var status = ""
    var profilePicture = ""

    val showPermissionRequest = remember { mutableStateOf(false) }

    // Trigger the permission request process
    if (showPermissionRequest.value) {
        SimplePermissionRequest(onPermissionGranted = {
            onNavigateToChat()
        })
    }

    Column (modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row() {

        }

        Button(onClick = { showPermissionRequest.value = true }) {
            Text("Finish Signup ")
        }
    }

}


@Composable
fun SimplePermissionRequest(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) onPermissionGranted()
        }
    )
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }
//    Button(onClick = {
//        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
//    }) {
//        Text("Request Permission")
//    }
}