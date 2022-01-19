package com.gtera.ui.profile.messages

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Message
import com.gtera.data.model.User
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute.MESSAGE_DETAILS
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import javax.inject.Inject

class MessagesListViewModel @Inject constructor() : BaseViewModel<MessagesListNavigator>() {

    init {
        initMessagesView()
    }

    var isRefreshing = ObservableBoolean(false)

    var loggedUser: User? = null

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var messagesListOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL

    var messagesListAdapter: BaseAdapter<MessagesListItemViewModel>? = null

    // adapter's lists
    protected var messagesList: ArrayList<MessagesListItemViewModel> =
        ArrayList<MessagesListItemViewModel>()


    override fun onViewCreated() {
        super.onViewCreated()

        appRepository.getLoggedInUser(
            lifecycleOwner,
            object : APICommunicatorListener<User?> {
                override fun onSuccess(result: User?) {


                    loggedUser = result

                }

                override fun onError(throwable: ErrorDetails?) {

                    showErrorBanner(resourceProvider.getString(R.string.something_went_wrong))
                }
            })

        getMessages()


    }

    private fun getMessages() {

        showLoading(false)
        appRepository.getMyMessages(
            lifecycleOwner,
            object :
                APICommunicatorListener<BaseResponse<ArrayList<Message?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Message?>?>?) {
                    hideLoading()
                    hasData(result?.data?.size!!)
                    addMessages(result.data!!)
                    isRefreshing.set(false)
                }

                override fun onError(throwable: ErrorDetails?) {
                    isRefreshing.set(false)
                    hideLoading()
                    hasData(0)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getMessages()
                            }
                        })
                }

            })
    }


    protected fun initMessagesView() {

        messagesListAdapter = BaseAdapter(messagesList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {


                val extras = Bundle()
                extras.putInt(APPConstants.MESSAGE_ID, messagesList.get(position).message?.id!!)
                extras.putString(APPConstants.MESSAGE_USER_IMAGE, loggedUser?.profilePicture)
                openView(MESSAGE_DETAILS, extras)


            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getMessages()
        }
    }

    protected fun addMessages(messages: List<Message?>) {
        if (Utilities.isNullList(messages)) return

        this.messagesList.clear()
        for (message in messages) {

            val messagesListItemViewModel =
                MessagesListItemViewModel(
                    message!!, context
                )
            this.messagesList.add(messagesListItemViewModel)
        }
        messagesListAdapter?.updateList(messagesList)
        messagesListAdapter?.notifyDataSetChanged()
    }


}