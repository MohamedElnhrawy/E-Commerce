package com.gtera.ui.profile.messages

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.MessagesListLayoutBinding
import com.gtera.ui.helper.EmptyView

class MessagesListFragment :
    BaseFragment<MessagesListLayoutBinding, MessagesListViewModel>(),
    MessagesListNavigator {

    override val layoutId: Int
        get() = R.layout.messages_list_layout


    override val viewModelClass: Class<MessagesListViewModel>
        get() = MessagesListViewModel::class.java

    override fun setNavigator(viewModel: MessagesListViewModel?) {
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

        return EmptyView(R.drawable.ic_empty_messages,
            getString(R.string.str_messages_empty_message_header),
            getString(R.string.str_messages_empty_message_body),
            viewModel?.resourceProvider!!
        )
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_messages)



}