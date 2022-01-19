package com.gtera.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gtera.data.model.CartProduct
import com.gtera.data.model.User
import io.reactivex.Single

@Dao
interface CartDao : BaseDao<CartProduct?> {

    @Query("SELECT * FROM cart")
    fun getAll(): LiveData<List<CartProduct?>?>?

    @Query("DELETE FROM cart")
    fun clearCart()
}