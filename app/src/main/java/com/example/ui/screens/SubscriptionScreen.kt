package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VideoSettings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.MovieRepository
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.CyanTertiary
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
fun SubscriptionScreen(
    onBack: () -> Unit
) {
    var isYearly by remember { mutableStateOf(true) }
    var isMatrixExpanded by remember { mutableStateOf(true) }
    var selectedFaqIndex by remember { mutableStateOf<Int?>(null) }

    val faqs = listOf(
        "Can I download movies to an external SD card?" to "Yes! On the Android mobile and tablet app, you can select custom internal storage or an external high-speed microSD card for offline viewing of all 1080p and 4K cinema master prints.",
        "Can I switch between monthly and annual plans?" to "Absolutely. Upgrading from monthly to yearly prorates your existing billing balance immediately, applying credit automatically. When downgrading, the change takes effect at the close of your current active billing cycle.",
        "How does Cine Nova family screening work?" to "Premium 4K permits up to 5 customized cinephile profiles with distinct linguistic subtitle preferences, curated watchlists, and individual Cinephile AI taste calibration."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .testTag("subscription_screen"),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        // App Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }
                    Text(
                        text = "Subscription Pass",
                        color = OnSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(GoldSecondary.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "VIP MEMBER",
                        color = GoldSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Hero Proposition
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Auteur Edition Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerHigh)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "AUTEUR EDITION • PASS",
                        color = GoldSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Choose Your Cinema\nExperience",
                    color = OnSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 30.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Unrestricted access to award-winning regional masterworks, indie premieres, 4K Dolby Atmos, and Cinephile AI curation.",
                    color = OnSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Active Subscription Ribbon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
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
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = CrimsonPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Cine Nova Premium (Annual)",
                                    color = OnSurface,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(CrimsonPrimary)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        color = OnPrimaryContainer,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                            Text(
                                text = "Next billing: Nov 14, 2025 • Auto-renewal active",
                                color = Outline,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Text(
                        text = "Manage",
                        color = PrimaryLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Billing Cycle Toggle (Monthly vs Yearly)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(SurfaceContainerLowest)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (!isYearly) SurfaceContainerHigh else Color.Transparent)
                            .clickable { isYearly = false }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Monthly",
                            color = if (!isYearly) OnSurface else Outline,
                            fontSize = 12.sp,
                            fontWeight = if (!isYearly) FontWeight.Bold else FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isYearly) SurfaceContainerHigh else Color.Transparent)
                            .clickable { isYearly = true }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Yearly",
                                color = if (isYearly) OnSurface else Outline,
                                fontSize = 12.sp,
                                fontWeight = if (isYearly) FontWeight.Bold else FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SecondaryContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "SAVE 35%",
                                    color = OnSecondaryContainer,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Includes 2 Months Free Cinema Pass",
                        color = GoldSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Outline, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "DEMO PRICING • SUBJECT TO LAUNCH TERMS",
                        color = Outline,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Plan 1: PREMIUM 4K (Active Plan with Glowing Accent)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerHigh)
                    .border(1.dp, CrimsonPrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "PREMIUM 4K",
                                color = OnSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Star, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = "ULTRA CINEPHILE EXPERIENCE",
                            color = PrimaryLight,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CrimsonPrimary)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = OnPrimaryContainer, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ACTIVE PLAN",
                                color = OnPrimaryContainer,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // Price
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = if (isYearly) "₹799" else "₹149",
                        color = OnSurface,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = if (isYearly) " / year" else " / month",
                        color = Outline,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (isYearly) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹1,249",
                            color = Outline,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Text(
                    text = "Effective cost ₹66/month. Billed annually with instant archival perks.",
                    color = Outline,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Feature Highlights
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FeatureCheck(Icons.Default.VideoSettings, "4K UHD + Dolby Vision & HDR10+", CyanTertiary)
                    FeatureCheck(Icons.Default.SurroundSound, "Dolby Atmos & 5.1 Master Audio", GoldSecondary)
                    FeatureCheck(Icons.Default.Tv, "4 Simultaneous Screens (Projector/TV/App)", OnSurfaceVariant)
                    FeatureCheck(Icons.Default.Download, "Unlimited Offline 4K & FHD Archival", OnSurfaceVariant)
                    FeatureCheck(Icons.Default.Block, "100% Zero-Ad Cinema Flow", CrimsonPrimary)
                    FeatureCheck(Icons.Default.WorkspacePremium, "IFFK / IFFI Festival Premiere Pass", CyanTertiary)
                }

                Button(
                    onClick = { /* Already active */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainerHighest,
                        contentColor = OnSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Currently Subscribed (Renews Nov 2025)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Plan 2: STANDARD HD
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(14.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("STANDARD HD", color = OnSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(SurfaceContainerHighest)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text("POPULAR", color = Outline, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("LIVING ROOM CINEMA", color = Outline, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text("Downgrade", color = OnSurface, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = if (isYearly) "₹499" else "₹99",
                        color = OnSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(text = if (isYearly) " / year" else " / month", color = Outline, fontSize = 12.sp)
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FeatureCheck(Icons.Default.Videocam, "1080p Full HD Resolution", OnSurface)
                    FeatureCheck(Icons.Default.Speaker, "5.1 Surround Studio Audio", OnSurface)
                    FeatureCheck(Icons.Default.Tv, "2 Simultaneous Screens (TV + App)", OnSurface)
                    FeatureCheck(Icons.Default.Download, "Up to 15 Offline Titles", OnSurface)
                }
            }
        }

        // Plan 3: CINE MOBILITY
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(14.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("CINE MOBILITY", color = OnSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("ON-THE-GO FILM ENTHUSIAST", color = Outline, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text("Switch", color = OnSurface, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = if (isYearly) "₹299" else "₹49",
                        color = OnSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(text = if (isYearly) " / year" else " / month", color = Outline, fontSize = 12.sp)
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FeatureCheck(Icons.Default.Smartphone, "720p HD (Mobile & Tablet Only)", OnSurface)
                    FeatureCheck(Icons.Default.Headset, "Standard Stereo 2.0 Audio", OnSurface)
                    FeatureCheck(Icons.Default.ScreenShare, "1 Active Screen", OnSurface)
                    FeatureCheck(Icons.Default.Download, "5 Offline Download Slots", OnSurface)
                }
            }
        }

        // Compare All Plan Specs Collapsible Matrix
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainer)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isMatrixExpanded = !isMatrixExpanded },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CompareArrows, contentDescription = null, tint = CrimsonPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Compare All Plan Specs",
                            color = OnSurface,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = if (isMatrixExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Outline
                    )
                }

                AnimatedVisibility(visible = isMatrixExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("FEATURE", color = Outline, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text("MOBILITY", color = Outline, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text("STANDARD", color = Outline, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text("PREMIUM", color = CrimsonPrimary, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                        }

                        MovieRepository.planSpecs.forEach { spec ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceContainerLow)
                                    .padding(horizontal = 6.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(spec.feature, color = OnSurface, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                Text(spec.mobility, color = Outline, fontSize = 10.sp, modifier = Modifier.weight(1f))
                                Text(spec.standard, color = OnSurfaceVariant, fontSize = 10.sp, modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(CrimsonPrimary.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(spec.premium, color = PrimaryLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Trust & Payment Guarantee Banner
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLow)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(24.dp))
                    Column {
                        Text("Guaranteed Seamless Cinema", color = OnSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Cancel anytime in 1 tap • No lock-in period", color = Outline, fontSize = 11.sp)
                    }
                }

                Text("SUPPORTED INDIAN PAYMENT GATEWAYS", color = Outline, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("⚡ UPI / GPay", "PhonePe", "Paytm", "RuPay", "NetBanking").forEach { gateway ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerHighest)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(gateway, color = OnSurface, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // FAQs
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Frequently Asked Questions",
                    color = OnSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                faqs.forEachIndexed { index, (q, a) ->
                    val isExpanded = selectedFaqIndex == index
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainer)
                            .clickable { selectedFaqIndex = if (isExpanded) null else index }
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(q, color = OnSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Outline
                            )
                        }
                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(a, color = OnSurfaceVariant, fontSize = 11.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureCheck(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = OnSurface, fontSize = 12.sp)
    }
}
