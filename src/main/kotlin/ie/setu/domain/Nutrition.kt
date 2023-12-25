package ie.setu.domain

import org.joda.time.DateTime

data class Nutrition (var id: Int,
                     var partOfDay:String,
                     var foodName: String,
                     var calories: Double,
                     var cholesterol: Double,
                     var protein: Double,
                     var fat: Double,
                     var fiber: Double,
                     var macroDate: DateTime,
                     var userId: Int)