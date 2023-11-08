package ie.setu.domain

import java.util.Date

data class User (
    var id: Int,
    var name: String,
    var email: String,
    var gender: String,
    var birthDate: Date,
    var mobileNumber: String,
    var dietPreferences: String,
    var height: Double,
    var weight: Int,
    var profession: String)
