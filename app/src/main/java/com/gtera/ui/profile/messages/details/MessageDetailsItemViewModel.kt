package com.gtera.ui.profile.messages.details

import android.content.Context
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.MessagesItem
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.MessageStatusType
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class MessageDetailsItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = if (messagesItem?.type.equals(APPConstants.MESSAGE_TO)!!) R.layout.messages_conversation_from_item_list_layout else R.layout.messages_conversation_to_item_list_layout


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var messagesItem: MessagesItem? = null

    var userImage: String? = null
    var senderImage: String? = null
    lateinit var context: Context
    var senderImageUrl = ObservableField("")
    var messageDate = ObservableField("")
    var messageBody = ObservableField("")
    var messageImage = ObservableField("")
    var seen = ObservableField(false)
    var placeHolder: ObservableField<Int> = ObservableField(R.drawable.ic_profile_info_placeholder)


    constructor(
        messagesItem: MessagesItem?,
        context: Context,
        userImage: String?,
        senderImage: String?
    ) : this() {
        this.context = context
        this.messagesItem = messagesItem
        this.userImage = userImage
        this.senderImage = senderImage
        this.seen.set(
            messagesItem?.status?.equals(
                MessageStatusType.SEEN.toString()
                    .toLowerCase(Locale.ROOT)
            )
        )
        setupMessage(messagesItem)
    }


    fun setupMessage(messagesItem: MessagesItem?) {

        if (messagesItem?.type?.equals(APPConstants.MESSAGE_TO)!!) senderImageUrl.set(senderImage)
        else
            senderImageUrl.set(userImage)

        messageDate.set(
            Utilities.getMessagesDate(
                messagesItem.updatedAt,
                context
            )
        )
        messageBody.set(messagesItem.message)
        messageImage.set(messagesItem.image)

    }

}