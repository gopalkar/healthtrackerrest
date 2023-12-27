package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Nutrient
import ie.setu.domain.Nutrition
import ie.setu.domain.User
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.nutritions
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
class NutritionControllerAPITest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    val user1 = users.get(0)
    val nutrition1 = nutritions.get(0)

    @Nested
    inner class TestNutritions {

        @Nested
        inner class CreateNutritions {
            @Test
            fun `add a nutrition with correct details returns a 201 response`() {

                //Arrange & Act & Assert
                //    add the user and verify return code (using fixture data)
                val addUserResponse = addUser(user1)

                //    add the user and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                nutrition1.userId = retrievedUser.id
                val addNutritionResponse = addNutrition(nutrition1)
                assertEquals(201, addNutritionResponse.status)

                //Assert - retrieve the added Nutrition from the database and verify return code
                val retrievedNutrition: Nutrition = jsonToObject(addNutritionResponse.body.toString())
                val retrieveNutritionResponse = retrieveNutritionById(retrievedNutrition.id)
                assertEquals(200, retrieveNutritionResponse.status)

                //Assert - verify the contents of the retrieved Nutrition
                assertEquals(nutrition1.foodName, retrievedNutrition.foodName)
                assertEquals(nutrition1.partOfDay, retrievedNutrition.partOfDay)
                assertEquals(nutrition1.calories, retrievedNutrition.calories)

                //After - restore the db to previous state by deleting the added Nutrition
                val deleteResponse = deleteNutrition(retrievedNutrition.id)
                assertEquals(204, deleteResponse.status)

                deleteUser(retrievedUser.id)
            }
        }

        @Nested
        inner class ReadNutritions {

            val localDateTime = LocalDateTime.now()
            val jodaDateTime = DateTime(localDateTime.toDateTime(), DateTimeZone.getDefault())
            val formattedDateTime = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss").print(jodaDateTime)

            @Test
            fun `get all nutritions from the database returns 200 or 404 response`() {
                val response = Unirest.get(origin + "/api/nutritions/").asString()
                if (response.status == 200) {
                    val retrievedNutritions: ArrayList<Nutrition> = jsonToObject(response.body.toString())
                    assertNotEquals(0, retrievedNutritions.size)
                } else {
                    assertEquals(404, response.status)
                }
            }

            @Test
            fun `get nutrition by id when nutrition does not exist returns 404 response`() {

                //Arrange - test data for Nutrition id
                val id = Integer.MIN_VALUE

                // Act - attempt to retrieve the non-existent Nutrition from the database
                val retrieveResponse = retrieveNutritionById(id)

                // Assert -  verify return code
                assertEquals(404, retrieveResponse.status)
            }

            @Test
            fun `get nutrition by user-id when nutrition does not exist returns 404 response`() {

                val id = 1001

                // Arrange & Act - attempt to retrieve the non-existent Nutrition from the database
                val retrieveResponse = retrieveNutritionByUser(id, DateTime("2020-01-01T00:00:00"), DateTime(formattedDateTime))

                // Assert -  verify return code
                assertEquals(404, retrieveResponse.status)
            }

            @Test
            fun `getting a nutrition by user-id when nutrition exists, returns a 200 response`() {

                //Arrange & Act & Assert

                val addUserResponse = addUser(user1)

                //    add the nutrition and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                nutrition1.userId = retrievedUser.id

                val addNutritionResponse = addNutrition(nutrition1)
                assertEquals(201, addNutritionResponse.status)

                val retrievedNutrition: Nutrition = jsonToObject(addNutritionResponse.body.toString())
                val retrieveNutritionResponse = retrieveNutritionByUser(retrievedNutrition.userId, DateTime("2020-01-01T00:00:00"), DateTime(formattedDateTime))
                if (retrieveNutritionResponse.status == 200) {
                    val retrievedNutritions: ArrayList<Nutrition> = jsonToObject(retrieveNutritionResponse.body.toString())
                    assertNotEquals(0, retrievedNutritions.size)
                } else {
                    assertEquals(404, retrieveNutritionResponse.status)
                }

                //After - restore the db to previous state by deleting the added Nutrition
                val deleteResponse = deleteNutrition(retrievedNutrition.id)
                assertEquals(204, deleteResponse.status)

                deleteUser(retrievedUser.id)
            }

            @Test
            fun `Read nutrition values from external resource, returns a 200 response`() {
                val response = Unirest.get(origin + "/api/nutrition/search?searchQuery=bagel").asString()
                if (response.status == 200) {
                    val retrievedNutrient: Nutrient = jsonToObject(response.body.toString())
                    //print(retrievedNutrient)
                    assertNotEquals(0, retrievedNutrient.enerc_KCAL)
                }
                else
                    assertEquals(404, response.status)
            }
        }

        @Nested
        inner class UpdateNutritions {
            @Test
            fun `updating a nutrition when it exists, returns a 204 response`() {

                val addUserResponse = addUser(user1)

                //    add the user and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                nutrition1.userId = retrievedUser.id
                val addNutritionResponse = addNutrition(nutrition1)
                assertEquals(201, addNutritionResponse.status)

                //Assert - retrieve the nutrition from the database and verify return code
                val retrievedNutrition: Nutrition = jsonToObject(addNutritionResponse.body.toString())
                val retrieveNutritionResponse = retrieveNutritionById(retrievedNutrition.id)
                assertEquals(200, retrieveNutritionResponse.status)

                nutrition1.id = retrievedNutrition.id
                nutrition1.partOfDay = "Breakfast"
                nutrition1.foodName = "Pancakes"

                val updatedNutritionResponse = updateNutrition(nutrition1)
                assertEquals(204, updatedNutritionResponse.status)
                //Act & Assert - retrieve updated Nutrition and assert details are correct

                val retrieveUpdatedNutrition = retrieveNutritionById(nutrition1.id)
                val updatedNutritionValidation: Nutrition = jsonToObject(retrieveUpdatedNutrition.body.toString())
                assertEquals("Breakfast", updatedNutritionValidation.partOfDay)
                assertEquals("Pancakes", updatedNutritionValidation.foodName)

                //After - restore the db to previous state by deleting the added Nutrition
                val deleteResponse = deleteNutrition(retrievedNutrition.id)
                assertEquals(204, deleteResponse.status)

                deleteUser(retrievedUser.id)
            }

            @Test
            fun `updating a nutrition when it doesn't exist, returns a 404 response`() {

                //Act & Assert - attempt to update the nutrition that doesn't exist
                val unknownNutrition: Nutrition = nutrition1
                assertEquals(404, updateNutrition(unknownNutrition).status)
            }
        }

        @Nested
        inner class DeleteNutritions {
            @Test
            fun `deleting a nutrition when it doesn't exist, returns a 404 response`() {
                //Act & Assert - attempt to delete a nutrition that doesn't exist
                assertEquals(404, deleteNutrition(-1).status)
            }

            @Test
            fun `deleting a nutrition when it exists, returns a 204 response`() {

                val addUserResponse = addUser(user1)

                //    add the user and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                nutrition1.userId = retrievedUser.id
                val addNutritionResponse = addNutrition(nutrition1)
                assertEquals(201, addNutritionResponse.status)

                //Assert - retrieve the added user from the database and verify return code
                val retrievedNutrition: Nutrition = jsonToObject(addNutritionResponse.body.toString())
                val retrieveNutritionResponse = retrieveNutritionById(retrievedNutrition.id)
                assertEquals(200, retrieveNutritionResponse.status)

                //Act & Assert - delete the added user and assert a 204 is returned
                assertEquals(204, deleteNutrition(retrievedNutrition.id).status)

                //Act & Assert - attempt to retrieve the deleted user --> 404 response
                assertEquals(404, retrieveNutritionById(retrievedNutrition.id).status)

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

        //helper function to add a test nutrition to the database
        private fun addNutrition(nutrition: Nutrition): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/nutritions")
                .body("{" +
                        "\"partOfDay\":\"${nutrition.partOfDay}\"," +
                        "\"foodName\":\"${nutrition.foodName}\"," +
                        " \"calories\":\"${nutrition.calories}\", " +
                        "\"cholesterol\":\"${nutrition.cholesterol}\", " +
                        "\"protein\":\"${nutrition.protein}\", " +
                        "\"fat\":\"${nutrition.fat}\", " +
                        "\"fiber\":\"${nutrition.fiber}\", " +
                        "\"macroDate\":\"${nutrition.macroDate}\", " +
                        "\"userId\":\"${nutrition.userId}\"" +
                        "}")
                .asJson()
        }

        //helper function to delete a test nutrition from the database
        private fun deleteNutrition(id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/nutrition/${id}").asString()
        }

        //helper function to retrieve a test nutrition from the database by email
        private fun retrieveNutritionByUser(id: Int, startDate: DateTime, endDate: DateTime): HttpResponse<String> {
            return Unirest.get(origin + "/api/measurements/${id}?start-date=${startDate}&end-date=${endDate}").asString()
        }

        //helper function to retrieve a test nutrition from the database by id
        private fun retrieveNutritionById(id: Int): HttpResponse<String> {
            return Unirest.get(origin + "/api/nutrition/${id}").asString()
        }

        //helper function to add a test nutrition to the database
        private fun updateNutrition(nutrition: Nutrition): HttpResponse<JsonNode> {
            return Unirest.patch(origin + "/api/nutrition/${nutrition.id}")
                .body("{" +
                        "\"partOfDay\":\"${nutrition.partOfDay}\"," +
                        "\"foodName\":\"${nutrition.foodName}\"," +
                        " \"calories\":\"${nutrition.calories}\", " +
                        "\"cholesterol\":\"${nutrition.cholesterol}\", " +
                        "\"protein\":\"${nutrition.protein}\", " +
                        "\"fat\":\"${nutrition.fat}\", " +
                        "\"fiber\":\"${nutrition.fiber}\", " +
                        "\"macroDate\":\"${nutrition.macroDate}\", " +
                        "\"userId\":\"${nutrition.userId}\"" +
                        "}")
                .asJson()
        }
    }
}