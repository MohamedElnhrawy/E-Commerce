package com.gtera.data.model


import java.io.Serializable
import java.util.*

data class Promotion(
    var code: String?,
    var Amount: Double?,
    var exclude:String?,
    var min:Int?,
    var max:Int?,
    var start: Date?,
    var end: Date?


):Serializable{


    companion object{
        const val CODE = "code"
        const val AMOUNT = "amount"
        const val EXCLUDE = "exclude"
        const val MIN = "min"
        const val MAX = "max"
        const val START = "start"
        const val END = "end"
    }

    fun isValidDatePromotion(currentDate:Date) : Boolean{
        if (start != null && end != null){
            if ((currentDate.compareTo(start) >= 0 && currentDate.compareTo(end) <= 0 )) {
               // currentDate is after start     && currentDate is before start
                return true
            }
        }
        return false
    }

    fun isValidCartItemsCount(currentCount:Int) : Boolean{
        return if (min != null && max != null){
            currentCount >= min!! &&  currentCount <= max!!
        } else false
    }

}
