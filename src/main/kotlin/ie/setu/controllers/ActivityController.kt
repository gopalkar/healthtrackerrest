package ie.setu.controllers
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object ActivityController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

    //--------------------------------------------------------------
    // ActivityDAO specifics
    //-------------------------------------------------------------

    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jsonObjectMapper()
        ctx.json(mapper.writeValueAsString( ActivityController.activityDAO.getAll() ))
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (ActivityController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = ActivityController.activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                ctx.json(jsonObjectMapper().writeValueAsString(activities))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun getActivitiesByActivityId(ctx: Context) {
        val activities = ActivityController.activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())
        if (activities != null) {
            //mapper handles the deserialization of Joda date into a String.
            ctx.json(jsonObjectMapper().writeValueAsString(activities))
            ctx.status(200)
        }
        else {
            ctx.status(404)
        }
    }

    fun deleteActivitiesByUserId(ctx: Context) {
        if (ActivityController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = ActivityController.activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                ActivityController.activityDAO.deleteAll(ctx.pathParam("user-id").toInt())
                ctx.json("""{"message":"Activities Deleted Successfully"}""")
                ctx.status(204)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun updateActivity(ctx: Context) {
        val activity: Activity = jsonToObject(ctx.body())
        val activities = ActivityController.activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())
        if (activities != null) {
            activity.id = activities.id
            ActivityController.activityDAO.update(ctx.pathParam("activity-id").toInt(), activity)
            ctx.json(activity)
            ctx.status(204)
        }
        else   {
            ctx.status(404)
        }
    }

    fun deleteActivity(ctx: Context) {
        if (ActivityController.activityDAO.delete(ctx.pathParam("activity-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val activity: Activity = jsonToObject(ctx.body())
        val activityId = ActivityController.activityDAO.save(activity)
        if (activityId != null) {
            activity.id = activityId
            ctx.json(activity)
            ctx.status(201)
        }
    }

}