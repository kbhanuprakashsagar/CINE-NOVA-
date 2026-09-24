package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.LicenseContract
import com.example.data.repository.MovieRepository
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Outline
import com.example.ui.theme.PrimaryLight
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest

@Composable
fun StudioOpsScreen(
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Telemetry") } // "Telemetry" or "Licensing"
    var selectedRange by remember { mutableStateOf("Last 30 Days") }

    val ranges = listOf("Last 30 Days", "Daily", "Weekly", "Monthly", "Yearly")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceContainerLowest)
            .testTag("studio_ops_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Bar & Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "OPERATIONAL TELEMETRY • AP-SOUTH-1",
                                color = PrimaryLight,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "Good morning, Vikram",
                            color = OnSurface,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF00363D))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "PRODUCTION 99.98%",
                        color = CyanTertiary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        // Subtitle & Tab Switcher (Telemetry vs Rights & Licensing)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceContainerLow)
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (selectedTab == "Telemetry") CrimsonPrimary else Color.Transparent)
                        .clickable { selectedTab = "Telemetry" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Overview & Telemetry",
                        color = if (selectedTab == "Telemetry") OnPrimaryContainer else Outline,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (selectedTab == "Licensing") CrimsonPrimary else Color.Transparent)
                        .clickable { selectedTab = "Licensing" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Rights & Licenses (3 Expiring)",
                        color = if (selectedTab == "Licensing") OnPrimaryContainer else Outline,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (selectedTab == "Telemetry") {
            // Range Selector
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ranges.forEach { range ->
                        val isSelected = selectedRange == range
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) SurfaceContainerHigh else SurfaceContainerLow)
                                .clickable { selectedRange = range }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = range,
                                color = if (isSelected) CyanTertiary else Outline,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Key Metrics Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "TOTAL AUDIENCE BASE",
                            value = "3,842,190",
                            delta = "+14.2% vs last month",
                            color = CyanTertiary,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "ACTIVE PAID SUBS",
                            value = "1,428,500",
                            delta = "+8.7% • Churn 1.8%",
                            color = PrimaryLight,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "LICENSED CATALOG",
                            value = "1,840 Titles",
                            delta = "48 in QA • 12 Expiring",
                            color = GoldSecondary,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "MONTHLY REVENUE",
                            value = "₹48.6 Cr",
                            delta = "~$5.8M USD • ARPU ₹340",
                            color = CrimsonPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Rights Expirations Alert Panel
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Rights Expirations", color = OnSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(ErrorContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("ACTION REQ.", color = ErrorRed, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    // Expiring item 1
                    ExpiringLicenseRow(
                        days = "8 DAYS LEFT (DEC 2)",
                        title = "All We Imagine as Light",
                        studio = "Cannes Grand Prix • Chalk & Cheese Films",
                        tag = "Auto-Unpublish ON"
                    )

                    // Expiring item 2
                    ExpiringLicenseRow(
                        days = "14 DAYS LEFT (NOV 28)",
                        title = "Iratta",
                        studio = "Investigative Noir • Appu Pathu Pappu Prod.",
                        tag = "Malayalam OTT"
                    )

                    // Expiring item 3
                    ExpiringLicenseRow(
                        days = "27 DAYS LEFT (DEC 11)",
                        title = "Garudan",
                        studio = "Legal Thriller • Magic Frames Studio",
                        tag = "Tamil Exclusive"
                    )
                }
            }

            // Top Streaming Cinema Assets Table
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Top Streaming Cinema Assets", color = OnSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("LIVE TELEMETRY", color = CyanTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    MovieRepository.topStreamingAssets.forEach { asset ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(if (asset.rank == 1) CrimsonPrimary else SurfaceContainerHighest),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${asset.rank}", color = OnPrimaryContainer, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text(asset.title, color = OnSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("${asset.language} • ${asset.studio}", color = Outline, fontSize = 10.sp)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("${asset.streams} (${asset.completion}%)", color = CyanTertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(asset.qualityTier, color = GoldSecondary, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }

            // CDN Edge Mesh Health
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("CDN Edge Mesh Health", color = OnSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("● 99.98%", color = CyanTertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    listOf(
                        "AWS Mumbai (BOM-1)" to "142 Gbps",
                        "Cloudflare Chennai Edge" to "88 Gbps",
                        "Singtel SG Gateway" to "64 Gbps",
                        "London EU Hub (Diaspora)" to "38 Gbps"
                    ).forEach { (node, speed) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainer)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(node, color = OnSurface, fontSize = 11.sp)
                            Text(speed, color = CyanTertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Licensing Tab Content
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Digital Rights & Licenses",
                        color = OnSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Track territorial broadcasting rights, streaming windows, SVOD/AVOD terms, and DRM expiration contracts across regional Indian circuits.",
                        color = Outline,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    MovieRepository.licenseContracts.forEach { contract ->
                        LicenseContractCard(contract)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(title: String, value: String, delta: String, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerLow)
            .padding(12.dp)
    ) {
        Text(title, color = Outline, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = OnSurface, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(delta, color = color, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ExpiringLicenseRow(days: String, title: String, studio: String, tag: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceContainer)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(days, color = GoldSecondary, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
            Text(title, color = OnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(studio, color = Outline, fontSize = 10.sp)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(SurfaceContainerHigh)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(tag, color = CyanTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LicenseContractCard(contract: LicenseContract) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainer)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(contract.title, color = OnSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (contract.isExpiringCritical) GoldSecondary.copy(alpha = 0.2f) else CyanTertiary.copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = contract.status,
                    color = if (contract.isExpiringCritical) GoldSecondary else CyanTertiary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text("${contract.rightsOwner} • ${contract.studioSubsidiary}", color = Outline, fontSize = 11.sp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Territory: ${contract.territory}", color = OnSurfaceVariant, fontSize = 11.sp)
            Text(contract.licenseModel, color = CyanTertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Text("Window: ${contract.windowDates}", color = Outline, fontSize = 10.sp)
    }
}
