package ie.setu.controllers

import ie.setu.config.edamanAPI
import ie.setu.domain.Nutrition
import ie.setu.domain.edamanResponse
import ie.setu.domain.repository.NutritionDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import kong.unirest.Unirest
import org.joda.time.DateTime
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
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

/*    fun getNutritionsByUserId(ctx: Context) {
        if (NutritionController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val nutritions = NutritionController.nutritionDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (nutritions.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                ctx.json(jsonObjectMapper().writeValueAsString(nutritions))
            }
        }
    }*/

    fun getNutritionsByDate(ctx: Context) {
        if (NutritionController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val startDateTime: DateTime = DateTime.parse(ctx.queryParam("start-date"))
            val endDateTime: DateTime = DateTime.parse(ctx.queryParam("end-date"))
            val mapper = jsonObjectMapper()
            val nutritions = NutritionController.nutritionDAO.findByDate(ctx.pathParam("user-id").toInt(), startDateTime, endDateTime)
            if (nutritions.isNotEmpty()) {
                ctx.status(200)
                ctx.json(mapper.writeValueAsString(nutritions))
            }
            else {
                ctx.status(404)
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
            ctx.status(200)
        }
        else {
            ctx.status(404)
        }
    }

    fun deleteNutritionsByUserId(ctx: Context) {
        if (NutritionController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val nutritions = NutritionController.nutritionDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (nutritions.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                NutritionController.nutritionDAO.deleteAll(ctx.pathParam("user-id").toInt())
                ctx.json("""{"message":"nutritions Deleted Successfully"}""")
                ctx.status(204)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun updateNutrition(ctx: Context) {
        val nutrition = jsonToObject<Nutrition>(ctx.body())
        val nutritions = NutritionController.nutritionDAO.findByNutritionId(ctx.pathParam("nutrition-id").toInt())
        if (nutritions != null) {
            nutrition.id = nutritions.id
            NutritionController.nutritionDAO.update(ctx.pathParam("nutrition-id").toInt(), nutrition)
            ctx.json(nutrition)
            ctx.status(204)
        }
        else   {
            ctx.status(404)
        }
    }

    fun deleteNutrition(ctx: Context) {
        if (NutritionController.nutritionDAO.delete(ctx.pathParam("nutrition-id").toInt()) != 0)
        ctx.status(204)
        else
        ctx.status(404)
    }

    fun addNutrition(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val nutrition = jsonToObject<Nutrition>(ctx.body())
        val nutritionId = NutritionController.nutritionDAO.save(nutrition)
        if (nutritionId != null) {
            nutrition.id = nutritionId
            ctx.json(jsonObjectMapper().writeValueAsString(nutrition))
            ctx.status(201)
        }
    }

    fun getNutritionSearch(ctx:Context) {

        val encodedSearchTerm: String = URLEncoder.encode(ctx.queryParam("searchQuery"), "UTF-8")
        val searchQueryParm = "ingr"

        print(encodedSearchTerm)
        val apiUrl = String.format("%s&%s=%s", edamanAPI.getAPIUrl(), searchQueryParm, encodedSearchTerm)
        //print(apiUrl)
        try {
            // Create a URL object
            val url = URL(apiUrl)
            // Open a connection to the URL
            val retrieveEdamanResponse = Unirest.get(apiUrl).asString()

            if (retrieveEdamanResponse.status == HttpURLConnection.HTTP_OK) {
                // Read the response
                val jsonResponse = retrieveEdamanResponse.body
                //print(jsonResponse)
                val edamanFoodResponse: edamanResponse = jsonToObject(retrieveEdamanResponse.body.toString())

                if (edamanFoodResponse.parsed.size > 0 ) {
                    val nutrient = edamanFoodResponse.parsed.get(0).food.nutrients
                    if (nutrient != null) {
                        ctx.json(nutrient)
                    }
                }
                else {
                    val nutrient = edamanFoodResponse.hints.get(0).food.nutrients
                    if (nutrient != null) {
                        ctx.json(nutrient)
                    }
                }
                ctx.status(200)
            } else {
                ctx.status(retrieveEdamanResponse.status)
            }
        } catch (e: Exception) {
            ctx.status(404)
            println("Exception: ${e.message}")
        }
    }
}