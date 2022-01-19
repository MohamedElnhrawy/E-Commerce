package com.gtera.data.interfaces

import android.location.Location

interface LocationListener {
    fun currentLocation(location: Location)
}
