package ie.setu.domain

import org.joda.time.DateTime
import java.util.Date

data class User (
    var id: Int,
    var name: String,
    var email: String,
    var gender: String,
    var birthDate: DateTime,
    var mobileNumber: String,
    var dietPreferences: String,
    var height: Double,
    var weight: Int,
    var profession: String)
