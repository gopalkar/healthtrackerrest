package ie.setu.config

import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            //add this jsonMapper to serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.staticFiles.enableWebjars()
            it.vue.vueAppName="app"
        }
            .apply{
                exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
                error(404) { ctx -> ctx.json("404 - Not Found") }
            }
            .start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7000
    }
    private fun registerRoutes(app: Javalin) {
        app.routes {
// The @routeComponent that we added in layout.html earlier will be replaced
// by the String inside the VueComponent. This means a call to / will load
// the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}"){
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                }
            }
            path("/api/usercreds") {
                post(UserCredController::addUserCred)
                path("{user-id}") {
                    get(UserCredController::getUserCredByUserId)
                    delete(UserCredController::deleteUserCred)
                    patch(UserCredController::updateUserCred)
                }
            }
            path("/api/users/email") {
                path("{email-id}"){
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{user-id}") {
                    get(ActivityController::getActivitiesByUserId)
                    delete(ActivityController::deleteActivitiesByUserId)
                }
            }
            path("/api/activity/{activity-id}") {
                get(ActivityController::getActivitiesByActivityId)
                delete(ActivityController::deleteActivity)
                patch(ActivityController::updateActivity)
            }
            path("/api/measurements") {
                get(MeasurementController::getAllMeasurements)
                post(MeasurementController::addMeasurement)
                path("{user-id}") {
                    get(MeasurementController::getMeasurementsByDate)
                    delete(MeasurementController::deleteMeasurementsByUserId)
                }
            }
            path("/api/measurement/{measurement-id}") {
                get(MeasurementController::getMeasurementByMeasurementId)
                delete(MeasurementController::deleteMeasurement)
                patch(MeasurementController::updateMeasurement)
            }
            path("/api/nutritions") {
                get(NutritionController::getAllNutritions)
                post(NutritionController::addNutrition)
                path("{user-id}") {
                    get(NutritionController::getNutritionsByUserId)
                    delete(NutritionController::deleteNutritionsByUserId)
                }
            }
            path("/api/nutrition/{nutrition-id}") {
                get(NutritionController::getNutritionByNutritionId)
                delete(NutritionController::deleteNutrition)
                patch(NutritionController::updateNutrition)
            }
        }
    }
}