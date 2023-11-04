package ie.setu.controllers

import ie.setu.domain.Measurement
import ie.setu.domain.repository.MeasurementDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object MeasurementController {

    private val userDao = UserDAO()
    private val measurementDAO =  MeasurementDAO()

    //--------------------------------------------------------------
    // MeasurementDAO specifics
    //-------------------------------------------------------------

    fun getAllMeasurements(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jsonObjectMapper()
        ctx.json(mapper.writeValueAsString( MeasurementController.measurementDAO.getAll() ))
    }

    fun getMeasurementsByUserId(ctx: Context) {
        if (MeasurementController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val measurements = MeasurementController.measurementDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (measurements.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jsonObjectMapper()
                ctx.json(mapper.writeValueAsString(measurements))
            }
        }
    }

    fun getMeasurementByMeasurementId(ctx: Context) {
        val measurements = MeasurementController.measurementDAO.findByMeasurementsId(ctx.pathParam("measurement-id").toInt())
        if (measurements != null) {
            //mapper handles the deserialization of Joda date into a String.
            val mapper = jsonObjectMapper()
            ctx.json(mapper.writeValueAsString(measurements))
        }
    }

    fun deleteMeasurementsByUserId(ctx: Context) {
        if (MeasurementController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val measurements = MeasurementController.measurementDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (measurements.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                MeasurementController.measurementDAO.deleteAll(ctx.pathParam("user-id").toInt())
                ctx.json("""{"message":"Measurements Deleted Successfully"}""")
            }
        }
    }

    fun updateMeasurement(ctx: Context) {
        val measurement = jsonToObject<Measurement>(ctx.body())
        MeasurementController.measurementDAO.update(ctx.pathParam("measurement-id").toInt(), measurement)
        ctx.json("""{"message":"Measurement Updated Successfully"}""")
    }

    fun deleteMeasurement(ctx: Context) {
        MeasurementController.measurementDAO.delete(ctx.pathParam("measurement-id").toInt())
        ctx.json("""{"message":"Measurement Deleted Successfully"}""")
    }

    fun addMeasurement(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val measurement = jsonToObject<Measurement>(ctx.body())
        MeasurementController.measurementDAO.save(measurement)
        ctx.json(jsonObjectMapper().writeValueAsString(measurement))
    }
}