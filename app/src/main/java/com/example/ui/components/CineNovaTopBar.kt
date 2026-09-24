package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLowest

@Composable
fun CineNovaTopBar(
    title: String? = null,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onCastClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onStudioOpsClick: () -> Unit = {},
    onWatchlistClick: () -> Unit = {},
    onReviewsClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest.copy(alpha = 0.90f))
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Logo or Back + Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                if (showBack) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("topbar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Brand Emblem + Typography
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onBack() }
                ) {
                    // Custom Cine Nova Emblem disc
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1B1B22))
                            .border(1.dp, CrimsonPrimary.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Cine Nova Emblem",
                            tint = GoldSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (title != null) {
                        Text(
                            text = title,
                            color = OnSurface,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CINE ",
                                color = OnSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "NOVA",
                                color = CrimsonPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            // Right Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language Switcher
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerHigh)
                        .clickable { onLanguageClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("language_selector_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ML • TE • TA • HI",
                        color = CyanTertiary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                // Watchlist Quick Shortcut Button
                IconButton(
                    onClick = onWatchlistClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("topbar_watchlist_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "My Watchlist",
                        tint = GoldSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Reviews Quick Shortcut Button
                IconButton(
                    onClick = onReviewsClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("topbar_reviews_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.RateReview,
                        contentDescription = "Indie Reviews",
                        tint = CyanTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Cast to Screen Button
                IconButton(
                    onClick = onCastClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("cast_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Cast,
                        contentDescription = "Cast to TV",
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Studio Ops Telemetry Quick Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF00363D))
                        .border(1.dp, CyanTertiary.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .clickable { onStudioOpsClick() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("studio_ops_badge")
                ) {
                    Text(
                        text = "OPS",
                        color = CyanTertiary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }

                // User Profile Avatar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(CrimsonPrimary, GoldSecondary, CyanTertiary)
                            )
                        )
                        .padding(1.5.dp)
                        .clickable { onProfileClick() }
                        .testTag("profile_avatar_button")
                ) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuD1LdRnTPDVoIXFKu6PE8fwlbyaGOsjuxMuhYLFdkEUnvjoOEU4t2p7Wv9suw7I-aKvZq7OSfw8bC50LyOIJQKXIKa5FU0GMqK0xRawpof3dPs6MZHKRdBxQEb8NvMBg5GdhWsfP01msruvJ3Pih8m3fehLgmUEW7TOi53udpsXxaTLK2NbMKngtGk7CRThldiB7K29zd7vCReFGgmyzxReMs5odqJBy1FrpYxhJJMNjrGL-aaOyI0CHg",
                        contentDescription = "User Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                    )
                }
            }
        }
    }
}
