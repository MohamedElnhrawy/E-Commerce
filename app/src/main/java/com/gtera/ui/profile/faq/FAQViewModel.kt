package com.gtera.ui.profile.faq

import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.FAQ
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.common.ViewHolderInterface
import javax.inject.Inject

class FAQViewModel @Inject constructor() : BaseViewModel<FAQNavigator>(),
    ViewHolderInterface {


    var FAQAdapter: BaseAdapter<FAQItemViewModel>? = null
    private val FAQlist =
        ArrayList<FAQItemViewModel>()

    init {
        FAQAdapter = BaseAdapter(FAQlist, this)
    }

    constructor(activityClass: Class<*>?) : this() {
        FAQAdapter = BaseAdapter(FAQlist, this)
    }

    override fun onViewCreated() {
        super.onViewCreated()

        getItemsList()
    }

    override fun onViewRecreated() {
        super.onViewRecreated()
        getItemsList()
    }


    private fun getItemsList() {


        showLoading(false)
        appRepository.getFAQ(
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<FAQ?>?>?> {

                override fun onSuccess(result: BaseResponse<ArrayList<FAQ?>?>?) {
                    hideLoading()
                    drawFAQList(result?.data)

                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    showErrorBanner(throwable?.errorMsg)

                }


            })


    }

    private fun drawFAQList(faqList: ArrayList<FAQ?>?) {

        for (faq in faqList!!) {

            var faq = FAQItemViewModel(faq)
            FAQlist.add(faq)
        }

        FAQAdapter!!.notifyDataSetChanged()
    }

    override fun onViewClicked(position: Int, id: Int) {
    }

}