package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AnimalEntity
import com.example.data.model.BookingEntity
import com.example.data.model.HealthRecordEntity
import com.example.data.model.SchemeEntity
import com.example.data.repository.VetSathiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

enum class UserRole {
    OWNER, DOCTOR, ADMIN, PHARMACY, LABORATORY
}

enum class Screen {
    AUTH, ROLE_SELECTION, OWNER_HOME, BOOKING_FLOW, DOCTOR_DASHBOARD, ADMIN_DASHBOARD, PHARMACY_DASHBOARD, LAB_DASHBOARD, AI_ASSISTANT
}

class VetSathiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VetSathiRepository(application)

    // Current app routing and role state
    private val _currentScreen = MutableStateFlow(Screen.ROLE_SELECTION)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.OWNER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _phoneInput = MutableStateFlow("")
    val phoneInput: StateFlow<String> = _phoneInput.asStateFlow()

    private val _otpInput = MutableStateFlow("")
    val otpInput: StateFlow<String> = _otpInput.asStateFlow()

    private val _isOtpSent = MutableStateFlow(false)
    val isOtpSent: StateFlow<Boolean> = _isOtpSent.asStateFlow()

    // Database Observables
    val animals: StateFlow<List<AnimalEntity>> = repository.getAnimals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookings: StateFlow<List<BookingEntity>> = repository.getBookings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val healthRecords: StateFlow<List<HealthRecordEntity>> = repository.getHealthRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val governmentSchemes: StateFlow<List<SchemeEntity>> = repository.getGovernmentSchemes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Interactive Booking state
    private val _selectedAnimal = MutableStateFlow<AnimalEntity?>(null)
    val selectedAnimal: StateFlow<AnimalEntity?> = _selectedAnimal.asStateFlow()

    private val _selectedServiceType = MutableStateFlow("Emergency SOS")
    val selectedServiceType: StateFlow<String> = _selectedServiceType.asStateFlow()

    private val _activeBooking = MutableStateFlow<BookingEntity?>(null)
    val activeBooking: StateFlow<BookingEntity?> = _activeBooking.asStateFlow()

    // GPS & Live Tracking Simulation
    private val _doctorLatitude = MutableStateFlow(26.2389f) // Jodhpur coordinates
    val doctorLatitude: StateFlow<Float> = _doctorLatitude.asStateFlow()

    private val _doctorLongitude = MutableStateFlow(73.0243f)
    val doctorLongitude: StateFlow<Float> = _doctorLongitude.asStateFlow()

    private val _doctorEtaMinutes = MutableStateFlow(12)
    val doctorEtaMinutes: StateFlow<Int> = _doctorEtaMinutes.asStateFlow()

    // AI States
    private val _aiResult = MutableStateFlow<UiState<String>?>(null)
    val aiResult: StateFlow<UiState<String>?> = _aiResult.asStateFlow()

    private val _aiInputText = MutableStateFlow("")
    val aiInputText: StateFlow<String> = _aiInputText.asStateFlow()

    // Doctor profile states
    private val _isDoctorOnline = MutableStateFlow(true)
    val isDoctorOnline: StateFlow<Boolean> = _isDoctorOnline.asStateFlow()

    // Local Wallet
    private val _walletBalance = MutableStateFlow(1240.0)
    val walletBalance: StateFlow<Double> = _walletBalance.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedMockDataIfNeeded()
            // Set first animal as default if available
            animals.collect { list ->
                if (list.isNotEmpty() && _selectedAnimal.value == null) {
                    _selectedAnimal.value = list.first()
                }
            }
        }
    }

    // Auth actions
    fun setPhoneInput(phone: String) { _phoneInput.value = phone }
    fun setOtpInput(otp: String) { _otpInput.value = otp }

    fun sendOtp() {
        if (_phoneInput.value.length >= 10) {
            _isOtpSent.value = true
        }
    }

    fun verifyOtp() {
        if (_otpInput.value == "1234" || _otpInput.value.length == 4) {
            _isLoggedIn.value = true
            _currentScreen.value = when (_currentRole.value) {
                UserRole.OWNER -> Screen.OWNER_HOME
                UserRole.DOCTOR -> Screen.DOCTOR_DASHBOARD
                UserRole.ADMIN -> Screen.ADMIN_DASHBOARD
                UserRole.PHARMACY -> Screen.PHARMACY_DASHBOARD
                UserRole.LABORATORY -> Screen.LAB_DASHBOARD
            }
        }
    }

    fun selectRole(role: UserRole) {
        _currentRole.value = role
        _currentScreen.value = Screen.AUTH
    }

    fun logOut() {
        _isLoggedIn.value = false
        _isOtpSent.value = false
        _phoneInput.value = ""
        _otpInput.value = ""
        _currentScreen.value = Screen.ROLE_SELECTION
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectAnimal(animal: AnimalEntity) {
        _selectedAnimal.value = animal
    }

    fun setServiceType(serviceType: String) {
        _selectedServiceType.value = serviceType
    }

    // Add Animal Profile
    fun addAnimal(name: String, species: String, breed: String, age: String, weight: String, gender: String, tagNumber: String, milk: String, pregnancy: String) {
        viewModelScope.launch {
            val animal = AnimalEntity(
                name = name,
                species = species,
                breed = breed,
                age = age,
                weight = weight,
                gender = gender,
                tagNumber = tagNumber,
                milkProduction = milk,
                pregnancyStatus = pregnancy
            )
            repository.insertAnimal(animal)
        }
    }

    // Trigger Smart Booking System
    fun createBooking(
        description: String,
        voiceNotePath: String? = null,
        imagePath: String? = null,
        paymentMethod: String = "Wallet"
    ) {
        viewModelScope.launch {
            val animal = _selectedAnimal.value
            val isSOS = _selectedServiceType.value == "Emergency SOS"
            val cost = if (isSOS) 650.0 else 350.0
            
            val booking = BookingEntity(
                animalId = animal?.id ?: 0,
                animalName = animal?.name ?: "Unknown Animal",
                serviceType = _selectedServiceType.value,
                doctorName = if (isSOS) "Dr. Rajesh Sharma (SOS Specialist)" else "Dr. Alok Yadav",
                description = description,
                voiceNotePath = voiceNotePath,
                imagePath = imagePath,
                location = "Jodhpur, Rajasthan, India (GPS: 26.2389, 73.0243)",
                date = "Today",
                time = "Just now",
                status = "Pending",
                invoiceAmount = cost,
                paymentMethod = paymentMethod
            )
            val id = repository.insertBooking(booking)
            val completeBooking = booking.copy(id = id)
            _activeBooking.value = completeBooking

            // Simulate Smart Doctor Matching Process
            simulateDoctorResponse(completeBooking)
        }
    }

    private fun simulateDoctorResponse(booking: BookingEntity) {
        viewModelScope.launch {
            delay(3000) // Doctor is matching...
            val accepted = booking.copy(status = "Accepted")
            repository.updateBooking(accepted)
            _activeBooking.value = accepted

            // Update local state to trigger Google Map simulation
            _doctorLatitude.value = 26.2500f
            _doctorLongitude.value = 73.0300f
            _doctorEtaMinutes.value = 15

            // Simulated step-by-step movement of the Doctor's vehicle
            for (i in 1..5) {
                delay(4000)
                _doctorLatitude.value = 26.2389f + (0.0111f * (5 - i) / 5f)
                _doctorLongitude.value = 73.0243f + (0.0057f * (5 - i) / 5f)
                _doctorEtaMinutes.value = 15 - (i * 3)
            }

            delay(2000)
            val active = accepted.copy(status = "Active", otp = "4832")
            repository.updateBooking(active)
            _activeBooking.value = active
        }
    }

    // Completes active visit & issues digital invoice and AI prescription draft
    fun completeActiveVisit(otp: String) {
        viewModelScope.launch {
            val current = _activeBooking.value ?: return@launch
            if (otp == current.otp || otp == "1234") {
                val completeDraft = "RECOMMENDED TREATMENT PLAN:\n" +
                        "1. Injection Melonex 5ml deep IM once daily for 3 days.\n" +
                        "2. Capsule Rumen FS 2 capsules twice daily for rumen stabilization.\n" +
                        "3. Complete rest in dry, aerated stall. Avoid cold feed."

                val paid = current.paymentMethod == "Wallet"
                if (paid) {
                    _walletBalance.value = (_walletBalance.value - current.invoiceAmount).coerceAtLeast(0.0)
                }

                val completed = current.copy(
                    status = "Completed",
                    isPaid = true,
                    prescriptionDraft = completeDraft,
                    prescriptionSigned = true,
                    signatureData = "DR_VERIFIED_SECURE_TOKEN_2026"
                )
                repository.updateBooking(completed)
                _activeBooking.value = completed

                // Save to general health records of the animal
                repository.insertHealthRecord(
                    HealthRecordEntity(
                        animalName = completed.animalName,
                        diagnosis = "Diagnostic scan completed for ${completed.serviceType}",
                        treatment = "Completed by ${completed.doctorName}",
                        date = "Today",
                        vetName = completed.doctorName,
                        prescription = completeDraft
                    )
                )
            }
        }
    }

    fun addRatingToBooking(booking: BookingEntity, rating: Float) {
        viewModelScope.launch {
            val updated = booking.copy(rating = rating)
            repository.updateBooking(updated)
            if (_activeBooking.value?.id == booking.id) {
                _activeBooking.value = updated
            }
        }
    }

    // Core AI Disease & Breed Detection
    fun runAiAnalysis(symptomText: String, bitmap: Bitmap?) {
        _aiInputText.value = symptomText
        _aiResult.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.analyzeSymptomImage(symptomText, bitmap)
            _aiResult.value = UiState.Success(result)
        }
    }

    fun clearAiAnalysis() {
        _aiResult.value = null
        _aiInputText.value = ""
    }

    // Doctor dashboard actions
    fun setDoctorOnline(online: Boolean) {
        _isDoctorOnline.value = online
    }

    fun acceptDoctorBooking(bookingId: Long) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, "Accepted")
        }
    }

    fun addFundsToWallet(amount: Double) {
        _walletBalance.value += amount
    }
}
