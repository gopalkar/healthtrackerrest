package ie.setu.domain

import org.joda.time.DateTime

data class Nutrition (var id: Int,
                     var partOfDay:String,
                     var foodName: String,
                     var calories: Int,
                     var macroDate: DateTime,
                     var userId: Int)