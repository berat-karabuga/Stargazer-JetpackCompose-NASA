package com.stargazer.stargazer

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.stargazer.stargazer.ui.theme.StargazerTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

public lateinit var auth: FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StargazerTheme {
                auth = Firebase.auth
                val navController = rememberNavController()
                val currentUser: FirebaseUser? = auth.currentUser
                val startRoute = if (currentUser != null){
                    Screen.Second.route
                }else{
                    Screen.Entry.route
                }
                navi(navController, startRoute)
            }
        }
    }
}

@Composable
fun entryScreen(navHostController: NavHostController){
    var email by remember { mutableStateOf("") }
    var parola by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    val context = LocalContext.current
    val hel = second(navHostController)

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Email giriniz")}
        )

        Spacer(modifier = Modifier.padding(16.dp))

        TextField(
            value = parola,
            onValueChange = {parola = it},
            label = {Text("Şifre giriniz")}
        )

        Spacer(modifier = Modifier.padding(16.dp))

        TextField(
            value = username,
            onValueChange = {username = it},
            label = {Text("kullanıcı adı giriniz")}
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround

        ){
            Button(onClick = {
                auth.createUserWithEmailAndPassword(email,parola)
                    .addOnCompleteListener(context as ComponentActivity) { task ->
                        if (task.isSuccessful){
                            val guncelKullanıcı = auth.currentUser
                            val profilcGuncelleme = UserProfileChangeRequest.Builder().apply{
                                displayName = username
                            }.build()

                            if(guncelKullanıcı != null){
                                guncelKullanıcı.updateProfile(profilcGuncelleme).addOnCompleteListener{ updateTask ->
                                    if (updateTask.isSuccessful){
                                        Toast.makeText(context.applicationContext, "Profil Kullanıcı Adı Eklendi: $username", Toast.LENGTH_LONG ).show()
                                    }
                                }
                            }
                            navHostController.navigate(Screen.Second.route)
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            })
            {
                Text(text = "Kayıt ol")
            }


            Button(onClick = {
                auth.signInWithEmailAndPassword(email,parola)
                    .addOnCompleteListener(context as ComponentActivity){ task ->
                        if(task.isSuccessful){
                            val guncelkullanıcı0 = auth.currentUser?.displayName.toString()
                            navHostController.navigate(Screen.Second.route)
                            Toast.makeText(context.applicationContext,"hoş geldin ${guncelkullanıcı0}" ,Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener{ exception ->
                        Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            }) {
                Text(text = "Giriş yap")
            }
        }
    }
}