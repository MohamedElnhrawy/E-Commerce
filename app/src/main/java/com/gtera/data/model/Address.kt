package com.gtera.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
public final class Address {

    @PrimaryKey
    @SerializedName("streetNo")
    @ColumnInfo(name = "streetNo")
    @NonNull
    var streetNo :Int? = null
}