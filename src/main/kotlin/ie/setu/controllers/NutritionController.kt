package ie.setu.controllers

import ie.setu.domain.Nutrition
import ie.setu.domain.repository.NutritionDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import org.joda.time.DateTime
import java.time.format.DateTimeFormatter

object NutritionController {

    private val userDao = UserDAO()
    private val nutritionDAO = NutritionDAO()
    //--------------------------------------------------------------
    // NutritionDAO specifics
    //-------------------------------------------------------------

    fun getAllNutritions(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        ctx.json(jsonObjectMapper().writeValueAsString( NutritionController.nutritionDAO.getAll() ))
    }

    fun getNutritionsByUserId(ctx: Context) {
        if (NutritionController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val nutritions = NutritionController.nutritionDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (nutritions.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                ctx.json(jsonObjectMapper().writeValueAsString(nutritions))
            }
        }
    }

    fun getNutritionsByDate(ctx: Context) {
        if (NutritionController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val startDateTime: DateTime = DateTime.parse(ctx.queryParam("start-date"))
            val endDateTime: DateTime = DateTime.parse(ctx.queryParam("end-date"))
            val mapper = jsonObjectMapper()
            if (startDateTime != null) {
                ctx.status(200)
                ctx.json(
                    mapper.writeValueAsString(
                        NutritionController.nutritionDAO.findByDate(
                            ctx.pathParam("user-id").toInt(),
                            startDateTime,
                            endDateTime
                        )
                    )
                )
            }
            else {
                val nutritions = NutritionController.nutritionDAO.findByUserId(ctx.pathParam("user-id").toInt())
                if (nutritions.isNotEmpty()) {
                    //mapper handles the deserialization of Joda date into a String.
                    val mapper = jsonObjectMapper()
                    ctx.json(mapper.writeValueAsString(nutritions))
                }
            }
        }
        else {
            ctx.status(404)
        }
    }

    fun getNutritionByNutritionId(ctx: Context) {
        val nutritions = NutritionController.nutritionDAO.findByNutritionId(ctx.pathParam("nutrition-id").toInt())
        if (nutritions != null) {
            //mapper handles the deserialization of Joda date into a String.
            val mapper = jsonObjectMapper()
            ctx.json(mapper.writeValueAsString(nutritions))
        }
    }

    fun deleteNutritionsByUserId(ctx: Context) {
        if (NutritionController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val nutritions = NutritionController.nutritionDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (nutritions.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                NutritionController.nutritionDAO.deleteAll(ctx.pathParam("user-id").toInt())
                ctx.json("""{"message":"nutritions Deleted Successfully"}""")
            }
        }
    }

    fun updateNutrition(ctx: Context) {
        val nutrition = jsonToObject<Nutrition>(ctx.body())
        NutritionController.nutritionDAO.update(ctx.pathParam("nutrition-id").toInt(), nutrition)
        ctx.json("""{"message":"Nutrition Updated Successfully"}""")
    }

    fun deleteNutrition(ctx: Context) {
        NutritionController.nutritionDAO.delete(ctx.pathParam("nutrition-id").toInt())
        ctx.json("""{"message":"Nutrition Deleted Successfully"}""")
    }

    fun addNutrition(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val nutrition = jsonToObject<Nutrition>(ctx.body())
        NutritionController.nutritionDAO.save(nutrition)
        ctx.json(jsonObjectMapper().writeValueAsString(nutrition))
    }
}