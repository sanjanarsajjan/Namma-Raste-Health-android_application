package com.nammaraste.health

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminDashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "📊 Admin Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // KPI Row 1
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KPICard("Total Roads", "4", Color(0x2196F3), modifier = Modifier.weight(1f))
            KPICard("Critical", "1", Color(0xF44336), modifier = Modifier.weight(1f))
            KPICard("Today's Reports", "2", Color(0x4CAF50), modifier = Modifier.weight(1f))
        }

        // Reports Section
        Text(
            "Recent Reports",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        LazyColumn {
            items(3) { index ->
                ReportCard(
                    title = if (index == 0) "Pothole near junction" else if (index == 1) "Water clogged drain" else "Road crack",
                    severity = if (index == 0) 4 else if (index == 1) 3 else 2,
                    road = if (index == 0) "Hulimavu Road" else if (index == 1) "Gottigere Road" else "Meenakshi Mall Road",
                    timestamp = "2 hours ago"
                )
            }
        }
    }
}

@Composable
fun KPICard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ReportCard(title: String, severity: Int, road: String, timestamp: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Road: $road", fontSize = 12.sp, color = Color.Gray)
                    Text(timestamp, fontSize = 10.sp, color = Color.Gray)
                }
                Surface(
                    color = Color(0xFF9C27B0).copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        "Severity: $severity",
                        modifier = Modifier.padding(6.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9C27B0)
                    )
                }
            }
        }
    }
}