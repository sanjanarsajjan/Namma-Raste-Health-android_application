package com.nammaraste.health

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaRasteApp()
        }
    }
}

@Composable
fun NammaRasteApp() {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedRoadId by remember { mutableStateOf("") }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    currentScreen = "detail"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Report"
                )
            }
        },

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = currentScreen == "home",
                    onClick = {
                        currentScreen = "home"
                    },
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    },
                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(
                    selected = currentScreen == "admin",
                    onClick = {
                        currentScreen = "admin"
                    },
                    icon = {
                        Icon(Icons.Default.Menu, contentDescription = "Dashboard")
                    },
                    label = {
                        Text("Dashboard")
                    }
                )
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            when (currentScreen) {

                "home" -> HomeScreen(
                    onRoadClicked = {
                        selectedRoadId = it
                        currentScreen = "detail"
                    }
                )

                "detail" -> DetailScreen(selectedRoadId) {
                    currentScreen = "home"
                }

                "admin" -> AdminDashboardScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(onRoadClicked: (String) -> Unit, viewModel: RoadViewModel = viewModel()) {
    val roads by viewModel.roads.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Surface(
            color = Color(0x02C39A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Namma-Raste Health",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Road Maintenance Tracker",
                    fontSize = 12.sp,
                    color = Color.White
                )

            }
        }

        // Search
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.searchRoads(it)
            },
            label = { Text("Search road...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        // Road List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            items(roads) { road ->
                RoadCard(road) {
                    onRoadClicked(road.roadId)
                }
            }
        }
    }
}

@Composable
fun RoadCard(road: RoadEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                road.roadName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "${road.taluka} | ${road.lengthKm} km",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Contractor: ${road.contractorName}",
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f)
                )
                HealthBadge(road.currentHealthScore)
            }
        }
    }
}

@Composable
fun HealthBadge(score: Int) {
    val (color, label) = when {
        score >= 70 -> Pair(Color(0x4CAF50), "Good")
        score >= 40 -> Pair(Color(0xF9A825), "Moderate")
        else -> Pair(Color(0xF44336), "Critical")
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        modifier = Modifier.padding(4.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            "$label ($score)",
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(6.dp)
        )
    }
}

@Composable
fun DetailScreen(roadId: String, onBack: () -> Unit) {
    var damageType by remember { mutableStateOf("pothole") }
    var severity by remember { mutableStateOf(3) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("← Back")
        }

        Text(
            "Report Damage for Road: $roadId",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Damage Type
        Text(
            "Damage Type:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        listOf("pothole", "crack", "waterlog", "drain_block").forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { damageType = type }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = damageType == type,
                    onClick = { damageType = type }
                )
                Text(type, modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Severity
        Text(
            "Severity: $severity / 5",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Slider(
            value = severity.toFloat(),
            onValueChange = { severity = it.toInt() },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.fillMaxWidth()
        )

        // Submit Button
        Button(
            onClick = {
                // Submit report
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Submit Report")
        }

        // Admin Dashboard Button
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Admin Dashboard")
        }
    }
}

class RoadViewModel : ViewModel() {
    private val _roads = MutableStateFlow<List<RoadEntity>>(emptyList())
    val roads = _roads.asStateFlow()

    private val allRoads = listOf(
        RoadEntity("1", "Hulimavu Road", "Devanahalli", 5.2, "Tech Builders Ltd", 85),
        RoadEntity("2", "Meenakshi Mall Road", "Devanahalli", 3.8, "Skyway Constructions", 72),
        RoadEntity("3", "Gottigere Road", "Nelamangala", 4.5, "Quality Builders", 45),
        RoadEntity("4", "Nelmangala Main Road", "Nelamangala", 6.2, "Roads & Bridges Inc", 35),
    )

    init {
        _roads.value = allRoads
    }

    fun searchRoads(query: String) {
        _roads.value = if (query.isEmpty()) {
            allRoads
        } else {
            allRoads.filter { it.roadName.contains(query, ignoreCase = true) }
        }
    }
}