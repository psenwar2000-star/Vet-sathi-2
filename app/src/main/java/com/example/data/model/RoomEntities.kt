package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "animals")
data class AnimalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val species: String, // Cattle, Dog, Cat, Goat, Buffalo
    val breed: String,
    val age: String,
    val weight: String,
    val gender: String,
    val tagNumber: String,
    val milkProduction: String = "0L/day", // For livestock
    val pregnancyStatus: String = "Not Pregnant",
    val vaccinationHistory: String = "None",
    val medicalHistory: String = "Healthy",
    val photoUri: String? = null
) : Serializable

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animalId: Long,
    val animalName: String,
    val serviceType: String, // Home Visit, Video Consult, Lab Test, Insemination, Vaccination, Pregnancy Diagnosis
    val doctorName: String,
    val description: String = "",
    val voiceNotePath: String? = null,
    val imagePath: String? = null,
    val videoPath: String? = null,
    val location: String = "Default Farmer Location, India",
    val date: String,
    val time: String,
    val status: String = "Pending", // Pending, Accepted, Active, Completed, Cancelled
    val otp: String = "1234",
    val rating: Float = 0.0f,
    val prescriptionDraft: String? = null,
    val prescriptionSigned: Boolean = false,
    val signatureData: String? = null, // Path or drawn points base64
    val invoiceAmount: Double = 350.0,
    val isPaid: Boolean = false,
    val paymentMethod: String = "Cash" // Wallet, UPI, Stripe, Razorpay, Cash
) : Serializable

@Entity(tableName = "health_records")
data class HealthRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animalName: String,
    val diagnosis: String,
    val treatment: String,
    val date: String,
    val vetName: String,
    val prescription: String
) : Serializable

@Entity(tableName = "schemes")
data class SchemeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val benefit: String,
    val state: String = "National"
) : Serializable
