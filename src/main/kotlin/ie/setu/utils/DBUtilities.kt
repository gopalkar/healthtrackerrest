package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.Measurement
import ie.setu.domain.Nutrition
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Measurements
import ie.setu.domain.db.Nutritions
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToNutrition(it: ResultRow) = Nutrition(
    id = it[Nutritions.id],
    partOfDay = it[Nutritions.partOfDay],
    foodName = it[Nutritions.foodName],
    calories = it[Nutritions.calories],
    macroDate = it[Nutritions.macroDate],
    userId = it[Nutritions.userId]
)

fun mapToMeasurement(it: ResultRow) = Measurement(
    id = it[Measurements.id],
    bodyPart = it[Measurements.bodyPart],
    size = it[Measurements.size],
    measuredDate = it[Measurements.measuredDate],
    userId = it[Measurements.userId]
)