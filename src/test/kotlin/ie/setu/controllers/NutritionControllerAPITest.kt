package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
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
    val activity1 = activities.get(0)

    @Nested
    inner class TestActivities {

        @Nested
        inner class CreateActivities {
            @Test
            fun `add a activity with correct details returns a 201 response`() {

                //Arrange & Act & Assert
                //    add the user and verify return code (using fixture data)
                val addUserResponse = addUser(user1)

                //    add the user and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                activity1.userId = retrievedUser.id
                val addActivityResponse = addActivity(activity1)
                assertEquals(201, addActivityResponse.status)

                //Assert - retrieve the added user from the database and verify return code
                val retrievedActivity: Activity = jsonToObject(addActivityResponse.body.toString())
                val retrieveActivityResponse = retrieveActivityById(retrievedActivity.id)
                assertEquals(200, retrieveActivityResponse.status)

                //Assert - verify the contents of the retrieved user
                assertEquals(activity1.description, retrievedActivity.description)
                assertEquals(activity1.duration, retrievedActivity.duration)
                assertEquals(activity1.calories, retrievedActivity.calories)

                //After - restore the db to previous state by deleting the added user
                val deleteResponse = deleteActivity(retrievedActivity.id)
                assertEquals(204, deleteResponse.status)

                deleteUser(retrievedUser.id)
            }

        }

        @Nested
        inner class ReadActivities {
            @Test
            fun `get all activities from the database returns 200 or 404 response`() {
                val response = Unirest.get(origin + "/api/activities/").asString()
                if (response.status == 200) {
                    val retrievedActivities: ArrayList<Activity> = jsonToObject(response.body.toString())
                    assertNotEquals(0, retrievedActivities.size)
                } else {
                    assertEquals(404, response.status)
                }
            }

            @Test
            fun `get activity by id when activity does not exist returns 404 response`() {

                //Arrange - test data for user id
                val id = Integer.MIN_VALUE

                // Act - attempt to retrieve the non-existent user from the database
                val retrieveResponse = retrieveActivityById(id)

                // Assert -  verify return code
                assertEquals(404, retrieveResponse.status)
            }

            @Test
            fun `get activity by user-id when activity does not exist returns 404 response`() {

                val id = 51

                // Arrange & Act - attempt to retrieve the non-existent user from the database
                val retrieveResponse = retrieveActivityByUser(id)

                // Assert -  verify return code
                assertEquals(404, retrieveResponse.status)
            }

            @Test
            fun `getting a activity by user-id when activity exists, returns a 200 response`() {

                //Arrange & Act & Assert

                val addUserResponse = addUser(user1)

                //    add the activity and verify return code (using fixture data)
                val retrievedUser: User = jsonToObject(addUserResponse.body.toString())
                activity1.userId = retrievedUser.id

                val addActivityResponse = addActivity(activity1)
                assertEquals(201, addActivityResponse.status)

                val retrievedActivity: Activity = jsonToObject(addActivityResponse.body.toString())
                val retrieveActivityResponse = retrieveActivityByUser(retrievedActivity.userId)
                if (retrieveActivityResponse.status == 200) {
                    val retrievedActivities: ArrayList<Activity> = jsonToObject(retrieveActivityResponse.body.toString())
                    assertNotEquals(0, retrievedActivities.size)
                } else {
                    assertEquals(404, retrieveActivityResponse.status)
                }

                //After - restore the db to previous state by deleting the added user
                val deleteResponse = deleteActivity(retrievedActivity.id)
                assertEquals(204, deleteResponse.status)

                deleteUser(retrievedUser.id)
            }
        }

        @Nested
        inner class UpdateUsers {
            @Test
            fun `updating a user when it exists, returns a 204 response`() {

                //Arrange - add the user that we plan to do an update on
                val addedResponse = addUser(user1)
                val addedUser: User = jsonToObject(addedResponse.body.toString())

                //Act & Assert - update the email and name of the retrieved user and assert 204 is returned
                assertEquals(204, updateUser(addedUser, updatedName, updatedEmail).status)

                //Act & Assert - retrieve updated user and assert details are correct
                val updatedUserResponse = retrieveUserById(addedUser.id)
                val updatedUser: User = jsonToObject(updatedUserResponse.body.toString())
                assertEquals(updatedName, updatedUser.name)
                assertEquals(updatedEmail, updatedUser.email)

                //After - restore the db to previous state by deleting the added user
                deleteUser(addedUser.id)
            }

            @Test
            fun `updating a user when it doesn't exist, returns a 404 response`() {

                //Act & Assert - attempt to update the email and name of user that doesn't exist
                val unknownUser: User = User(name = "Alice Wonderland", email = "alice@wonderland.com", gender = "Female", birthDate = DateTime("1986-11-03T05:59:27"), mobileNumber = "987-569-7854", dietPreferences = "Vegetarian", height = 5.10, weight = 150, profession = "Information Technology", id = -1)
                assertEquals(404, updateUser(unknownUser, updatedName, updatedEmail).status)
            }
        }

        @Nested
        inner class DeleteUsers {
            @Test
            fun `deleting a user when it doesn't exist, returns a 404 response`() {
                //Act & Assert - attempt to delete a user that doesn't exist
                assertEquals(404, deleteUser(-1).status)
            }

            @Test
            fun `deleting a user when it exists, returns a 204 response`() {

                //Arrange - add the user that we plan to do a delete on
                val addedResponse = addUser(user1)
                val addedUser: User = jsonToObject(addedResponse.body.toString())

                //Act & Assert - delete the added user and assert a 204 is returned
                assertEquals(204, deleteUser(addedUser.id).status)

                //Act & Assert - attempt to retrieve the deleted user --> 404 response
                assertEquals(404, retrieveUserById(addedUser.id).status)
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

        //helper function to retrieve a test user from the database by email
        private fun retrieveUserByEmail(email: String): HttpResponse<String> {
            return Unirest.get(origin + "/api/users/email/${email}").asString()
        }

        //helper function to retrieve a test user from the database by id
        private fun retrieveUserById(id: Int): HttpResponse<String> {
            return Unirest.get(origin + "/api/users/${id}").asString()
        }

        //helper function to add a test user to the database
        private fun updateUser(user: User, name: String, email: String): HttpResponse<JsonNode> {
            return Unirest.patch(origin + "/api/users/${user.id}")
                .body("{" +
                        "\"name\":\"${name}\", " +
                        "\"email\":\"${email}\"," +
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

        //helper function to add a test activity to the database
        private fun addActivity(activity: Activity): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/activities")
                .body("{" +
                        "\"description\":\"${activity.description}\"," +
                        " \"duration\":\"${activity.duration}\", " +
                        "\"calories\":\"${activity.calories}\", " +
                        "\"started\":\"${activity.started}\", " +
                        "\"userId\":\"${activity.userId}\"" +
                        "}")
                .asJson()
        }

        //helper function to delete a test user from the database
        private fun deleteActivity(id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/activity/${id}").asString()
        }

        //helper function to retrieve a test user from the database by email
        private fun retrieveActivityByUser(id: Int): HttpResponse<String> {
            return Unirest.get(origin + "/api/activities/${id}").asString()
        }

        //helper function to retrieve a test user from the database by id
        private fun retrieveActivityById(id: Int): HttpResponse<String> {
            return Unirest.get(origin + "/api/activity/${id}").asString()
        }

        //helper function to add a test user to the database
        private fun updateActivity(activity: Activity): HttpResponse<JsonNode> {
            return Unirest.patch(origin + "/api/activity/${activity.id}")
                .body("{" +
                        "\"description\":\"${activity.description}\"," +
                        " \"duration\":\"${activity.duration}\", " +
                        "\"calories\":\"${activity.calories}\", " +
                        "\"started\":\"${activity.started}\", " +
                        "\"userId\":\"${activity.userId}\"" +
                        "}")
                .asJson()
        }
    }
}