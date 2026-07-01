package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VetSathiPrimary
import com.example.ui.theme.VetSathiSecondary
import com.example.ui.theme.VetSathiTertiary
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UiState
import com.example.ui.viewmodel.VetSathiViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(viewModel: VetSathiViewModel) {
    val aiResult by viewModel.aiResult.collectAsState()
    val aiInputText by viewModel.aiInputText.collectAsState()
    val scope = rememberCoroutineScope()

    var userSymptomText by remember { mutableStateOf("") }
    var selectedMockPic by remember { mutableStateOf<String?>(null) } // "cow_lumps", "dog_scratch", "buffalo_bloat"
    
    // Chat states
    val chatHistory = remember {
        mutableStateListOf(
            Pair("assistant", "Namaste! I am VetSathi AI. You can describe your animal's health symptoms in Hindi, English, or regional languages. I can diagnose bovine bloat, lumpy skin disease, mastitis, or pet parvovirus. How can I assist you today?")
        )
    }
    var currentChatMsg by remember { mutableStateOf("") }
    var isChatLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AutoAwesome, "Sparkle", tint = VetSathiPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VetSathi Diagnostic AI Portal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.OWNER_HOME) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAF9))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Card with Sparkles
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "CHOOSE RAPID DIAGNOSTIC MODULE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VetSathiPrimary,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "Describe your livestock/pet symptoms below or choose from popular physical symptoms to run the Gemini AI analysis instantly.",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }

            // Input Symptom Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = userSymptomText,
                        onValueChange = { userSymptomText = it },
                        label = { Text("Describe Symptoms in English / Hindi / Regional") },
                        placeholder = { Text("e.g. My cow Ganga has small lumps/pox on her neck and has had a fever for 3 days.") },
                        minLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("symptom_textbox")
                    )

                    // Mock physical snaps
                    Text(
                        text = "TAP TO SIMULATE CAMERA SCAN / IMAGE CAPTURE:",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MockImageSelectorCard(
                            label = "🐄 Cow Skin Lumps",
                            emoji = "🐄",
                            isSelected = selectedMockPic == "cow_lumps",
                            onClick = {
                                selectedMockPic = "cow_lumps"
                                userSymptomText = "My cow Ganga has developed circular lumps on her neck, high fever of 104°F, and a drop in milk production."
                            },
                            modifier = Modifier.weight(1f)
                        )

                        MockImageSelectorCard(
                            label = "🐕 Dog Scratch/Fever",
                            emoji = "🐕",
                            isSelected = selectedMockPic == "dog_scratch",
                            onClick = {
                                selectedMockPic = "dog_scratch"
                                userSymptomText = "Rocky has been vomiting, is lethargic, and refusing food. Refusing to play."
                            },
                            modifier = Modifier.weight(1f)
                        )

                        MockImageSelectorCard(
                            label = "🐃 Buffalo Bloat",
                            emoji = "🐃",
                            isSelected = selectedMockPic == "buffalo_bloat",
                            onClick = {
                                selectedMockPic = "buffalo_bloat"
                                userSymptomText = "Buffalo stomach is swollen, filled with gas, and is finding it extremely difficult to breathe."
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                userSymptomText = ""
                                selectedMockPic = null
                                viewModel.clearAiAnalysis()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Clear", color = Color.Black)
                        }

                        Button(
                            onClick = {
                                if (userSymptomText.isNotEmpty()) {
                                    viewModel.runAiAnalysis(userSymptomText, null)
                                }
                            },
                            enabled = userSymptomText.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(50.dp)
                                .testTag("run_analysis_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Filled.AutoAwesome, "AI Icon", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Analyze with AI", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // AI results display
            AnimatedVisibility(visible = aiResult != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1FDF9)),
                    border = BorderStroke(1.2.dp, VetSathiPrimary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Recommend, "Analysis Done", tint = VetSathiPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("VetSathi Diagnostics Result", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F2D1F))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        when (val state = aiResult) {
                            is UiState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator(color = VetSathiPrimary)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text("Consulting Gemini AI experts...", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                            }
                            is UiState.Success -> {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Response content
                                    Text(
                                        text = state.data,
                                        fontSize = 13.sp,
                                        color = Color.DarkGray,
                                        lineHeight = 18.sp
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Action to Book Vet immediately
                                    Text(
                                        text = "Need physical/expert verification?",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )

                                    Button(
                                        onClick = {
                                            viewModel.setServiceType("Emergency SOS")
                                            viewModel.createBooking("VetSathi AI diagnostic matching: $userSymptomText")
                                            viewModel.navigateTo(Screen.BOOKING_FLOW)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = VetSathiTertiary),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Book Verified Doctor Immediately", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            is UiState.Error -> {
                                Text("Error running diagnostic: ${state.message}", color = Color.Red, fontSize = 13.sp)
                            }
                            else -> {}
                        }
                    }
                }
            }

            // Veterinary AI chatbot segment
            Text(
                text = "VETSATHI CHATBOT COMPANION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 1.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chatHistory.forEach { msg ->
                            val isUser = msg.first == "user"
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Card(
                                    modifier = Modifier
                                        .align(if (isUser) Alignment.CenterEnd else Alignment.CenterStart)
                                        .fillMaxWidth(0.85f),
                                    shape = RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isUser) 16.dp else 0.dp,
                                        bottomEnd = if (isUser) 0.dp else 16.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isUser) Color(0xFFECFDF5) else Color(0xFFF1F5F9)
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isUser) VetSathiPrimary.copy(alpha = 0.2f) else Color(0xFFE2E8F0)
                                    )
                                ) {
                                    Text(
                                        text = msg.second,
                                        fontSize = 11.2.sp,
                                        lineHeight = 15.sp,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }

                        if (isChatLoading) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .align(Alignment.CenterStart),
                                    strokeWidth = 2.dp,
                                    color = VetSathiPrimary
                                )
                            }
                        }
                    }

                    Divider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = currentChatMsg,
                            onValueChange = { currentChatMsg = it },
                            placeholder = { Text("Ask VetSathi AI: 'cow fever treatment'...", fontSize = 11.sp) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                if (currentChatMsg.isNotEmpty()) {
                                    val userText = currentChatMsg
                                    chatHistory.add(Pair("user", userText))
                                    currentChatMsg = ""
                                    isChatLoading = true

                                    scope.launch {
                                        delay(1500)
                                        val reply = when {
                                            userText.lowercase().contains("fever") -> "For a cow running a high fever, isolation is critical. Ensure fresh water is always available. You can safely administer oral Paracetamol or Meloxicam (consult registered VetSathi doctor via 'Video Consultation' for dose confirmation depending on body weight)."
                                            userText.lowercase().contains("bloat") -> "Acute bloat (swollen stomach) can be fatal. Keep the animal standing, insert a wooden bit in the mouth, and administer 100ml anti-bloating suspension (Tympol/Anisap) or liquid paraffin. If breathing is shallow, press Emergency SOS immediately."
                                            else -> "Thank you for sharing. I recommend capturing a physical symptom picture and submitting it via our VetAI Scanner module at the top of this portal. This helps Gemini AI parse the visual symptoms accurately."
                                        }
                                        chatHistory.add(Pair("assistant", reply))
                                        isChatLoading = false
                                    }
                                }
                            },
                            modifier = Modifier.background(VetSathiPrimary, CircleShape)
                        ) {
                            Icon(Icons.Filled.Send, "Send", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MockImageSelectorCard(
    label: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(72.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFECFDF5) else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 1.6.dp else 1.dp,
            color = if (isSelected) VetSathiPrimary else Color(0xFFE2E8F0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 10.sp,
                color = if (isSelected) VetSathiPrimary else Color.DarkGray
            )
        }
    }
}
