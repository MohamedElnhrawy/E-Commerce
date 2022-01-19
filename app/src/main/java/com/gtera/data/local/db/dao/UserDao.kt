package com.gtera.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.gtera.data.model.User
import io.reactivex.Single

@Dao
interface UserDao : BaseDao<User?> {
    @get:Query("SELECT * FROM users LIMIT 1")
    val loggedInUser: Single<User?>?
}