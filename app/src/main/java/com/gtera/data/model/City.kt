package com.gtera.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "city")
data class City(

	@Ignore
	@field:SerializedName("governorate")
	val governorate: Governorate? = null,
	@ColumnInfo(name = "name")
	@field:SerializedName("name")
	val name: String? = null,
	@PrimaryKey
	@SerializedName("id")
	@ColumnInfo(name = "id")
	val id: Int? = null,

	@ColumnInfo(name = "governorate_id")
	@field:SerializedName("governorate_id")
	val governorateId: Int? = null
)
@Entity(tableName = "governorate")
data class Governorate(

	@ColumnInfo(name = "name")
	@field:SerializedName("name")
	val name: String? = null,

	@PrimaryKey
	@SerializedName("id")
	@ColumnInfo(name = "id")
	val id: Int? = null
)
