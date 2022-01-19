package com.gtera.data.model.requests

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddRemoveToCompareRequest(
    @field:SerializedName("ids")
    var ids: ArrayList<Int>? = null
) : Serializable