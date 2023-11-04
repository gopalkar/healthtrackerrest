package ie.setu.controllers

import ie.setu.domain.Nutrition
import ie.setu.domain.repository.NutritionDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

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

    fun addNutrition(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val nutrition = jsonToObject<Nutrition>(ctx.body())
        NutritionController.nutritionDAO.save(nutrition)
        ctx.json(jsonObjectMapper().writeValueAsString(nutrition))
    }
}