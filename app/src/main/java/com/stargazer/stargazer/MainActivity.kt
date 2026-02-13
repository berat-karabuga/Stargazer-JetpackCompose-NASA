package com.stargazer.stargazer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.stargazer.stargazer.presentation.StargazerRoot
import com.stargazer.stargazer.ui.theme.StargazerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StargazerTheme {
                StargazerRoot()
            }
        }
    }
}