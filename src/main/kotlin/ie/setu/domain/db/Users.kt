package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table
import java.util.*

// SRP - Responsibility is to manage one user.
//       Database wise, this is the table object.

object Users : Table("users") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val email = varchar("email", 255)
    val gender = varchar("gender", 20)
    val birthDate = date("birthdate")
    val mobileNumber = varchar("mobile_number", 20)
    val dietPreferences = varchar("diet_preferences", 25)
    val height = double("height")
    val weight = integer("weight")
    val profession = varchar("profession", 30)
}

