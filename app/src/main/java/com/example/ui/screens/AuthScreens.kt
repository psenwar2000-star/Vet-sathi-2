package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VetSathiPrimary
import com.example.ui.theme.VetSathiSecondary
import com.example.ui.theme.VetSathiTertiary
import com.example.ui.viewmodel.UserRole
import com.example.ui.viewmodel.VetSathiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(viewModel: VetSathiViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F9D58).copy(alpha = 0.1f),
                        Color(0xFFF7F9FA)
                    )
                )
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Premium Logo Sparkle Area
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
                    .border(2.dp, VetSathiPrimary, RoundedCornerShape(32.dp))
            ) {
                Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = "VetSathi Logo",
                    tint = VetSathiPrimary,
                    modifier = Modifier.size(54.dp)
                )
                // Sparkle
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = "AI Sparkle",
                        tint = VetSathiSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "VetSathi",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00201C),
                letterSpacing = (-1).sp
            )

            Text(
                text = "India's Largest AI-Powered Veterinary Platform",
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "WHO ARE YOU?",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VetSathiPrimary,
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Role Buttons
            RoleCard(
                title = "Animal / Pet Owner",
                subtitle = "Book emergency vets, insemination, or scan health records",
                icon = Icons.Filled.Agriculture,
                color = VetSathiPrimary,
                onClick = { viewModel.selectRole(UserRole.OWNER) }
            )

            RoleCard(
                title = "Veterinary Doctor",
                subtitle = "Receive nearby livestock/pet consult calls & log prescriptions",
                icon = Icons.Filled.MedicalServices,
                color = VetSathiSecondary,
                onClick = { viewModel.selectRole(UserRole.DOCTOR) }
            )

            RoleCard(
                title = "System Administrator",
                subtitle = "Approve vet licenses, manage payouts, dispatch SOS assistance",
                icon = Icons.Filled.AdminPanelSettings,
                color = Color(0xFF1E293B),
                onClick = { viewModel.selectRole(UserRole.ADMIN) }
            )

            RoleCard(
                title = "Pharmacy & Chemist",
                subtitle = "Deliver veterinary medicines & track regional inventory",
                icon = Icons.Filled.LocalPharmacy,
                color = VetSathiTertiary,
                onClick = { viewModel.selectRole(UserRole.PHARMACY) }
            )

            RoleCard(
                title = "Diagnostic Laboratory",
                subtitle = "Manage animal blood tests and generate pathology reports",
                icon = Icons.Filled.Science,
                color = Color(0xFF8E44AD),
                onClick = { viewModel.selectRole(UserRole.LABORATORY) }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    lineHeight = 14.sp
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Select",
                tint = Color.LightGray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(viewModel: VetSathiViewModel) {
    val phone by viewModel.phoneInput.collectAsState()
    val otp by viewModel.otpInput.collectAsState()
    val isOtpSent by viewModel.isOtpSent.collectAsState()
    val selectedRole by viewModel.currentRole.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FA))
    ) {
        // Upper decorative backdrop
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            VetSathiPrimary,
                            VetSathiPrimary.copy(alpha = 0.0f)
                        )
                    )
                )
        )

        // Back Button
        IconButton(
            onClick = { viewModel.logOut() },
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .align(Alignment.TopStart)
                .background(Color.White, RoundedCornerShape(12.dp))
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "VetSathi Mobile Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Securing login for account role: ${selectedRole.name}",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (!isOtpSent) "Enter Phone Number" else "Verify One-Time OTP",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )

                    Text(
                        text = if (!isOtpSent) 
                            "We'll send an OTP to verify your identity instantly" 
                        else 
                            "Enter the 4-digit code sent to your phone (Default: 1234)",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    if (!isOtpSent) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { if (it.length <= 10) viewModel.setPhoneInput(it) },
                            label = { Text("Mobile Number") },
                            placeholder = { Text("e.g. 9876543210") },
                            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Phone") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("phone_input")
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.sendOtp() },
                            enabled = phone.length >= 10,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("send_otp_button")
                        ) {
                            Text("Request Secure OTP", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    } else {
                        OutlinedTextField(
                            value = otp,
                            onValueChange = { if (it.length <= 4) viewModel.setOtpInput(it) },
                            label = { Text("OTP Code") },
                            placeholder = { Text("1234") },
                            leadingIcon = { Icon(Icons.Filled.LockOpen, contentDescription = "Lock") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("otp_input")
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.verifyOtp() },
                            enabled = otp.length >= 4,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VetSathiSecondary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("verify_otp_button")
                        ) {
                            Text("Verify & Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }

                        TextButton(
                            onClick = { viewModel.setOtpInput("") },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Resend SMS OTP", color = VetSathiPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Sign-In Row
            Text(text = "Or register instantly with", fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialLoginButton(icon = Icons.Filled.Add, label = "Google") {
                    viewModel.setPhoneInput("9999988888")
                    viewModel.sendOtp()
                }
                SocialLoginButton(icon = Icons.Filled.AccountCircle, label = "Apple") {
                    viewModel.setPhoneInput("8888877777")
                    viewModel.sendOtp()
                }
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = Modifier.testTag("social_btn_$label")
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.Black, fontSize = 13.sp)
    }
}
