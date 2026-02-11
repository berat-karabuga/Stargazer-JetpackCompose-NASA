package com.stargazer.stargazer.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stargazer.stargazer.util.Resource

data class Badge(
    val dayRequirement: Int,
    val title: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.userStats.collectAsStateWithLifecycle()

    val badges = listOf(
        Badge(1, "Acemi Kaşif", Icons.Default.Star),
        Badge(7, "Haftalık Yıldız", Icons.Default.Star),
        Badge(14, "Kararlı Gözlemci", Icons.Default.Star),
        Badge(30, "Ayın Elemanı", Icons.Default.Star),
        Badge(60, "Uzay Kurdu", Icons.Default.Star),
        Badge(90, "Galaksi Rehberi", Icons.Default.Star),
        Badge(365, "Voyager", Icons.Default.Star)
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profil & Başarımlar") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = state) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                is Resource.Error -> Text(
                    text = result.message ?: "Hata",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )

                is Resource.Success -> {
                    val user = result.data!!
                    val currentStreak = user.streak

                    val rank = when {
                        currentStreak >= 365 -> "Voyager 🛸"
                        currentStreak >= 90 -> "Galaksi Uzmanı 🌌"
                        currentStreak >= 30 -> "Stargazer 🔭"
                        currentStreak >= 7 -> "Hevesli Gözlemci 👀"
                        else -> "Çaylak (Cadet) 👶"
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👨‍🚀", fontSize = 50.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = user.email.substringBefore("@"),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = rank,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF5722)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$currentStreak Günlük Seri!",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Rozet Koleksiyonu",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(badges) { badge ->
                                val isUnlocked = currentStreak >= badge.dayRequirement

                                BadgeItem(badge = badge, isUnlocked = isUnlocked)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeItem(badge: Badge, isUnlocked: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.primaryContainer else Color.LightGray
        ),
        modifier = Modifier.height(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isUnlocked) badge.icon else Icons.Default.Lock,
                contentDescription = null,
                tint = if (isUnlocked) MaterialTheme.colorScheme.primary else Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = badge.title,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1
            )
            Text(
                text = "${badge.dayRequirement} Gün",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}