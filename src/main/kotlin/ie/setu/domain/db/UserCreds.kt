package ie.setu.domain.db

import ie.setu.domain.db.Activities.references
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import java.util.*

// SRP - Responsibility is to manage one user.
//       Database wise, this is the table object.

object UserCreds : Table("users") {
    val id = integer("id").autoIncrement().primaryKey()
    val password = varchar("password", 40)
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}

