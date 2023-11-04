package ie.setu.controllers
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject

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
            }
        }
    }

    fun getActivitiesByActivityId(ctx: Context) {
        val activities = ActivityController.activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())
        if (activities != null) {
            //mapper handles the deserialization of Joda date into a String.
            ctx.json(jsonObjectMapper().writeValueAsString(activities))
        }
    }

    fun deleteActivitiesByUserId(ctx: Context) {
        if (ActivityController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = ActivityController.activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                ActivityController.activityDAO.deleteAll(ctx.pathParam("user-id").toInt())
                ctx.json("""{"message":"Activities Deleted Successfully"}""")
            }
        }
    }

    fun updateActivity(ctx: Context) {
        val activity = jsonToObject<Activity>(ctx.body())
        ActivityController.activityDAO.update(ctx.pathParam("activity-id").toInt(), activity)
        ctx.json("""{"message":"Activity Updated Successfully"}""")
    }

    fun deleteActivity(ctx: Context) {
        ActivityController.activityDAO.delete(ctx.pathParam("activity-id").toInt())
        ctx.json("""{"message":"Activity Deleted Successfully"}""")
    }

    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val activity = jsonToObject<Activity>(ctx.body())
        ActivityController.activityDAO.save(activity)
        ctx.json(activity)
    }

}