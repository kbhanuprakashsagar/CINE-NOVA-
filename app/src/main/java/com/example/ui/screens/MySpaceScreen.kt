package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Movie
import com.example.data.repository.MovieRepository
import com.example.ui.components.ContinueWatchingCard
import com.example.ui.components.PortraitMovieCard
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Outline
import com.example.ui.theme.PrimaryLight
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest

@Composable
fun MySpaceScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateToDownloads: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToWatchlist: () -> Unit = {},
    onNavigateToReviews: () -> Unit = {}
) {
    val movies by MovieRepository.movies.collectAsState()
    val watchlist = movies.filter { it.isWatchlisted }
    val continueWatching = movies.filter { it.progress > 0 }
    val wifiOnly by MovieRepository.isWifiOnlyDownload.collectAsState()

    var notificationsEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .testTag("my_space_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerLow)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar with luminous gradient halo
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(CrimsonPrimary, GoldSecondary, CyanTertiary)
                                    )
                                )
                                .padding(2.5.dp)
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuD1LdRnTPDVoIXFKu6PE8fwlbyaGOsjuxMuhYLFdkEUnvjoOEU4t2p7Wv9suw7I-aKvZq7OSfw8bC50LyOIJQKXIKa5FU0GMqK0xRawpof3dPs6MZHKRdBxQEb8NvMBg5GdhWsfP01msruvJ3Pih8m3fehLgmUEW7TOi53udpsXxaTLK2NbMKngtGk7CRThldiB7K29zd7vCReFGgmyzxReMs5odqJBy1FrpYxhJJMNjrGL-aaOyI0CHg",
                                contentDescription = "Ananya Menon",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceContainerLowest),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = CyanTertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        // Identity & Status
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ananya Menon",
                                    color = OnSurface,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = { /* Edit profile */ },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Profile",
                                        tint = Outline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Text(
                                text = "ananya.cinephile@novastream.in",
                                color = Outline,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Badges
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(CrimsonPrimary.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.WorkspacePremium,
                                            contentDescription = null,
                                            tint = PrimaryLight,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "VIP Tier",
                                            color = PrimaryLight,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(CyanTertiary.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Auteur Patron",
                                        color = CyanTertiary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Metrics Ribbon
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainer.copy(alpha = 0.6f))
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        MetricBlock(number = "142", label = "Watched", color = OnSurface)
                        MetricBlock(number = "${watchlist.size}", label = "In Watchlist", color = GoldSecondary)
                        MetricBlock(number = "4", label = "Active Offline", color = CyanTertiary)
                    }
                }
            }
        }

        // Subscription Status Banner
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(SurfaceContainer, SurfaceContainerHigh, SurfaceContainerLow)
                        )
                    )
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldSecondary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = GoldSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Cine Nova Premium 4K",
                                    color = OnSurface,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "UHD",
                                    color = OnSecondaryContainer,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(SecondaryContainer, RoundedCornerShape(3.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                            Text(
                                text = "Renews Nov 14, 2025 • ₹799/yr",
                                color = Outline,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToSubscription,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHighest,
                            contentColor = OnSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("manage_subscription_button")
                    ) {
                        Text(
                            text = "Manage",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Perks Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PerkTag(Icons.Default.Tv, "4 Screens", GoldSecondary)
                    PerkTag(Icons.Default.SpatialAudio, "Dolby Atmos", CyanTertiary)
                    PerkTag(Icons.Default.Block, "Ad-Free", CrimsonPrimary)
                    PerkTag(Icons.Default.Downloading, "Unlimited Offline", CyanTertiary)
                }
            }
        }

        // Downloaded Cinema Card with Storage Bar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLow)
                    .clickable { onNavigateToDownloads() }
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyanTertiary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DownloadForOffline,
                                contentDescription = null,
                                tint = CyanTertiary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Downloaded Cinema",
                                color = OnSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "4 Titles • Ready for travel",
                                color = Outline,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Outline
                    )
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Device Storage", color = Outline, fontSize = 10.sp)
                        Text("12.4 GB / 64 GB", color = Outline, fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { 0.193f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = CyanTertiary,
                        trackColor = SurfaceContainerHighest
                    )
                }
            }
        }

        // Quick Toggles Row (Audio & Subs + Content Filters)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .clickable { /* Audio toggles */ }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHighest),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Subtitles,
                            contentDescription = null,
                            tint = CrimsonPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text("Audio & Subs", color = OnSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Malayalam + EN", color = Outline, fontSize = 10.sp)
                    }
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .clickable { /* Filter toggles */ }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHighest),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text("Content Filters", color = OnSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Festival Uncut", color = Outline, fontSize = 10.sp)
                    }
                }
            }
        }

        // Cinema DNA Affinity Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(SurfaceContainerLow, SurfaceContainer, SurfaceContainerLowest)
                        )
                    )
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = CyanTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CINEMA DNA AFFINITY",
                            color = CyanTertiary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "AI Synthesis",
                        color = Outline,
                        fontSize = 10.sp
                    )
                }

                Text(
                    text = "74% Neo-Noir & Folk Mysticism • 26% Social Realism",
                    color = OnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // Affinity Spectrum Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SurfaceContainerHighest)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.74f)
                            .fillMaxSize()
                            .background(CyanTertiary)
                    )
                    Box(
                        modifier = Modifier
                            .weight(0.26f)
                            .fillMaxSize()
                            .background(CrimsonPrimary)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("● Mysticism / Folk Noir", color = CyanTertiary, fontSize = 10.sp)
                    Text("● Cinema Verite / Realism", color = CrimsonPrimary, fontSize = 10.sp)
                }
            }
        }

        // Continue Watching Rail
        if (continueWatching.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(CrimsonPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Continue Watching",
                            color = OnSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "${continueWatching.size} In-Progress",
                        color = Outline,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(continueWatching) { movie ->
                        ContinueWatchingCard(
                            movie = movie,
                            onPlayClick = { onNavigateToPlayer(movie.id) }
                        )
                    }
                }
            }
        }

        // Watchlist Rail
        if (watchlist.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Watchlist",
                            color = OnSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SurfaceContainerHighest)
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${watchlist.size}",
                                color = Outline,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onNavigateToWatchlist() }
                            .testTag("view_all_watchlist_button")
                    ) {
                        Text(
                            text = "View All",
                            color = CyanTertiary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = CyanTertiary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(watchlist) { movie ->
                        PortraitMovieCard(
                            movie = movie,
                            onMovieClick = { onNavigateToDetail(movie.id) },
                            onToggleWatchlist = { MovieRepository.toggleWatchlist(movie.id) }
                        )
                    }
                }
            }
        }

        // Recently Logged & Rated
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recently Logged & Rated",
                        color = OnSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onNavigateToReviews() }
                            .testTag("view_all_reviews_button")
                    ) {
                        Text(
                            text = "All Reviews",
                            color = CyanTertiary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = CyanTertiary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Rated movie 1
                RatedMovieCard(
                    title = "Manjummel Boys",
                    rating = "★ 9.5",
                    info = "Completed Oct 28 • Malayalam",
                    quote = "\"Masterclass in tension & camaraderie\"",
                    posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD8fBg99ZDjBV2OgQk_bToDdzvODIkfvNa0THaHrZQoBRq2TXYzW9wu9qdH4-LAwtwEcTkd32aUe9nciCBjRxYPeHRpe_tXt20wkCjKaHDZPK3TRKqVL8qz_aqd05IZnAJr4KUlfra0gCXynQcT9TSXaUhdWR-155SHsSu6MLShBRiQ3QBGyvDofaGWLYcLltccmj5cMoWExC7gYC1VvtRT0YVwcXr1376X4noQPhF0LGLk69CK6lJXwA",
                    onReplay = { onNavigateToPlayer("manjummel_boys") }
                )

                // Rated movie 2
                RatedMovieCard(
                    title = "Viduthalai Part 1",
                    rating = "★ 9.0",
                    info = "Completed Oct 14 • Tamil",
                    quote = "\"Vetri Maaran's uncompromising lens\"",
                    posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAo0PMg5mYvts1JU0vMre23FvRlzWAUCcyU9rrZxDggm676Ny2savJD1rhbVwCsFUOmZGw51mWL5w-5eyAJ1w7usVC8m61Mj3vgyGv4V23K0TqlYG9jzNfclVDMEzeUNP-Neh65dbjwz3lRgGR3-i8VSr_dYbfa0fe25fC0TYYTO1zaa9JhW84j2uUOfOAcPfL7BQe8i9UNoDtdtlsFBT0pijYTP79jGxyPYtm7PMOk4qNn0aL51bcO0A",
                    onReplay = { onNavigateToPlayer("viduthalai_1") }
                )
            }
        }

        // Curated Next For Ananya Spotlight Strip
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerLow)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(CyanTertiary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = CyanTertiary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Curated Next For Ananya",
                            color = OnSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Matched with your Folk Mysticism affinity",
                            color = Outline,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(68.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAoR5jyk1Wg4opPfNIaJvfBvUA0wmm_cyco6StTQklQYauB5w6u706SkBcK_JkLuKUAIqKnD-v9A93pxWYZxmRFDEcsLxxE5x_JlbLx1whTze52f5ug-ti4Msg4zSAk0ojC3FLvripDr6kn5WGs4hdIXVO_Mkfo0AoaNm6kj-rLJCoOqs4_wxocJu06AK1r1uuWT3TJjAh42zHsnnBYnkd_cJGH9uJYGznYQ7vwp7sVJIatEiEgrX2QpA",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(3.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(CyanTertiary)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "98% Match",
                                color = Color.Black,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Kantara: A Legend",
                            color = OnSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Sacred demigod rites, indigenous spirit lore, and blistering ritual fury.",
                            color = Outline,
                            fontSize = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onNavigateToDetail("kantara") }
                                .padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = CrimsonPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Watch Trailer",
                                color = CrimsonPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // App & Stream Settings Menu
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "App & Stream Settings",
                    color = OnSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(vertical = 4.dp)
                ) {
                    SettingMenuItem(
                        icon = Icons.Default.Hd,
                        title = "Video Streaming Quality",
                        subtitle = "Auto 4K Ultra HD (Dolby Vision)",
                        onClick = { /* Quality options */ }
                    )

                    // Wi-Fi Only toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = Outline)
                            Column {
                                Text("Download on Wi-Fi Only", color = OnSurface, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text("Saves cellular data allocation", color = Outline, fontSize = 11.sp)
                            }
                        }

                        Switch(
                            checked = wifiOnly,
                            onCheckedChange = { MovieRepository.toggleWifiOnly() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = OnPrimaryContainer,
                                checkedTrackColor = CrimsonPrimary
                            )
                        )
                    }

                    SettingMenuItem(
                        icon = Icons.Default.NotificationsActive,
                        title = "Festival Drops & Reminders",
                        subtitle = "IFFK, Cannes, and Regional premieres",
                        onClick = { notificationsEnabled = !notificationsEnabled }
                    )

                    SettingMenuItem(
                        icon = Icons.Default.ContactSupport,
                        title = "Help Center & Diaspora Support",
                        subtitle = "24/7 Cinephile concierge",
                        onClick = { /* Help */ }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Logout */ }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = ErrorRed)
                            Text("Sign Out of Cine Nova", color = ErrorRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("v3.4.1", color = Outline.copy(alpha = 0.7f), fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricBlock(number: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(number, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Outline, fontSize = 10.sp)
    }
}

@Composable
private fun PerkTag(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(text, color = Outline, fontSize = 10.sp)
    }
}

@Composable
private fun RatedMovieCard(
    title: String,
    rating: String,
    info: String,
    quote: String,
    posterUrl: String,
    onReplay: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainerLow)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceContainer)
            ) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, color = OnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = rating,
                        color = GoldSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(GoldSecondary.copy(alpha = 0.15f), RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
                Text(info, color = Outline, fontSize = 10.sp)
                Text(quote, color = CyanTertiary, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }

        IconButton(
            onClick = onReplay,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh)
        ) {
            Icon(Icons.Default.Replay, contentDescription = "Replay", tint = CrimsonPrimary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun SettingMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Outline, modifier = Modifier.size(18.dp))
            Column {
                Text(title, color = OnSurface, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Outline, fontSize = 11.sp)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Outline, modifier = Modifier.size(18.dp))
    }
}
