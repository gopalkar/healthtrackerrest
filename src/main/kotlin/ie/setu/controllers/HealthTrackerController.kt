package ie.setu.controllers
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.User
import io.javalin.http.Context
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.Activity
import ie.setu.domain.Measurement
import ie.setu.domain.Nutrition
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.MeasurementDAO
import ie.setu.domain.repository.NutritionDAO

object HealthTrackerController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
    private val measurementDAO =  MeasurementDAO()
    private val nutritionDAO = NutritionDAO()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun addUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    fun getUserByEmail(ctx: Context){
        val user = userDao.findByEmail(ctx.pathParam("email-id").toString())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun deleteUser(ctx: Context){
        userDao.delete(ctx.pathParam("user-id").toInt())
        ctx.json("""{"message":"User Deleted Successfully"}""")
    }

    fun updateUser(ctx: Context){
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDao.update(ctx.pathParam("user-id").toInt(), user)
        ctx.json("""{"message":"User Updated Successfully"}""")
    }

    //--------------------------------------------------------------
    // ActivityDAO specifics
    //-------------------------------------------------------------

    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( activityDAO.getAll() ))
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(activities))
            }
        }
    }

    fun getActivitiesByActivityId(ctx: Context) {
        val activities = activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())
        if (activities != null) {
           //mapper handles the deserialization of Joda date into a String.
           val mapper = jacksonObjectMapper()
               .registerModule(JodaModule())
               .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
           ctx.json(mapper.writeValueAsString(activities))
        }
    }

    fun deleteActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                activityDAO.deleteAll(ctx.pathParam("user-id").toInt())
                ctx.json("""{"message":"Activities Deleted Successfully"}""")
            }
        }
    }

    fun updateActivity(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDAO.update(ctx.pathParam("activity-id").toInt(), activity)
        ctx.json("""{"message":"Activity Updated Successfully"}""")
    }

    fun deleteActivity(ctx: Context) {
        activityDAO.delete(ctx.pathParam("activity-id").toInt())
        ctx.json("""{"message":"Activity Deleted Successfully"}""")
    }

    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDAO.save(activity)
        val retActivity = mapper.writeValueAsString(activity)
        ctx.json(retActivity)
    }


    //--------------------------------------------------------------
    // MeasurementDAO specifics
    //-------------------------------------------------------------

    fun getAllMeasurements(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( measurementDAO.getAll() ))
    }

    fun getMeasurementsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val measurements = measurementDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (measurements.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(measurements))
            }
        }
    }

    fun getMeasurementByMeasurementId(ctx: Context) {
        val measurements = measurementDAO.findByMeasurementsId(ctx.pathParam("measurement-id").toInt())
        if (measurements != null) {
            //mapper handles the deserialization of Joda date into a String.
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(measurements))
        }
    }

    fun deleteMeasurementsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val measurements = measurementDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (measurements.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                measurementDAO.deleteAll(ctx.pathParam("user-id").toInt())
                ctx.json("""{"message":"Measurements Deleted Successfully"}""")
            }
        }
    }

    fun updateMeasurement(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val measurement = mapper.readValue<Measurement>(ctx.body())
        measurementDAO.update(ctx.pathParam("measurement-id").toInt(), measurement)
        ctx.json("""{"message":"Measurement Updated Successfully"}""")
    }

    fun deleteMeasurement(ctx: Context) {
        measurementDAO.delete(ctx.pathParam("measurement-id").toInt())
        ctx.json("""{"message":"Measurement Deleted Successfully"}""")
    }

    fun addMeasurement(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val measurement = mapper.readValue<Measurement>(ctx.body())
        measurementDAO.save(measurement)
        val retMeasurement = mapper.writeValueAsString(measurement)
        ctx.json(retMeasurement)
    }

    //--------------------------------------------------------------
    // NutritionDAO specifics
    //-------------------------------------------------------------

    fun getAllNutritions(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( nutritionDAO.getAll() ))
    }

    fun getNutritionsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val nutritions = nutritionDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (nutritions.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(nutritions))
            }
        }
    }

    fun addNutrition(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val nutrition = mapper.readValue<Nutrition>(ctx.body())
        nutritionDAO.save(nutrition)
        val retNutrition = mapper.writeValueAsString(nutrition)
        ctx.json(retNutrition)
    }

}