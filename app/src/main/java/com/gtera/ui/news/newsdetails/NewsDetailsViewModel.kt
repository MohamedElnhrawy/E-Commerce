package com.gtera.ui.news.newsdetails

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.New
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.NewsItemViewModel
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NewsDetailsViewModel @Inject constructor() : BaseViewModel<NewsDetailsNavigator>() {

    init {
        initRelatedNews()
    }

    var new_id = -1
    var news: New? = null
    var hasRelated = ObservableBoolean(false)
    var newsImage = ObservableField("")
    var newsTitle = ObservableField("  ")
    var newsDetails = ObservableField("")
    var newsDate = ObservableField("")
    var relatedNewsListAdapter: BaseAdapter<NewsItemViewModel>? = null
    var relateNewsOrientation: ListOrientation? = ListOrientation.ORIENTATION_HORIZONTAL

    var shareNews =
        object : ClickListener {
            override fun onViewClicked() {
                launchShareIntent(
                    news?.title,
                    news?.shareLink
                )
            }
        }
    var relatedNewsList: ArrayList<NewsItemViewModel> =
        ArrayList<NewsItemViewModel>()

    override fun onViewCreated() {
        super.onViewCreated()
        setToolbarSecondaryActionEnabled(true)
        setToolbarSecondaryActionField(R.drawable.ic_tool_bar_share, shareNews)

        if (dataExtras?.containsKey(APPConstants.EXTRAS_KEY_NEWS)!!) {
            news = dataExtras?.getSerializable(APPConstants.EXTRAS_KEY_NEWS) as New
            viewNews(news!!)
            news!!.id?.let { getNewsList(it) }

        } else {
            new_id = dataExtras?.getInt(APPConstants.EXTRAS_KEY_NEW_ID)!!
            getNewsById(new_id)
            getNewsList(new_id)
        }


    }

    private fun viewNews(news: New) {

        newsImage.set(news.image?.get(0))
        newsTitle.set(news.title)
        newsDetails.set(news.description)

        news.date?.let { date ->
            newsDate.set(
                Utilities.formatDateTime(
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(
                        date
                    )
                )
            )
        }

    }


    private fun getNewsList(id: Int) {

        appRepository.getRelatedNews(
            id,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<New?>?>?> {

                override fun onSuccess(result: BaseResponse<ArrayList<New?>?>?) {
                    hasRelated.set(result?.data!!.size > 0)
                    addRelatedNews(result.data!!)
                }

                override fun onError(throwable: ErrorDetails?) {

                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                if (news != null)
                                    getNewsList(news!!.id!!)
                                else
                                    getNewsList(new_id)
                            }
                        })
                }
            }
        )
    }

    private fun initRelatedNews() {
        relatedNewsListAdapter = BaseAdapter(relatedNewsList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {


                val extras = Bundle()
                extras.putSerializable(APPConstants.EXTRAS_KEY_NEWS, relatedNewsList[0].news)
                openView(AppScreenRoute.NEWS_DETAILS, extras)
            }
        })


    }

    fun addRelatedNews(List: List<New?>) {
        if (Utilities.isNullList(List)) return

        this.relatedNewsList.clear()
        for (news in List) {
            val newsItemViewModel = NewsItemViewModel(
                news, resourceProvider, ListOrientation.ORIENTATION_HORIZONTAL
            )
            this.relatedNewsList.add(newsItemViewModel)
        }
        relatedNewsListAdapter?.updateList(relatedNewsList)
        relatedNewsListAdapter?.notifyDataSetChanged()
    }

    private fun getNewsById(id: Int) {
        showLoading(true)
        appRepository.getNewById(id, lifecycleOwner, object : APICommunicatorListener<BaseResponse<New?>> {
            override fun onSuccess(result: BaseResponse<New?>) {
                hideLoading()
                result.data?.let { viewNews(it) }
            }

            override fun onError(throwable: ErrorDetails?) {
                hideLoading()
                showErrorView(
                    throwable?.statusCode!!,
                    throwable.errorMsg,
                    object : ClickListener {
                        override fun onViewClicked() {
                            getNewsById(id)
                        }
                    })
            }

        })
    }

}