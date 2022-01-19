package com.gtera.ui.profile.messages

import android.content.Context
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Message
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.MessageStatusType
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class MessagesListItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = R.layout.messages_item_list_layout


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var isFavoriteCar = ObservableField(false)
    var message: Message? = null

    lateinit var context: Context
    var senderImageUrl = ObservableField("")
    var senderName = ObservableField("")
    var messageDate = ObservableField("")
    var messageBody = ObservableField("")
    var seen = ObservableField(false)
    var placeHolder: ObservableField<Int> = ObservableField(R.drawable.ic_profile_info_placeholder)


    constructor(
        message: Message?,
        context: Context
    ) : this() {
        this.context = context
        this.message = message
        this.seen.set(
            if(message?.messages?.size!! >0)
            message.messages.let {
                it[0]?.status?.equals(
                    MessageStatusType.SEEN.toString()
                        .toLowerCase(Locale.ROOT))
            }
        else true
        )
        setupMessage(message)
    }


    fun setupMessage(message: Message?) {
        this.message = message
        if (!Utilities.isNullString(message?.user?.profilePicture)) senderImageUrl.set(
            message?.user?.profilePicture
        )
        senderName.set(message?.user?.firstName + " " + message?.user?.lastName)
        messageDate.set(Utilities.getMessagesDate(message?.messages?.get(0)?.createdAt!!, context))
        messageBody.set(message.messages.get(0)?.message)

    }

}