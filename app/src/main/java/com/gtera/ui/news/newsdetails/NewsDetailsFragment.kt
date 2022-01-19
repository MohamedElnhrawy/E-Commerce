package com.gtera.ui.news.newsdetails

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.NewsDetailsLayoutBinding

class NewsDetailsFragment :
    BaseFragment<NewsDetailsLayoutBinding, NewsDetailsViewModel>(),
    NewsDetailsNavigator {
    override val layoutId: Int
        get() = R.layout.news_details_layout

    override val viewModelClass: Class<NewsDetailsViewModel>
        get() = NewsDetailsViewModel::class.java

    override fun setNavigator(viewModel: NewsDetailsViewModel?) {
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
        get() = getString(R.string.str_latest_news)


}