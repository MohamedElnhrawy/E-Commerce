package com.gtera.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gtera.data.local.db.converter.Converter
import com.gtera.data.local.db.dao.CartDao
import com.gtera.data.local.db.dao.UserDao
import com.gtera.data.model.CartProduct
import com.gtera.data.model.User

@Database(
    entities = [User::class,CartProduct::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao
}