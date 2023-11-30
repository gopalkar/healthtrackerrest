package ie.setu.domain.repository

import ie.setu.domain.UserCred
import ie.setu.domain.db.UserCreds
import ie.setu.utils.mapToUserCred
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserCredDAO {

    fun save(userCred: UserCred) : Int?{
        return transaction {
            UserCreds.insert {
                it[password] = userCred.password
                it[userId] = userCred.userId
            } get UserCreds.id
        }
    }

    fun findByUserId(userId: Int) :UserCred?{
        return transaction {
            UserCreds.select() {
                UserCreds.userId eq userId}
                .map{mapToUserCred(it)}
                .firstOrNull()
        }
    }

    fun delete(userId: Int):Int{
        return transaction{
            UserCreds.deleteWhere{
                UserCreds.userId eq userId
            }
        }
    }

    fun update(userId: Int, userCred: UserCred): Int{
        return transaction {
            UserCreds.update ({
                UserCreds.userId eq userId}) {
                it[password] = userCred.password
            }
        }
    }
}