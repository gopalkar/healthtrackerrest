package ie.setu.domain.repository

import ie.setu.domain.Nutrition
import ie.setu.domain.db.Nutritions
import ie.setu.utils.mapToNutrition
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class NutritionDAO {

    //Get all the nutritions in the database regardless of user id
    fun getAll(): ArrayList<Nutrition> {
        val nutritionsList: ArrayList<Nutrition> = arrayListOf()
        transaction {
            Nutritions.selectAll().map {
                nutritionsList.add(mapToNutrition(it)) }
        }
        return nutritionsList
    }

    //Find a specific nutrition by nutrition id
    fun findByNutritionId(id: Int): Nutrition?{
        return transaction {
            Nutritions
                .select() { Nutritions.id eq id}
                .map{mapToNutrition(it)}
                .firstOrNull()
        }
    }

    //Find all nutritions for a specific user id
    fun findByUserId(userId: Int): List<Nutrition>{
        return transaction {
            Nutritions
                .select {Nutritions.userId eq userId}
                .map {mapToNutrition(it)}
        }
    }

    //Save an nutrition to the database
    fun save(nutrition: Nutrition){
        transaction {
            Nutritions.insert {
                it[partOfDay] = nutrition.partOfDay
                it[foodName] = nutrition.foodName
                it[calories] = nutrition.calories
                it[macroDate] = nutrition.macroDate
                it[userId] = nutrition.userId
            }
        }
    }

}