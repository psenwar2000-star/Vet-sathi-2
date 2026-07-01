package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnimalEntity
import com.example.data.model.BookingEntity
import com.example.ui.theme.VetSathiPrimary
import com.example.ui.theme.VetSathiSecondary
import com.example.ui.theme.VetSathiTertiary
import kotlinx.coroutines.delay

// Simulated Doctor Data on Map
data class MapDoctor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Float,
    val experience: String,
    val distanceKm: Double,
    val latitude: Float,
    val longitude: Float,
    val status: String, // "Available", "On Duty", "In Emergency"
    val avatarEmoji: String
)

@Composable
fun LiveVetMapComponent(
    activeBooking: BookingEntity?,
    doctorLatitude: Float,
    doctorLongitude: Float,
    doctorEtaMinutes: Int,
    animals: List<AnimalEntity>,
    onSelectDoctor: (String) -> Unit = {}
) {
    // Center point of Jodhpur, Rajasthan
    val farmerLat = 26.2389f
    val farmerLng = 73.0243f

    // Bounds of our local projected vector map
    val minLat = 26.2100f
    val maxLat = 26.2600f
    val minLng = 73.0050f
    val maxLng = 73.0450f

    // 5 Simulated active doctors around Jodhpur area
    val activeDoctors = remember {
        listOf(
            MapDoctor(
                id = "doc_1",
                name = "Dr. Rajesh Sharma",
                specialty = "Cattle & Livestock Specialist",
                rating = 4.9f,
                experience = "12 yrs",
                distanceKm = 1.4,
                latitude = 26.2510f,
                longitude = 73.0315f,
                status = "Emergency SOS",
                avatarEmoji = "👨‍⚕️"
            ),
            MapDoctor(
                id = "doc_2",
                name = "Dr. Alok Yadav",
                specialty = "Surgeon & Cow Insemination",
                rating = 4.8f,
                experience = "9 yrs",
                distanceKm = 2.1,
                latitude = 26.2210f,
                longitude = 73.0110f,
                status = "On Duty",
                avatarEmoji = "👨‍⚕️"
            ),
            MapDoctor(
                id = "doc_3",
                name = "Dr. Sunita Meena",
                specialty = "Canine & Small Pets Specialist",
                rating = 4.7f,
                experience = "7 yrs",
                distanceKm = 2.8,
                latitude = 26.2480f,
                longitude = 73.0380f,
                status = "Available",
                avatarEmoji = "👩‍⚕️"
            ),
            MapDoctor(
                id = "doc_4",
                name = "Dr. Anand Verma",
                specialty = "Equine & Dairy Consultant",
                rating = 4.9f,
                experience = "15 yrs",
                distanceKm = 3.2,
                latitude = 26.2310f,
                longitude = 73.0120f,
                status = "Available",
                avatarEmoji = "👨‍⚕️"
            ),
            MapDoctor(
                id = "doc_5",
                name = "Dr. Shreya Joshi",
                specialty = "Avian & Sheep Vaccinator",
                rating = 4.6f,
                experience = "5 yrs",
                distanceKm = 4.0,
                latitude = 26.2550f,
                longitude = 73.0200f,
                status = "On Duty",
                avatarEmoji = "👩‍⚕️"
            )
        )
    }

    // Selected doctor from the map pins
    var selectedDoctorOnMap by remember { mutableStateOf<MapDoctor?>(null) }
    
    // Pulse animation for radar effect
    var pulseRadius by remember { mutableStateOf(10f) }
    LaunchedEffect(Unit) {
        while (true) {
            for (r in 10..45) {
                pulseRadius = r.toFloat()
                delay(30)
            }
        }
    }

    // Determine if we show sick animal icon (based on active booking)
    val animalEmoji = remember(activeBooking, animals) {
        if (activeBooking != null) {
            val matchingAnimal = animals.find { it.id == activeBooking.animalId }
            val species = matchingAnimal?.species?.lowercase() ?: activeBooking.animalName.lowercase()
            when {
                species.contains("cow") || species.contains("गाय") -> "🐄"
                species.contains("buffalo") || species.contains("भैंस") -> "🦬"
                species.contains("dog") || species.contains("कुत्ता") -> "🐕"
                species.contains("goat") || species.contains("बकरी") -> "🐐"
                species.contains("sheep") || species.contains("भेड़") -> "🐑"
                species.contains("camel") || species.contains("ऊँट") -> "🐫"
                species.contains("horse") || species.contains("घोड़ा") -> "🐎"
                species.contains("cat") || species.contains("बिल्ली") -> "🐈"
                species.contains("pig") || species.contains("सूअर") -> "🐖"
                species.contains("rabbit") || species.contains("खरगोश") -> "🐇"
                species.contains("poultry") || species.contains("मुर्गी") -> "🐓"
                else -> "🐄" // Fallback default
            }
        } else {
            "🚜" // Standard Farmer location icon
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.5.dp, Color(0xFFC2D3C4), RoundedCornerShape(24.dp))
            .background(Color(0xFFE5ECE0))
    ) {
        var canvasSize by remember { mutableStateOf(Size.Zero) }

        // Touch Input Detector on Map Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(canvasSize) {
                    detectTapGestures { offset ->
                        if (canvasSize != Size.Zero) {
                            var clickedDoctor: MapDoctor? = null
                            // If active travel, we override doctor tap check for incoming doc to make sure user can track
                            for (doc in activeDoctors) {
                                // Project coordinates
                                val docX = ((doc.longitude - minLng) / (maxLng - minLng)) * canvasSize.width
                                val docY = canvasSize.height - (((doc.latitude - minLat) / (maxLat - minLat)) * canvasSize.height)
                                val distance = Math.hypot((offset.x - docX).toDouble(), (offset.y - docY).toDouble())
                                if (distance < 50.0) { // 50 pixels touch radius
                                    clickedDoctor = doc
                                    break
                                }
                            }
                            selectedDoctorOnMap = clickedDoctor
                        }
                    }
                }
        ) {
            // Draw vector high-fidelity offline map simulation
            Canvas(modifier = Modifier.fillMaxSize()) {
                canvasSize = size

                // 1. Draw pasture park areas (Background elements)
                drawRect(
                    color = Color(0xFFCFDAC8),
                    size = Size(size.width * 0.3f, size.height * 0.4f),
                    topLeft = Offset(size.width * 0.05f, size.height * 0.1f)
                )
                drawCircle(
                    color = Color(0xFFD3DEC9),
                    radius = size.width * 0.15f,
                    center = Offset(size.width * 0.8f, size.height * 0.75f)
                )

                // 2. Draw a gorgeous blue river flow across the Jodhpur suburbs
                val riverPath = Path().apply {
                    moveTo(0f, size.height * 0.5f)
                    cubicTo(
                        size.width * 0.3f, size.height * 0.45f,
                        size.width * 0.6f, size.height * 0.6f,
                        size.width, size.height * 0.55f
                    )
                }
                drawPath(
                    path = riverPath,
                    color = Color(0xFFA5C9EB),
                    style = Stroke(width = 16f)
                )

                // 3. Draw Major Road Networks (Grid styling)
                // Road 1: West-East Highway
                drawLine(
                    color = Color.White,
                    start = Offset(0f, size.height * 0.3f),
                    end = Offset(size.width, size.height * 0.35f),
                    strokeWidth = 24f
                )
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(0f, size.height * 0.3f),
                    end = Offset(size.width, size.height * 0.35f),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                )

                // Road 2: North-South Bypass
                drawLine(
                    color = Color.White,
                    start = Offset(size.width * 0.4f, 0f),
                    end = Offset(size.width * 0.45f, size.height),
                    strokeWidth = 24f
                )
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(size.width * 0.4f, 0f),
                    end = Offset(size.width * 0.45f, size.height),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                )

                // Road 3: Diagnostic Link Road to Farmer Location
                drawLine(
                    color = Color.White,
                    start = Offset(size.width * 0.1f, size.height * 0.8f),
                    end = Offset(size.width * 0.9f, size.height * 0.2f),
                    strokeWidth = 14f
                )

                // 4. Draw active booking travel route if on-trip
                if (activeBooking != null && activeBooking.status != "Completed" && activeBooking.status != "Cancelled") {
                    val farmX = ((farmerLng - minLng) / (maxLng - minLng)) * size.width
                    val farmY = size.height - (((farmerLat - minLat) / (maxLat - minLat)) * size.height)

                    val docX = ((doctorLongitude - minLng) / (maxLng - minLng)) * size.width
                    val docY = size.height - (((doctorLatitude - minLat) / (maxLat - minLat)) * size.height)

                    // Glow line for dispatch routing
                    drawLine(
                        color = Color(0xFFFF5252).copy(alpha = 0.5f),
                        start = Offset(docX, docY),
                        end = Offset(farmX, farmY),
                        strokeWidth = 8f
                    )
                    drawLine(
                        color = Color(0xFFBA1A1A),
                        start = Offset(docX, docY),
                        end = Offset(farmX, farmY),
                        strokeWidth = 3f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                    )
                }
            }

            // --- PLACED COMPOSABLES ON THE Simulated GPS Coordinates ---
            if (canvasSize != Size.Zero) {
                // 1. Draw Farmer's Farm Pin (Centered coordinates)
                val farmX = ((farmerLng - minLng) / (maxLng - minLng)) * canvasSize.width
                val farmY = canvasSize.height - (((farmerLat - minLat) / (maxLat - minLat)) * canvasSize.height)

                // Render Farmer Pin Composable
                Box(
                    modifier = Modifier
                        .offset(
                            x = with(LocalDensity.current) { (farmX).toDp() } - 25.dp,
                            y = with(LocalDensity.current) { (farmY).toDp() } - 50.dp
                        )
                        .size(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Pulsing Glow Radar
                    Box(
                        modifier = Modifier
                            .size(pulseRadius.dp)
                            .background(
                                color = if (activeBooking != null) Color(0xFFFF5252).copy(alpha = 0.25f) else Color(0xFF006B5E).copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                    )

                    // Pin layout
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (activeBooking != null) Color(0xFFFFECEB) else Color(0xFFE6F4EA)),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (activeBooking != null) Color.Red else Color(0xFF006B5E)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(animalEmoji, fontSize = 20.sp)
                            }
                        }
                        // Pin arrow
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Pin base",
                            tint = if (activeBooking != null) Color.Red else Color(0xFF006B5E),
                            modifier = Modifier
                                .size(20.dp)
                                .offset(y = (-4).dp)
                        )
                    }
                }

                // 2. Draw static available nearby active doctors (or moving doctor if active booking)
                activeDoctors.forEach { doc ->
                    // Determine doctor latitude/longitude
                    val isThisDocAssigned = activeBooking != null &&
                            (activeBooking.doctorName.contains(doc.name) ||
                             (activeBooking.serviceType == "Emergency SOS" && doc.id == "doc_1") ||
                             (activeBooking.serviceType != "Emergency SOS" && doc.id == "doc_2"))

                    val dLat = if (isThisDocAssigned) doctorLatitude else doc.latitude
                    val dLng = if (isThisDocAssigned) doctorLongitude else doc.longitude

                    val docX = ((dLng - minLng) / (maxLng - minLng)) * canvasSize.width
                    val docY = canvasSize.height - (((dLat - minLat) / (maxLat - minLat)) * canvasSize.height)

                    Box(
                        modifier = Modifier
                            .offset(
                                x = with(LocalDensity.current) { (docX).toDp() } - 20.dp,
                                y = with(LocalDensity.current) { (docY).toDp() } - 42.dp
                            )
                            .size(40.dp)
                            .clickable {
                                selectedDoctorOnMap = doc
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(
                                        color = if (isThisDocAssigned) Color(0xFFBA1A1A) else Color(0xFF006B5E),
                                        shape = CircleShape
                                    )
                                    .border(2.dp, Color.White, CircleShape)
                                    .shadow(elevation = 4.dp, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isThisDocAssigned) {
                                    Icon(
                                        imageVector = Icons.Filled.DirectionsCar,
                                        contentDescription = "Car Incoming",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                } else {
                                    Text(doc.avatarEmoji, fontSize = 13.sp)
                                }
                            }
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Pin base",
                                tint = if (isThisDocAssigned) Color(0xFFBA1A1A) else Color(0xFF006B5E),
                                modifier = Modifier
                                    .size(16.dp)
                                    .offset(y = (-5).dp)
                            )
                        }
                    }
                }
            }

            // 3. Floating Overlay Card - Map Controls & Mode info
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.75f)),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF34D399), CircleShape)
                    )
                    Text(
                        text = if (activeBooking != null) "MODE: LIVE TRACKING" else "GPS: 5 VETS ACTIVE IN AREA",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // 4. Details Bottom Sheet / Overlay Panel for Tapped Doctor
            AnimatedVisibility(
                visible = selectedDoctorOnMap != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                selectedDoctorOnMap?.let { doc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(VetSathiPrimary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(doc.avatarEmoji, fontSize = 24.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Specs
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = doc.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                when (doc.status) {
                                                    "Available" -> Color(0xFFD1FAE5)
                                                    "On Duty" -> Color(0xFFFEF3C7)
                                                    else -> Color(0xFFFFECEB)
                                                }
                                            )
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = doc.status,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (doc.status) {
                                                "Available" -> Color(0xFF065F46)
                                                "On Duty" -> Color(0xFFD97706)
                                                else -> Color(0xFFBA1A1A)
                                            }
                                        )
                                    }
                                }
                                Text(
                                    text = doc.specialty,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "★ ${doc.rating} (${doc.experience})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706)
                                    )
                                    Text(
                                        text = "📍 ${doc.distanceKm} km away",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Book / Close Action
                            Column(horizontalAlignment = Alignment.End) {
                                IconButton(
                                    onClick = { selectedDoctorOnMap = null },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Filled.Close, "Close Dialog", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(
                                    onClick = {
                                        onSelectDoctor(doc.name)
                                        selectedDoctorOnMap = null
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text("Book", color = Color.White, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
