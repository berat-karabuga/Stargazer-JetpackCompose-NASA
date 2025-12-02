package com.stargazer.stargazer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun second(navController: NavController){

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.Blue)
        , horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center

    ) {

        Text("slm cnm")

        Button(onClick = {
            auth.signOut()
            navController.navigate(Screen.Entry.route)
        }) {
            Text(
                "çıkış"
            )
        }

    }

}