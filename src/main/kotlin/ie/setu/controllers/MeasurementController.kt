package ie.setu.controllers

import ie.setu.domain.Measurement
import ie.setu.domain.repository.MeasurementDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import org.joda.time.DateTime
import java.time.format.DateTimeFormatter

object MeasurementController {

    private val userDao = UserDAO()
    private val measurementDAO =  MeasurementDAO()

    //--------------------------------------------------------------
    // MeasurementDAO specifics
    //-------------------------------------------------------------

    fun getAllMeasurements(ctx: Context) {
     //mapper handles the deserialization of Joda date into a String.
        val mapper = jsonObjectMapper()
        ctx.json(mapper.writeValueAsString(MeasurementController.measurementDAO.getAll()))
    }

/*    fun getMeasurementsByUserId(ctx: Context) {
        if (MeasurementController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val measurements = MeasurementController.measurementDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (measurements.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jsonObjectMapper()
                ctx.json(mapper.writeValueAsString(measurements))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }

        }
        else {
            ctx.status(404)
        }
    }*/

    fun getMeasurementsByDate(ctx: Context) {
        if (MeasurementController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val startDateTime: DateTime = DateTime.parse(ctx.queryParam("start-date"))
            val endDateTime: DateTime = DateTime.parse(ctx.queryParam("end-date"))
            val mapper = jsonObjectMapper()
            val measurements = MeasurementController.measurementDAO.findByDate(ctx.pathParam("user-id").toInt(),startDateTime, endDateTime)
            if (measurements.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                ctx.status(200)
                ctx.json(mapper.writeValueAsString(measurements))
            }
            else {
                ctx.status(404)
            }
        }
        else {
            ctx.status(404)
        }
    }

    fun getMeasurementByMeasurementId(ctx: Context) {
        val measurements = MeasurementController.measurementDAO.findByMeasurementsId(ctx.pathParam("measurement-id").toInt())
        if (measurements != null) {
            //mapper handles the deserialization of Joda date into a String.
            val mapper = jsonObjectMapper()
            ctx.json(mapper.writeValueAsString(measurements))
            ctx.status(200)
        }
        else {
            ctx.status(404)
        }
    }

    fun deleteMeasurementsByUserId(ctx: Context) {
        if (MeasurementController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val measurements = MeasurementController.measurementDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (measurements.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                MeasurementController.measurementDAO.deleteAll(ctx.pathParam("user-id").toInt())
                ctx.json("""{"message":"Measurements Deleted Successfully"}""")
                ctx.status(204)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun updateMeasurement(ctx: Context) {
        val measurement = jsonToObject<Measurement>(ctx.body())
        val measurements = MeasurementController.measurementDAO.findByMeasurementsId(ctx.pathParam("measurement-id").toInt())
        if (measurements != null) {
            measurement.id = measurements.id
            MeasurementController.measurementDAO.update(ctx.pathParam("measurement-id").toInt(), measurement)
            ctx.json(measurement)
            ctx.status(204)
        }
        else   {
            ctx.status(404)
        }
    }

    fun deleteMeasurement(ctx: Context) {
        if (MeasurementController.measurementDAO.delete(ctx.pathParam("measurement-id").toInt()) != 0 )
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun addMeasurement(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val measurement = jsonToObject<Measurement>(ctx.body())
        val measurementId = MeasurementController.measurementDAO.save(measurement)
        if (measurementId != null) {
            measurement.id = measurementId
            ctx.json(measurement)
            ctx.status(201)
        }
    }
}