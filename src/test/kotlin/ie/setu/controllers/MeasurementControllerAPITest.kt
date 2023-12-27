package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Measurement
import ie.setu.domain.User
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.measurements
import ie.setu.helpers.users
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MeasurementControllerAPITest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    val user1 = users.get(0)
    val measurement1 = measurements.get(0)

    @Nested
    inner class TestMeasurements {

        @Nested
        inner class CreateMeasurements {
            @Test
            fun `add a measurement with correct details returns a 201 response`() {

                //Arrange & Act & Assert
                //    add the user and verify return code (using fixture data)
                val addUserResponse = addUser(user1)

                //    add the measurement and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                measurement1.userId = retrievedUser.id
                val addMeasurementResponse = addMeasurement(measurement1)
                assertEquals(201, addMeasurementResponse.status)

                //Assert - retrieve the added measurement from the database and verify return code
                val retrievedMeasurement: Measurement = jsonToObject(addMeasurementResponse.body.toString())
                val retrieveMeasurementResponse = retrieveMeasurementById(retrievedMeasurement.id)
                assertEquals(200, retrieveMeasurementResponse.status)

                //Assert - verify the contents of the retrieved user
                assertEquals(measurement1.abdomen, retrievedMeasurement.abdomen)
                assertEquals(measurement1.bicep, retrievedMeasurement.bicep)
                assertEquals(measurement1.chest, retrievedMeasurement.chest)

                //After - restore the db to previous state by deleting the added user
                val deleteResponse = deleteMeasurement(retrievedMeasurement.id)
                assertEquals(204, deleteResponse.status)

                deleteUser(retrievedUser.id)
            }
        }

        @Nested
        inner class ReadMeasurements {

            val localDateTime = LocalDateTime.now()
            val jodaDateTime = DateTime(localDateTime.toDateTime(), DateTimeZone.getDefault())
            val formattedDateTime = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss").print(jodaDateTime)

            @Test
            fun `get all Measurements from the database returns 200 or 404 response`() {
                val response = Unirest.get(origin + "/api/measurements/").asString()
                if (response.status == 200) {
                    val retrievedMeasurements: ArrayList<Measurement> = jsonToObject(response.body.toString())
                    assertNotEquals(0, retrievedMeasurements.size)
                } else {
                    assertEquals(404, response.status)
                }
            }

            @Test
            fun `get Measurement by id when measurement does not exist returns 404 response`() {

                //Arrange - test data for user id
                val id = Integer.MIN_VALUE

                // Act - attempt to retrieve the non-existent user from the database
                val retrieveResponse = retrieveMeasurementById(id)

                // Assert -  verify return code
                assertEquals(404, retrieveResponse.status)
            }

            @Test
            fun `get Measurement by user-id when Measurement does not exist returns 404 response`() {

                val id = 1001

                // Arrange & Act - attempt to retrieve the non-existent user from the database
                val retrieveResponse = retrieveMeasurementByUser(id, DateTime("2020-01-01T00:00:00"), DateTime(formattedDateTime))

                // Assert -  verify return code
                assertEquals(404, retrieveResponse.status)
            }

            @Test
            fun `getting a Measurement by user-id when Measurement exists, returns a 200 response`() {

                //Arrange & Act & Assert

                val addUserResponse = addUser(user1)

                //    add the measurement and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                measurement1.userId = retrievedUser.id


                val addMeasurementResponse = addMeasurement(measurement1)
                assertEquals(201, addMeasurementResponse.status)

                val retrievedMeasurement: Measurement = jsonToObject(addMeasurementResponse.body.toString())
                val retrieveMeasurementResponse = retrieveMeasurementByUser(retrievedMeasurement.userId, DateTime("2020-01-01T00:00:00"), DateTime(formattedDateTime))
                if (retrieveMeasurementResponse.status == 200) {
                    val retrievedMeasurements: ArrayList<Measurement> = jsonToObject(retrieveMeasurementResponse.body.toString())
                    assertNotEquals(0, retrievedMeasurements.size)
                } else {
                    assertEquals(404, retrieveMeasurementResponse.status)
                }

                //After - restore the db to previous state by deleting the added user
                val deleteResponse = deleteMeasurement(retrievedMeasurement.id)
                assertEquals(204, deleteResponse.status)

                deleteUser(retrievedUser.id)
            }
        }

        @Nested
        inner class UpdateMeasurements {
            @Test
            fun `updating a Measurement when it exists, returns a 204 response`() {

                val addUserResponse = addUser(user1)

                //    add the user and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                measurement1.userId = retrievedUser.id
                val addMeasurementResponse = addMeasurement(measurement1)
                assertEquals(201, addMeasurementResponse.status)

                //Assert - retrieve the measurement from the database and verify return code
                val retrievedMeasurement: Measurement = jsonToObject(addMeasurementResponse.body.toString())
                val retrieveMeasurementResponse = retrieveMeasurementById(retrievedMeasurement.id)
                assertEquals(200, retrieveMeasurementResponse.status)

                measurement1.id = retrievedMeasurement.id
                measurement1.abdomen = 35.0
                measurement1.bicep = 13.0

                val updatedMeasurementResponse = updateMeasurement(measurement1)
                assertEquals(204, updatedMeasurementResponse.status)
                //Act & Assert - retrieve updated user and assert details are correct

                val retrieveUpdatedMeasurement = retrieveMeasurementById(measurement1.id)
                val updatedMeasurementValidation: Measurement = jsonToObject(retrieveUpdatedMeasurement.body.toString())
                assertEquals(35.0, updatedMeasurementValidation.abdomen)
                assertEquals(13.0, updatedMeasurementValidation.bicep)

                //After - restore the db to previous state by deleting the added user
                val deleteResponse = deleteMeasurement(retrievedMeasurement.id)
                assertEquals(204, deleteResponse.status)

                deleteUser(retrievedUser.id)
            }

            @Test
            fun `updating a Measurement when it doesn't exist, returns a 404 response`() {

                //Act & Assert - attempt to update the measurement that doesn't exist
                val unknownMeasurement: Measurement = measurement1
                assertEquals(404, updateMeasurement(unknownMeasurement).status)
            }
        }

        @Nested
        inner class DeleteMeasurements {
            @Test
            fun `deleting a Measurement when it doesn't exist, returns a 404 response`() {
                //Act & Assert - attempt to delete a user that doesn't exist
                assertEquals(404, deleteMeasurement(-1).status)
            }

            @Test
            fun `deleting a measurement when it exists, returns a 204 response`() {

                val addUserResponse = addUser(user1)

                //    add the user and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                measurement1.userId = retrievedUser.id
                val addMeasurementResponse = addMeasurement(measurement1)
                assertEquals(201, addMeasurementResponse.status)

                //Assert - retrieve the added user from the database and verify return code
                val retrievedMeasurement: Measurement = jsonToObject(addMeasurementResponse.body.toString())
                val retrieveMeasurementResponse = retrieveMeasurementById(retrievedMeasurement.id)
                assertEquals(200, retrieveMeasurementResponse.status)

                //Act & Assert - delete the added user and assert a 204 is returned
                assertEquals(204, deleteMeasurement(retrievedMeasurement.id).status)

                //Act & Assert - attempt to retrieve the deleted user --> 404 response
                assertEquals(404, retrieveMeasurementById(retrievedMeasurement.id).status)

                deleteUser(retrievedUser.id)
            }
        }

        //helper function to add a test user to the database
        private fun addUser(user: User): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/users")
                .body("{" +
                        "\"name\":\"${user.name}\", " +
                        "\"email\":\"${user.email}\"," +
                        "\"gender\":\"${user.gender}\"," +
                        "\"birthDate\":\"${user.birthDate}\"," +
                        "\"mobileNumber\":\"${user.mobileNumber}\"," +
                        "\"dietPreferences\":\"${user.dietPreferences}\"," +
                        "\"height\":\"${user.height}\"," +
                        "\"weight\":\"${user.weight}\"," +
                        "\"profession\":\"${user.profession}\"" +
                        "}")
                .asJson()
        }

        //helper function to delete a test user from the database
        private fun deleteUser(id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/users/$id").asString()
        }

        //helper function to add a test measurement to the database
        private fun addMeasurement(measurement: Measurement): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/measurements")
                .body("{" +
                        "\"weight\":\"${measurement.weight}\"," +
                        "\"chest\":\"${measurement.chest}\"," +
                        "\"bicep\":\"${measurement.bicep}\"," +
                        "\"neck\":\"${measurement.neck}\"," +
                        "\"abdomen\":\"${measurement.abdomen}\"," +
                        "\"waist\":\"${measurement.waist}\"," +
                        "\"lowerWaist\":\"${measurement.lowerWaist}\"," +
                        "\"thigh\":\"${measurement.thigh}\"," +
                        "\"calves\":\"${measurement.calves}\"," +
                        "\"measuredDate\":\"${measurement.measuredDate}\", " +
                        "\"userId\":\"${measurement.userId}\"" +
                        "}")
                .asJson()
        }

        //helper function to delete a test user from the database
        private fun deleteMeasurement(id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/measurement/${id}").asString()
        }

        //helper function to retrieve a test user from the database by email
        private fun retrieveMeasurementByUser(id: Int, startDate: DateTime, endDate: DateTime): HttpResponse<String> {
            return Unirest.get(origin + "/api/measurements/${id}?start-date=${startDate}&end-date=${endDate}").asString()
        }

        //helper function to retrieve a test user from the database by id
        private fun retrieveMeasurementById(id: Int): HttpResponse<String> {
            return Unirest.get(origin + "/api/measurement/${id}").asString()
        }

        //helper function to add a test user to the database
        private fun updateMeasurement(measurement: Measurement): HttpResponse<JsonNode> {
            return Unirest.patch(origin + "/api/measurement/${measurement.id}")
                .body("{" +
                        "\"weight\":\"${measurement.weight}\"," +
                        "\"chest\":\"${measurement.chest}\"," +
                        "\"bicep\":\"${measurement.bicep}\"," +
                        "\"neck\":\"${measurement.neck}\"," +
                        "\"abdomen\":\"${measurement.abdomen}\"," +
                        "\"waist\":\"${measurement.waist}\"," +
                        "\"lowerWaist\":\"${measurement.lowerWaist}\"," +
                        "\"thigh\":\"${measurement.thigh}\"," +
                        "\"calves\":\"${measurement.calves}\"," +
                        "\"measuredDate\":\"${measurement.measuredDate}\", " +
                        "\"userId\":\"${measurement.userId}\"" +
                        "}")
                .asJson()
        }
    }
}