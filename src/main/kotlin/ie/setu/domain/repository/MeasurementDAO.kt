package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.Measurement
import ie.setu.domain.db.Measurements
import ie.setu.utils.mapToMeasurement
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class MeasurementDAO {

    //Get all the measurements in the database regardless of user id
    fun getAll(): ArrayList<Measurement> {
        val measurementsList: ArrayList<Measurement> = arrayListOf()
        transaction {
            Measurements.selectAll().map {
                measurementsList.add(mapToMeasurement(it)) }
        }
        return measurementsList
    }

    //Find a specific measurement by activity id
    fun findByMeasurementsId(id: Int): Measurement?{
        return transaction {
            Measurements
                .select() { Measurements.id eq id}
                .map{mapToMeasurement(it)}
                .firstOrNull()
        }
    }

    //Find all measurements for a specific user id
    fun findByUserId(userId: Int): List<Measurement>{
        return transaction {
            Measurements
                .select {Measurements.userId eq userId}
                .map {mapToMeasurement(it)}
        }
    }

    //Save an Measurement to the database
    fun save(activity: Measurement){
        transaction {
            Measurements.insert {
                it[bodyPart] = activity.bodyPart
                it[size] = activity.size
                it[measuredDate] = activity.measuredDate
                it[userId] = activity.userId
            }
        }
    }

}