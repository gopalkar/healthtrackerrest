package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Measurements : Table("measurements") {
    val id = integer("id").autoIncrement().primaryKey()
    val bodyPart = varchar("body_part", 100)
    val size = double("size")
    val measuredDate = datetime("measured_date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}