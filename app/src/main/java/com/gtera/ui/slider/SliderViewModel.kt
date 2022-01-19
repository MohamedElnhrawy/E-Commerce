package com.gtera.ui.slider

import android.os.Handler
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModel
import com.gtera.R
import com.gtera.data.model.Slider
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.banner.BannerItem
import com.gtera.ui.common.ViewHolderInterface
import com.zhpan.bannerview.BannerViewPager.OnPageClickListener
import com.zhpan.bannerview.adapter.OnPageChangeListenerAdapter
import java.util.*
import javax.inject.Inject

class SliderViewModel @Inject constructor(val resourceProvider: ResourceProvider) : ViewModel() {


    private var clickListener: ViewHolderInterface? = null
    var smoothScrollPosition = ObservableInt(-1)
    var changePage = ObservableInt(0)
    var leftText = ObservableField("")
    var righttext = ObservableField("")
    var prevSelectedDot = 0
    var list: ObservableList<Slider> = ObservableArrayList()
    var dotsAdapter: BaseAdapter<IndicatorItemViewModel>? = null
    var dotsList = ArrayList<IndicatorItemViewModel>()

    var pageChangeListenerAdapter: OnPageChangeListenerAdapter =
        object : OnPageChangeListenerAdapter() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                dotsList[prevSelectedDot].isSelected.set(false)
                dotsList[position].isSelected.set(true)

                prevSelectedDot = position
                setSliderStep(position)
            }
        }

    //auto slide
    val speedScroll = 4000
    val handler = Handler()
    val runnable: Runnable = object : Runnable {
        var count = 0
        var flag = true
        override fun run() {
            if (count < sliderAdapter!!.itemCount) {
                if (count == sliderAdapter!!.itemCount - 1) {
                    flag = false
                } else if (count == 0) {
                    flag = true
                }
                if (flag) count++ else count = 0

            }
        }
    }

    //slider
    var sliderSelectedImage = ObservableField("")
    var sliderItemClickListener =
        OnPageClickListener { position: Int -> onSliderItemClick(position) }
    var sliderAdapter: BaseAdapter<BannerItem>? = null
    var bannerList = ArrayList<BannerItem>()
    private var sliderSelectedPos = 0
    var itemClickListener =
        View.OnClickListener { v: View? ->

            onSliderItemClick(sliderSelectedPos)

        }

    init {
        dotsAdapter = BaseAdapter(dotsList, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {

            }
        })
        sliderAdapter = BaseAdapter(bannerList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {
                sliderSelectedImage.set(bannerList[position].imgUrl.get())
                sliderSelectedPos = position
                onSliderItemClick(position)
            }

        })
    }


    fun initSliderItems(items: List<Slider?>?) {
        sliderSelectedImage.set("")
        list.clear()
        dotsList.clear()
        for (i in items!!.indices) dotsList.add(IndicatorItemViewModel(i == 0))
        prevSelectedDot = 0
        dotsAdapter!!.notifyDataSetChanged()
        list.addAll(items)

        bannerList.clear()
        for (s in items) {

            bannerList.add(BannerItem(s?.image!!, resourceProvider))

        }

        sliderSelectedImage.set(bannerList[0].imgUrl.get())
        handler.postDelayed(runnable, speedScroll.toLong())

        sliderAdapter!!.notifyDataSetChanged()
        setSliderStep(0)

    }

    private fun onSliderItemClick(position: Int) {
        if (clickListener != null) clickListener!!.onViewClicked(position, 0)
    }

    fun setClickListener(clickListener: ViewHolderInterface?) {
        this.clickListener = clickListener
    }

    fun onNextBannerClicked() {

        var selectedPosition: Int  =  0
        if(prevSelectedDot+1 < dotsList.size)
            selectedPosition = prevSelectedDot+1
        else
            selectedPosition = 0
        dotsList[prevSelectedDot].isSelected.set(false)
        dotsList[selectedPosition].isSelected.set(true)

        changePage.set(selectedPosition)
        prevSelectedDot = selectedPosition
        setSliderStep(selectedPosition)



    }

    fun onBannerPreviewClicked() {
    }


    fun onPrevBannerClicked() {

        var selectedPosition: Int  =  0
        if(prevSelectedDot > 0)
            selectedPosition = prevSelectedDot-1
        else
            selectedPosition = dotsList.size -1

        dotsList[prevSelectedDot].isSelected.set(false)
        dotsList[selectedPosition].isSelected.set(true)
        changePage.set(selectedPosition)
        prevSelectedDot = selectedPosition
        setSliderStep(selectedPosition)
    }

    fun setSliderStep (step:Int){

        smoothScrollPosition.set(step)
        handler.postDelayed(runnable, speedScroll.toLong())
        sliderSelectedImage.set(bannerList[step].imgUrl.get())
        leftText.set(list.get(step)?.title)
        righttext.set( if (list.get(step)?.price == null ) "" else resourceProvider.getString(R.string.str_egp, list.get(step)?.price))

    }

}