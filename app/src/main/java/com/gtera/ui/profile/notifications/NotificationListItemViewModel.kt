package com.gtera.ui.profile.notifications

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Notification
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.utils.Utilities
import javax.inject.Inject

class NotificationListItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = R.layout.notifications_item_list_layout


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var isFavoriteCar = ObservableField(false)
    var notification: Notification? = null

    var notificationImageUrl = ObservableField("")
    var notificationTitle = ObservableField("")
    var notificationDate = ObservableField("")
    var read = ObservableField(false)


    constructor(
        notifications: Notification?
    ) : this() {
        this.notification = notifications
        this.read.set(notifications?.readAt != null)
        setupNotification(notifications)
    }


    fun setupNotification(notifications: Notification?) {
        this.notification = notification
        if (!Utilities.isNullString(notification?.data?.image)) notificationImageUrl.set(
            notification?.data?.image
        )
        notificationTitle.set(notification?.data?.title)
        notificationDate.set(notification?.data?.time)

    }
}