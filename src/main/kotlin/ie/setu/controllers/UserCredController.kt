package ie.setu.controllers
import ie.setu.domain.UserCred
import ie.setu.domain.repository.UserCredDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object UserCredController {

    private val userCredDao = UserCredDAO()

    fun getUserCredByUserId(ctx: Context) {
        val userCred = userCredDao.findByUserId(ctx.pathParam("user-id").toInt())
        if (userCred != null) {
            ctx.json(userCred)
            ctx.status(200)
        }
        else {
            ctx.status(404)
        }
    }

    fun addUserCred(ctx: Context) {
        val userCred : UserCred = jsonToObject(ctx.body())
        val userCredId = userCredDao.save(userCred)
        if (userCredId != null) {
            userCred.id = userCredId
            ctx.json(userCredId)
            ctx.status(201)
        }
    }

    fun deleteUserCred(ctx: Context) {
        if (userCredDao.delete(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateUserCred(ctx: Context) {
        val foundUser : UserCred = jsonToObject(ctx.body())
        if ((userCredDao.update(userId = ctx.pathParam("user-id").toInt(), userCred=foundUser)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}