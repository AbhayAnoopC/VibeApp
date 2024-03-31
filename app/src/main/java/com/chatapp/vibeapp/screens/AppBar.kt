package com.chatapp.vibeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.chatapp.vibeapp.R
import com.chatapp.vibeapp.ui.theme.Purple80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    title: String,
    onBackNavClicked: () -> Unit= {}
){

    val navigationIcon : (@Composable () -> Unit)? =
        if(title.contains("Contacts")){
            {
                IconButton(onClick = { onBackNavClicked() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        }else{
            null
        }
    TopAppBar(
        title = {
            Text(text = title,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp))
        },
        //modifier = Modifier.background(color = Purple80),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue
        )
    )
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppBarView(
//    title: String,
//    onBackNavClicked: () -> Unit = {},
//    onSearchQueryChanged: (String) -> Unit = {}
//) {
//    var showSearch by remember { mutableStateOf(false) }
//    val navigationIcon : (@Composable () -> Unit)? =
//        if(!title.contains("Contacts")){
//            {
//                IconButton(onClick = { onBackNavClicked() }) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        tint = Color.White,
//                        contentDescription = null
//                    )
//                }
//            }
//        }else{
//            null
//        }
//    if (showSearch) {
//        var searchQuery by remember { mutableStateOf("") }
//
//        SmallTopAppBar(
//            title = {
//                TextField(
//                    value = searchQuery,
//                    onValueChange = {
//                        searchQuery = it
//                        onSearchQueryChanged(it)
//                    },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        //textColor = Color.White,
//                        cursorColor = Color.White
//                    ),
//                    singleLine = true,
//                    placeholder = { Text("Search contacts...") }
//                )
//            },
//            navigationIcon = {
//                IconButton(onClick = { showSearch = false }) {
//                    Icon(Icons.Filled.ArrowBack, "Back", tint = Color.White)
//                }
//            },
//            colors = TopAppBarDefaults.smallTopAppBarColors(
//                containerColor = Purple80 // Adjust your color accordingly
//            )
//        )
//    } else {
//        (if (!title.contains("Contacts")) navigationIcon else null)?.let {
//            SmallTopAppBar(
//                title = { Text(text = title, color = Color.White) },
//                navigationIcon = it,
//                actions = {
//                    IconButton(onClick = { showSearch = true }) {
//                        Icon(Icons.Filled.Search, "Search", tint = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.smallTopAppBarColors(
//                    containerColor = Purple80 // Adjust your color accordingly
//                )
//            )
//        }
//    }
//}