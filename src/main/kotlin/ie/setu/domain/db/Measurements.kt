package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Measurements : Table("measurements") {
    val id = integer("id").autoIncrement().primaryKey()
    val weight = double("weight")
    val chest = double("chest")
    val bicep = double("bicep")
    val neck = double("neck")
    val abdomen = double("abdomen")
    val waist = double("waist")
    val lowerWaist = double("lowerwaist")
    val thigh = double("thigh")
    val calves = double("calves")
    val measuredDate = datetime("measured_date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}
