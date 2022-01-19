package com.gtera.ui.profile.notifications

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.NotificationListLayoutBinding
import com.gtera.ui.helper.EmptyView

class NotificationListFragment :
    BaseFragment<NotificationListLayoutBinding, NotificationListViewModel>(),
    NotificationListNavigator {

    override val layoutId: Int
        get() = R.layout.notification_list_layout


    override val viewModelClass: Class<NotificationListViewModel>
        get() = NotificationListViewModel::class.java

    override fun setNavigator(viewModel: NotificationListViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_notification_list_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 6
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true


    override fun screenEmptyView(): EmptyView {

        return EmptyView(R.drawable.ic_empty_notification,
            getString(R.string.str_notificatios_empty_notificatios_header),
            getString(R.string.str_notificatios_empty_notificatios_body),
            viewModel?.resourceProvider!!
        )
    }


    override val toolbarTitle: String?
        get() = getString(R.string.str_notificatios)




}