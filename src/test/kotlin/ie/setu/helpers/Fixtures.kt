package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.Measurement
import ie.setu.domain.Nutrition
import ie.setu.domain.User
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "alice@wonderland.com"
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"
val validActivity = "Running"
val validDuration = 1.5
val validCalories = 100
val validStarted = "2023-11-03T05:59:27.258Z"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", gender = "Female", birthDate = DateTime("1986-11-03T05:59:27"), mobileNumber = "987-569-7854", dietPreferences = "Vegetarian", height = 5.10, weight = 150, profession = "Information Technology", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", gender = "Male", birthDate = DateTime("1976-01-03T05:59:27"), mobileNumber = "487-529-7854", dietPreferences = "Vegetarian", height = 6.10, weight = 140, profession = "Actor", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", gender = "Female", birthDate = DateTime("1978-12-03T05:59:27"), mobileNumber = "957-529-7454", dietPreferences = "NonVegetarian", height = 5.01, weight = 170, profession = "Mechanic", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", gender = "Female", birthDate = DateTime("1996-05-03T05:59:27"), mobileNumber = "977-569-7354", dietPreferences = "Vegetarian", height = 5.7, weight = 160, profession = "Farmer", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(description = "Running", duration = 45.0, calories = 450, started = DateTime("2023-11-10T05:59:27"), userId = 1, id = 1)
)

val measurements = arrayListOf<Measurement>(
    Measurement(id = 1, weight = 145.0, chest = 35.0, bicep = 12.0, neck = 14.0, abdomen = 32.0, waist = 30.0, lowerWaist = 31.5, thigh = 21.0, calves = 13.5, measuredDate = DateTime("2023-11-10T05:59:27"), userId = 1)
)

val nutritions = arrayListOf<Nutrition>(
    Nutrition(id = 1, partOfDay = "Breakfast", foodName = "Pancakes", calories = 227.0, cholesterol = 28.3, protein = 6.4, fat = 9.7, fiber = 0.0, macroDate = DateTime("2023-11-10T05:59:27"), userId = 1)
)