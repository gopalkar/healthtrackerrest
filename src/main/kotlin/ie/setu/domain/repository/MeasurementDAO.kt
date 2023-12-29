package ie.setu.domain.repository

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

    //Find measurements between specific dates
    fun findByDate(userId: Int, startDate: DateTime, endDate: DateTime): List<Measurement>{
        return transaction {
            Measurements
                .select() { (Measurements.userId eq userId) and (Measurements.measuredDate greaterEq startDate) and (Measurements.measuredDate lessEq endDate) }
                . orderBy(Measurements.measuredDate, SortOrder.DESC)
                .map{mapToMeasurement(it)}
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
    fun save(measurement: Measurement) : Int? {
        return transaction {
            Measurements.insert {
                it[weight] = measurement.weight
                it[chest] = measurement.chest
                it[bicep] = measurement.bicep
                it[neck] = measurement.neck
                it[abdomen] = measurement.abdomen
                it[waist] = measurement.waist
                it[lowerWaist] = measurement.lowerWaist
                it[thigh] = measurement.thigh
                it[calves] = measurement.calves
                it[measuredDate] = DateTime.parse(measurement.measuredDate.toString())
                it[userId] = measurement.userId
            } get Measurements.id
        }
    }

    fun delete(id: Int):Int{
        return transaction{
            Measurements.deleteWhere{
                Measurements.id eq id
            }
        }
    }

    fun deleteAll(userid: Int):Int{
        return transaction{
            Measurements.deleteWhere{
                Measurements.userId eq userid
            }
        }
    }

    fun update(id: Int, measurement: Measurement): Int {
        return transaction {
            Measurements.update ({
                Measurements.id eq id}) {
                it[weight] = measurement.weight
                it[chest] = measurement.chest
                it[bicep] = measurement.bicep
                it[neck] = measurement.neck
                it[abdomen] = measurement.abdomen
                it[waist] = measurement.waist
                it[lowerWaist] = measurement.lowerWaist
                it[thigh] = measurement.thigh
                it[calves] = measurement.calves
                it[measuredDate] = DateTime.parse(measurement.measuredDate.toString())
            }
        }
    }

}