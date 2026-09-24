package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Outline
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerLow

@Composable
fun CastDialog(
    onDismiss: () -> Unit
) {
    var selectedDevice by remember { mutableStateOf<String?>(null) }

    val devices = listOf(
        "Living Room OLED 4K (Dolby Vision)" to "Chromecast Ultra • 5 GHz Wi-Fi",
        "Home Theater Projector" to "Apple TV 4K • 5.1 Atmos Surround",
        "Bedroom Android TV" to "Sony Bravia • Ready for cast"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceContainerLow,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cast,
                    contentDescription = null,
                    tint = CyanTertiary
                )
                Text(
                    text = "Cast to Screen",
                    color = OnSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Stream uncompressed 4K DCI master print directly to your theater display with spatial Dolby Atmos passthrough.",
                    color = OnSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                devices.forEach { (name, spec) ->
                    val isConnected = selectedDevice == name
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainer)
                            .clickable { selectedDevice = name }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tv,
                                contentDescription = null,
                                tint = if (isConnected) CyanTertiary else Outline,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = name,
                                    color = OnSurface,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = spec,
                                    color = Outline,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (isConnected) {
                            Text(
                                text = "Connected",
                                color = GoldSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = CyanTertiary, fontWeight = FontWeight.Bold)
            }
        }
    )
}
