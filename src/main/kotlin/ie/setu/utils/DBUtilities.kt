package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email],
    gender = it[Users.gender],
    birthDate = it[Users.birthDate],
    mobileNumber = it[Users.mobileNumber],
    dietPreferences = it[Users.dietPreferences],
    height = it[Users.height],
    weight = it[Users.weight],
    profession = it[Users.profession]
)

fun mapToUserCred(it: ResultRow) = UserCred(
    id = it[UserCreds.id],
    password = it[UserCreds.password],
    userId = it[UserCreds.userId]
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
    weight = it[Measurements.weight],
    chest = it[Measurements.chest],
    bicep = it[Measurements.bicep],
    neck = it[Measurements.neck],
    abdomen = it[Measurements.abdomen],
    waist = it[Measurements.waist],
    lowerWaist = it[Measurements.lowerWaist],
    thigh = it[Measurements.thigh],
    cough = it[Measurements.cough],
    measuredDate = it[Measurements.measuredDate],
    userId = it[Measurements.userId]
)