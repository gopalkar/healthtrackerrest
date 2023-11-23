package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one user.
//       Database wise, this is the table object.

object UserCreds : Table("usercreds") {
    val id = integer("id").autoIncrement().primaryKey()
    val password = varchar("password", 100)
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
}

