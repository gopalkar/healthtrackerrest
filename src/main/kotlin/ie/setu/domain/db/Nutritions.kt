package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Nutritions : Table("nutritions") {
    val id = integer("id").autoIncrement().primaryKey()
    val partOfDay = varchar("part_of_day", 100)
    val foodName = varchar("food_name", 100)
    val calories = double("calories")
    val cholesterol = double("cholesterol")
    val protein = double("protein")
    val fat = double("fat")
    val fiber = double("fiber")
    val macroDate = datetime("macro_date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}