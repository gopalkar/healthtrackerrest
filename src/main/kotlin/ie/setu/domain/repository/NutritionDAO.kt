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

    //Find set of nutritions consumed on a specific date or a period
    fun findByDate(userId: Int, startDate: DateTime, endDate: DateTime): List<Nutrition>{
        return transaction {
            Nutritions
                .select() { (Nutritions.userId eq userId) and (Nutritions.macroDate greaterEq startDate) and (Nutritions.macroDate lessEq endDate) }
                .map{ mapToNutrition(it) }
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
    fun save(nutrition: Nutrition): Int?{
        return transaction {
            Nutritions.insert {
                it[partOfDay] = nutrition.partOfDay
                it[foodName] = nutrition.foodName
                it[calories] = nutrition.calories
                it[cholesterol] = nutrition.cholesterol
                it[protein] = nutrition.protein
                it[fat] = nutrition.fat
                it[fiber] = nutrition.fiber
                it[macroDate] = nutrition.macroDate
                it[userId] = nutrition.userId
            } get Nutritions.id
        }
    }

    fun delete(id: Int):Int{
        return transaction{
            Nutritions.deleteWhere{
                Nutritions.id eq id
            }
        }
    }

    fun deleteAll(userid: Int):Int{
        return transaction{
            Nutritions.deleteWhere{
                Nutritions.userId eq userid
            }
        }
    }

    fun update(id: Int, nutrition: Nutrition): Int {
        return transaction {
            Nutritions.update ({
                Nutritions.id eq id}) {
                it[partOfDay] = nutrition.partOfDay
                it[foodName] = nutrition.foodName
                it[calories] = nutrition.calories
                it[cholesterol] = nutrition.cholesterol
                it[protein] = nutrition.protein
                it[fat] = nutrition.fat
                it[fiber] = nutrition.fiber
                it[macroDate] = nutrition.macroDate
            }
        }
    }

}