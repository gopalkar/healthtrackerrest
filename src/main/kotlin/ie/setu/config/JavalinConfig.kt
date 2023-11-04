package ie.setu.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.controllers.ActivityController
import ie.setu.controllers.UserController
import ie.setu.controllers.MeasurementController
import ie.setu.controllers.NutritionController
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson

class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            //add this jsonMapper to serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
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
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}"){
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
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
                    get(MeasurementController::getMeasurementsByUserId)
                    delete(MeasurementController::deleteMeasurementsByUserId)
                }
            }
            path("/api/measurement/{measurement-id}") {
                get(MeasurementController::getMeasurementByMeasurementId)
                delete(MeasurementController::deleteMeasurement)
                patch(MeasurementController::updateMeasurement)
            }
        }
    }
}