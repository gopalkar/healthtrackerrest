package ie.setu.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.controllers.HealthTrackerController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson

class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            //add this jsonMapper to serialise objects to json
            it.jsonMapper(JavalinJackson(jacksonObjectMapper()))
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
                get(HealthTrackerController::getAllUsers)
                post(HealthTrackerController::addUser)
                path("{user-id}"){
                    get(HealthTrackerController::getUserByUserId)
                    delete(HealthTrackerController::deleteUser)
                    patch(HealthTrackerController::updateUser)
                }
            }
            path("/api/users/email") {
                path("{email-id}"){
                    get(HealthTrackerController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(HealthTrackerController::getAllActivities)
                post(HealthTrackerController::addActivity)
                path("{user-id}") {
                    get(HealthTrackerController::getActivitiesByUserId)
                    delete(HealthTrackerController::deleteActivitiesByUserId)
                }
            }
            path("/api/activity/{activity-id}") {
                get(HealthTrackerController::getActivitiesByActivityId)
                delete(HealthTrackerController::deleteActivity)
                patch(HealthTrackerController::updateActivity)
            }
            path("/api/measurements") {
                get(HealthTrackerController::getAllMeasurements)
                post(HealthTrackerController::addMeasurement)
                path("{user-id}") {
                    get(HealthTrackerController::getMeasurementsByUserId)
                    delete(HealthTrackerController::deleteMeasurementsByUserId)
                }
            }
            path("/api/measurement/{measurement-id}") {
                get(HealthTrackerController::getMeasurementByMeasurementId)
                delete(HealthTrackerController::deleteMeasurement)
                patch(HealthTrackerController::updateMeasurement)
            }
        }
    }
}