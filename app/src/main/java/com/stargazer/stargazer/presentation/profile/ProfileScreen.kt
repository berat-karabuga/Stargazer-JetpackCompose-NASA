package com.stargazer.stargazer.presentation.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stargazer.stargazer.R
import com.stargazer.stargazer.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Badge(
    val dayRequirement: Int,
    val title: String,
    val iconResId: Int,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.userStats.collectAsStateWithLifecycle()
    var selectedBadge by remember { mutableStateOf<Badge?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val badges = listOf(
        Badge(1, "Observer", R.drawable.badge_observer, "for taking the first step into the void and looking up with curiosity."),
        Badge(7, "Explorer", R.drawable.badge_explorer, "for showing a week of relentless curiosity and stability."),
        Badge(14, "Pathfinder", R.drawable.badge_pathfinder, "for the determination to discover new paths in the cosmos."),
        Badge(30, "Vanguard", R.drawable.badge_vanguard, "for a month of unwavering discipline and pioneering spirit."),
        Badge(60, "Centurion", R.drawable.badge_centurion, "for two months of experience and loyalty to the space corps."),
        Badge(90, "Commander", R.drawable.badge_commander, "for leadership and wisdom demonstrated over a full season."),
        Badge(180, "Celestial", R.drawable.badge_celestial, "for a legendary half-year journey and celestial knowledge."),
        Badge(365, "Voyager", R.drawable.badge_voyager, "for mastering the infinity journey over a complete solar cycle.")
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Commander Profile", fontWeight = FontWeight.Bold) }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = state) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Error -> Text(text = "Connection Error", modifier = Modifier.align(Alignment.Center))
                is Resource.Success -> {
                    val user = result.data!!
                    val currentStreak = user.streak
                    val username = if (user.username.isNotEmpty()) user.username else "Skywatcher"

                    val rank = when {
                        currentStreak >= 365 -> "Voyager"
                        currentStreak >= 180 -> "Celestial"
                        currentStreak >= 90 -> "Commander"
                        currentStreak >= 60 -> "Centurion"
                        currentStreak >= 30 -> "Vanguard"
                        currentStreak >= 14 -> "Pathfinder"
                        currentStreak >= 7 -> "Explorer"
                        else -> "Observer"
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_profile_placeholder),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = username, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(text = rank, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(24.dp))

                        StreakCard(streak = currentStreak)

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Badge Collection", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(badges) { badge ->
                                val isUnlocked = currentStreak >= badge.dayRequirement
                                BadgeItem(
                                    badge = badge,
                                    isUnlocked = isUnlocked,
                                    onClick = {
                                        selectedBadge = badge
                                        showDialog = true
                                    }
                                )
                            }
                        }
                    }

                    if (showDialog && selectedBadge != null) {
                        val badge = selectedBadge!!
                        val isUnlocked = currentStreak >= badge.dayRequirement

                        Dialog(onDismissRequest = { showDialog = false }) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = badge.iconResId),
                                        contentDescription = null,
                                        modifier = Modifier.size(100.dp),
                                        colorFilter = if (!isUnlocked) ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }) else null
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = badge.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val message = if (isUnlocked) {
                                        "Congratulations Commander $username! \n\nAwarded ${badge.description} You have unlocked the '${badge.title}' rank."
                                    } else {
                                        "Access Denied \n\nMaintain your streak for ${badge.dayRequirement} days to decode this badge."
                                    }

                                    Text(
                                        text = message,
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(onClick = { showDialog = false }) { Text("Dismiss") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StreakCard(streak: Int) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rocketAlpha = remember { Animatable(1f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                scope.launch {
                    offsetX.snapTo(0f)
                    offsetY.snapTo(0f)
                    rocketAlpha.snapTo(1f)

                    launch { offsetX.animateTo(300f, tween(600)) }
                    launch { offsetY.animateTo(-300f, tween(600)) }
                    launch { rocketAlpha.animateTo(0f, tween(400)) }

                    delay(700)
                    offsetX.snapTo(0f)
                    offsetY.snapTo(0f)
                    rocketAlpha.animateTo(1f, tween(300))
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF335889),
                            Color(0xFF5A7CA6)
                        )
                    )
                )
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = offsetX.value.dp, y = offsetY.value.dp)
                        .alpha(rocketAlpha.value)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "$streak Day Streak!",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BadgeItem(badge: Badge, isUnlocked: Boolean, onClick: () -> Unit) {
    val grayScaleMatrix = ColorMatrix().apply { setToSaturation(0f) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surfaceVariant else Color.LightGray.copy(alpha = 0.2f)
        ),
        modifier = Modifier
            .height(120.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = badge.iconResId),
                    contentDescription = badge.title,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(bottom = 4.dp),
                    colorFilter = if (!isUnlocked) ColorFilter.colorMatrix(grayScaleMatrix) else null,
                    alpha = if (isUnlocked) 1f else 0.4f
                )

                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isUnlocked) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
                )

                Text(
                    text = "${badge.dayRequirement} Days",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            if (!isUnlocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(16.dp)
                )
            }
        }
    }
}