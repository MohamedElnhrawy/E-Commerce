package com.gtera.ui.profile.messages.details

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.MessagesDetailsLayoutBinding
import com.gtera.ui.helper.EmptyView

class MessageDetailsFragment :
    BaseFragment<MessagesDetailsLayoutBinding, MessageDetailsViewModel>(),
    MessageDetailsNavigator {

    override val layoutId: Int
        get() = R.layout.messages_details_layout


    override val viewModelClass: Class<MessageDetailsViewModel>
        get() = MessageDetailsViewModel::class.java

    override fun setNavigator(viewModel: MessageDetailsViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_messages_list_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 6
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true


    override fun screenEmptyView(): EmptyView {

        return EmptyView(
            R.drawable.ic_empty_messages,
            getString(R.string.str_messages_empty_message_header),
            getString(R.string.str_messages_empty_message_body),
            viewModel?.resourceProvider!!
        )
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_messages)


}