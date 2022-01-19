package com.gtera.ui.usedcardetails

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.viewpager.widget.ViewPager
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.*
import com.gtera.data.model.requests.AddRemoveToCompareRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.cardetials.attributes.CarAttributesItemListViewModel
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.dialog.inputdialog.InputDialogNavigator
import com.gtera.ui.usedcardetails.attributes.CarSpecsItemVM
import com.gtera.ui.utils.BindingUtils
import com.gtera.utils.APPConstants.EXTRAS_KEY_CAR_ID
import com.gtera.utils.APPConstants.IS_MY_CAR
import com.gtera.utils.Utilities
import com.synnapps.carouselview.ImageClickListener
import com.synnapps.carouselview.ImageListener
import javax.inject.Inject

class UsedCarDetailsViewModel @Inject constructor() : BaseViewModel<UsedCarDetailsNavigator>() {

    //swipe
    var isRefreshing = ObservableBoolean(false)

    //slider
    var autoSlide = ObservableBoolean(false)
    var isMyCar = ObservableBoolean(false)
    var pageCount = ObservableField(0)
    var currentPage = ObservableField(0)
    var imageListener: ImageListener? = null
    var imageClickListener: ImageClickListener? = null
    var onPageChangeListener: ViewPager.OnPageChangeListener? = null

    // car data
    var car: Car? = null
    var carName = ObservableField("")
    var adDate = ObservableField("")
    var carPrice = ObservableField("")
    var carId: Int? = null
    private val list = ArrayList<CarSpecsItemVM>()
    var adapter: BaseAdapter<CarSpecsItemVM> =
        BaseAdapter<CarSpecsItemVM>(list, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {

            }

        })
    protected var attributesCategoriesList: ArrayList<CarAttributesItemListViewModel> =
        ArrayList<CarAttributesItemListViewModel>()

    var attributesCategoriesListAdapter: BaseAdapter<CarAttributesItemListViewModel> =
        BaseAdapter(attributesCategoriesList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {

                if (id == R.id.iv_budgets_arrow) {
                    attributesCategoriesList.get(position).isexpanded.get()?.not()
                }

            }
        })




    //user data
    var placeHolder: ObservableField<Int> = ObservableField(R.drawable.ic_profile_info_placeholder)
    var carOwner = ObservableField<User>()

    var addCarToCompare =
        object : ClickListener {
            override fun onViewClicked() {
                addToCompare()
            }
        }


    override fun onViewCreated() {
        super.onViewCreated()
        setToolbarSecondaryActionEnabled(true)
        setToolbarSecondaryActionField(R.drawable.ic_compare, addCarToCompare)
        carId = dataExtras?.getInt(EXTRAS_KEY_CAR_ID)
        autoSlide.set(resourceProvider.getBoolean(R.bool.used_car_details_auto_play_slider))
        isMyCar.set(dataExtras?.getBoolean(IS_MY_CAR)!!)
        getCar(carId!!)


    }


    private fun setupCarousel(items: ArrayList<String?>?) {
        pageCount.set(items?.size)
        imageListener = ImageListener { position, imageView ->
            items?.get(position)?.let {
                BindingUtils.setImageUrl(
                    imageView,
                    it,
                    0,
                   null
                )
            }
        }
        imageClickListener = ImageClickListener {

        }

        onPageChangeListener = object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                currentPage.set(position + 1)

            }

            override fun onPageSelected(position: Int) {
            }



            override fun onPageScrollStateChanged(state: Int) {

            }
        }

        imageClickListener = ImageClickListener {

        }
    }

    private fun getCar(carId: Int) {

        appRepository.getCar(
            carId,
            lifecycleOwner,
            object :
                APICommunicatorListener<BaseResponse<Car?>?> {
                override fun onSuccess(result: BaseResponse<Car?>?) {
                    hideLoading()

                    result?.data?.let {
                        car = it
                        setupCarousel(it.images)
                        inflateData(it)
                    }
                    isRefreshing.set(false)
                }

                override fun onError(throwable: ErrorDetails?) {
                    isRefreshing.set(false)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getCar(carId)
                            }
                        })
                }


            })
    }




    private fun addToCompare() {
        var carsIds = ArrayList<Int>()
        carsIds.add(carId!!)
        var addToCompareRequest = AddRemoveToCompareRequest()
        addToCompareRequest.ids = carsIds
        appRepository.addCarCompare(
            addToCompareRequest,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<Compare?>?> {

                override fun onSuccess(result: BaseResponse<Compare?>?) {

                    showSuccessBanner(getStringResource(R.string.str_car_added_to_compare_successfully))
                }

                override fun onError(throwable: ErrorDetails?) {
                    showErrorBanner(throwable?.errorMsg)
                }
            })
    }

    private fun inflateData(car: Car) {


        carName.set(car.name + "  " + car.brand?.name + "  " + car.manufactureYear)
        car.carAd?.let {
            carPrice.set(
               it.price.toString() + " " + getStringResource(
                    R.string.str_egp
                )
            )


        }

        adDate.set(car.createdAt?.let { it1 -> Utilities.reFormatDate(it1 ?: "") })
        car.user?.let {
            carOwner.set(it)
        }


        car.manufactureYear?.let {
            list.add(CarSpecsItemVM(getStringResource(R.string.str_manufacture_year), it))
        }

        car.motorCc?.let {
            list.add(
                CarSpecsItemVM(
                    getStringResource(R.string.str_engine_capacity),
                    it + " " + getStringResource(R.string.str_cc)
                )
            )

        }

        // not found in response
        car.mileage?.let {
            list.add(CarSpecsItemVM(getStringResource(R.string.str_engine_capacity), it))

        }

        // not found in response
        car.license?.let {
            list.add(CarSpecsItemVM(getStringResource(R.string.str_driving_license), it))

        }

        car.numberOfDoors?.let {
            list.add(CarSpecsItemVM(getStringResource(R.string.str_num_of_doors), it.toInt()))

        }

        car.colors?.let {
            list.add(
                CarSpecsItemVM(
                    getStringResource(R.string.str_color),
                    it[0],
                    true,
                    isMyCar.get().not()
                )
            )

        }

        adapter.notifyDataSetChanged()


        car.attributes?.let { addCarAttributesCategories(it) }



    }

    fun startCall(){
        carOwner.get()?.let {
            launchCallIntent(it.phoneNumber)

        }
    }
    protected fun addCarAttributesCategories(List: List<AttributeGroup?>) {
        if (Utilities.isNullList(List)) return

        this.attributesCategoriesList.clear()

        for (attributeCategory in List) {
            val carAttributesItemListViewModel = CarAttributesItemListViewModel(
                attributeCategory!!, false
            )
            this.attributesCategoriesList.add(carAttributesItemListViewModel)
        }
        attributesCategoriesListAdapter.notifyDataSetChanged()
    }

    fun sendMessage() {

        showInputDialog(
            resourceProvider.getString(R.string.str_input_dialog_message),
            resourceProvider.getString(R.string.str_input_dialog_send_message),
            getShowInputDialogConfirmationNavigator()!!
        )
    }

    fun makeOffer() {

    }

    fun onShareClicked() {
        launchShareIntent(car?.name, car?.shareLink)
    }

    private fun getShowInputDialogConfirmationNavigator(): InputDialogNavigator? {

        return object : InputDialogNavigator {
            override fun onSendButtonClicked(inputText: String) {


                appRepository.getLoggedInUser(
                    lifecycleOwner,
                    object : APICommunicatorListener<User?> {
                        override fun onSuccess(result: User?) {

                            sendMessage(result, inputText)

                        }

                        override fun onError(throwable: ErrorDetails?) {

                            showErrorBanner(resourceProvider.getString(R.string.something_went_wrong))
                        }
                    })


            }
        }
    }

    private fun sendMessage(result: User?, inputText: String) {

        var message = MessagesItem()
        message.userId = result?.id
        message.conversationId = car?.user?.id
        message.message = inputText

        appRepository.startConversation(
            message,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<Any?>?> {
                override fun onSuccess(result: BaseResponse<Any?>?) {
                    showSuccessBanner(resourceProvider.getString(R.string.str_input_dialog_message_sent_successfully))
                    dismissDialog(INPUT_DIALOG_TAG)
                }

                override fun onError(throwable: ErrorDetails?) {
                    showErrorBanner(throwable?.errorMsg)
                    dismissDialog(INPUT_DIALOG_TAG)
                }

            })

    }
}