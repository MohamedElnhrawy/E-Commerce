package com.gtera.service

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseMessageHandling : FirebaseMessagingService() {
//    private val CHANNEL_ID: String
//    private val CHANNEL_NAME: String
//    private val CHANNEL_DESC: String
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        sendRegistrationToServer(s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

    }

    private fun handelMessageNotification(data: Map<String, String>) {
    }

//    private val pendingIntent: PendingIntent


    private fun getPendingIntent(notifyIntent: Intent): PendingIntent { // Set the Activity to start in a new, empty task
        notifyIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        // Create the PendingIntent
        return PendingIntent.getActivity(
            this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun buildNotification(
        title: String?, message: String?, url: String?,
        pendingIntent: PendingIntent
    ) {

    }

    private fun showNotification(notificationBuilder: NotificationCompat.Builder) {

    }

    private fun notifyActivity(
        title: String?,
        body: String?,
        productId: String?
    ) {

    }

//    override fun onDeletedMessages() {
//        super.onDeletedMessages()
//    }

    private fun sendRegistrationToServer(refreshedToken: String) {

    }

    companion object {
        const val BROADCAST_ACTION = "broadcast_action"
        const val NOTIFICATION_DATA_KEY = "data_key"
    }

    init {

    }
}