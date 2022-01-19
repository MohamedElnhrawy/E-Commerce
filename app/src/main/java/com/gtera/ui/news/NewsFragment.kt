package com.gtera.ui.news

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.NewsLayoutBinding

class NewsFragment :
    BaseFragment<NewsLayoutBinding, NewsViewModel>(),
    NewsNavigator {
    override val layoutId: Int
        get() = R.layout.news_layout

    override val viewModelClass: Class<NewsViewModel>
         get() = NewsViewModel::class.java

    override fun setNavigator(viewModel: NewsViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }


    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true



    override val toolbarTitle: String?
        get() = getString(R.string.str_home_listing_news_title)


    override fun shimmerLoadingCount(): Int {
        return 10
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_notification_list_layout
    }
}