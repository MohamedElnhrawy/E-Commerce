package com.gtera.base

import android.Manifest
import android.app.DatePickerDialog.OnDateSetListener
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.gtera.R
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.local.LocaleHelper
import com.gtera.data.model.requests.FavoriteResponse
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.data.repository.AppRepository
import com.gtera.di.providers.ResourceProvider
import com.gtera.event.EmptyEvent
import com.gtera.event.ErrorData
import com.gtera.event.ErrorEvent
import com.gtera.event.LoadingEvent
import com.gtera.service.FireBaseMessageHandling
import com.gtera.ui.adapter.AutoCompleteAdapter
import com.gtera.ui.base.BaseAppRoute
import com.gtera.ui.base.BaseNavigator
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.common.ClickListener
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogNavigator
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogViewModel
import com.gtera.ui.dialog.confirmation.ConfirmationDialogNavigator
import com.gtera.ui.dialog.confirmation.ConfirmationDialogViewModel
import com.gtera.ui.dialog.defaultdialog.DefaultDialogNavigator
import com.gtera.ui.dialog.inputdialog.InputDialogNavigator
import com.gtera.ui.dialog.inputdialog.InputDialogViewModel
import com.gtera.ui.helper.EmptyView
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.*
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.gtera.data.model.CartProduct
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

abstract class BaseViewModel<N> :
    ViewModel {


    protected val ERROR_DIALOG_TAG = "error_dialog"
    protected val DEFAULT_DIALOG_TAG = "default_dialog"
    protected val NOTIFICATION_DIALOG_TAG = "notification_dialog"
    protected val CONFIRMATION_DIALOG_TAG = "confirm_dialog"
    protected val INPUT_DIALOG_TAG = "confirm_dialog"

    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var resourceProvider: ResourceProvider

    var isLoading = ObservableBoolean(false)
    protected var isLoadingDialogShown = false
    private val error = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()
    private var isViewFragment = false
    var isViewModelAttached = false
    private var isPausing = false

    private var disposable: CompositeDisposable?

    init {
        disposable = CompositeDisposable()

    }

    var searchAdapter: AutoCompleteAdapter? = null
    private var appEventsLogger: AppEventsLogger? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    var broadcastReceiver: BroadcastReceiver? = null
        private set

    var intentFilter: IntentFilter? = null

    protected var navigator: N? = null
        private set
    private var baseNavigator: BaseNavigator? = null
    protected var currentActivityClass: Class<*>? = null
    var lifecycleOwner: LifecycleOwner? = null
    private val loadingEvent: LoadingEvent = LoadingEvent()
    private val emptyEvent: EmptyEvent = EmptyEvent()
    private val errorEvent: ErrorEvent = ErrorEvent()
    private var searchSuggestions: MutableList<String>? = null
    private var appRoute: BaseAppRoute? = null

    //    public LiveData<ActivityLauncher> getLauncherLiveData() {
    //        return appRoute.getLauncherLiveData();
    //    }
    protected constructor() {}

    constructor(currentActivityClass: Class<*>?) {
        this.currentActivityClass = currentActivityClass
        initNotificationsHandler()
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
//        appEventsLogger = AppEventsLogger.newLogger()
    }

    fun setAppRoute(appRoute: BaseAppRoute?) {
        this.appRoute = appRoute
    }

    fun openView(route: AppScreenRoute?, extras: Bundle?) {
        appRoute?.appRoute(getBaseNavigator(), currentActivityClass, route, extras)
    }

    protected fun openViewForResult(
        route: AppScreenRoute?,
        requestCode: Int,
        extras: Bundle?
    ) {
        appRoute?.appRoute(baseNavigator, currentActivityClass, route, requestCode, extras)
    }

    protected fun openViewForResult(
        navigator: BaseNavigator?,
        route: AppScreenRoute?,
        requestCode: Int,
        extras: Bundle?
    ) {
        appRoute?.appRoute(navigator, currentActivityClass, route, requestCode, extras)
    }

    protected fun setViewResult(
        route: AppScreenRoute?,
        resultCode: Int,
        extras: Bundle?
    ) {
        appRoute?.appResultRoute(
            getBaseNavigator(), route, currentActivityClass, isViewFragment,
            getBaseNavigator()!!.requestCode, resultCode, extras
        )
    }


    open fun getLoadingLiveData(): LiveData<Boolean?>? {
        return loadingEvent.getLiveData()
    }

    open fun getEmptyLiveData(): LiveData<Boolean?>? {
        return emptyEvent.getLiveData()
    }

    open fun getErrorLiveData(): LiveData<ErrorData?>? {
        return errorEvent.getLiveData()
    }

    fun showLoading(onBtnLoading: Boolean = false) {
        if (onBtnLoading) isLoading.set(true) else loadingEvent.showLoading()
    }

    fun hideLoading() {
        if (isLoading.get()) isLoading.set(false) else loadingEvent.hideLoading()
    }

     fun hasData(size: Int) {
        emptyEvent.setViewValue(size)
    }

    protected fun showErrorView(
        errorCode: Int,
        errorMessage: String?,
        listener: ClickListener
    ) {
        val drawable: Int =
            if (isNetworkError(errorCode)) R.drawable.ic_no_internet else R.drawable.ic_empty_list
        errorEvent.setViewValue(
            ErrorData(
                true, EmptyView(
                    drawable, errorMessage,
                    R.string.try_again,
                    object : ClickListener {
                        override fun onViewClicked() {
                            hideErrorView()
                            listener.onViewClicked()
                        }

                    }, resourceProvider
                )
            )
        )
    }


    protected fun hideErrorView() {
        errorEvent.setViewValue(ErrorData(false, null))
    }

    fun setPausing(pausing: Boolean) {
        isPausing = pausing
    }

    fun getBaseNavigator(): BaseNavigator? {
        return baseNavigator
    }

    fun setBaseNavigator(baseNavigator: BaseNavigator?) {
        this.baseNavigator = baseNavigator
    }

    protected fun getStringResource(@StringRes resource: Int): String {
        return resourceProvider.getString(resource)
    }

    protected fun getDrawableResources(@DrawableRes drawableId: Int): Drawable {
        return resourceProvider.getDrawable(drawableId)
    }

    private fun initNotificationsHandler() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                handleReceivedNotification(intent)
            }
        }
        intentFilter = IntentFilter(FireBaseMessageHandling.BROADCAST_ACTION)
    }

    private fun handleReceivedNotification(intent: Intent) {
        val notificationTitle =
            intent.getStringExtra(NOTIFICATION_TITLE)
    }


    fun setNavigator(navigator: N) {
        this.navigator = navigator
    }

    protected val isUserLoggedIn: Boolean?
        protected get() = appRepository?.isUserLoggedIn()

    protected val isGuestUser: Boolean?
        protected get() = appRepository?.isGuestUser()

    protected val isActiveUser: Boolean?
        protected get() = appRepository?.isActiveUser()


    open val hasBack: Boolean?
        get() = getBaseNavigator()?.hasBack()

    val toolbarTitle: String?
        get() = getBaseNavigator()?.toolbarTitle

    val toolbarElevation: Boolean?
        get() = getBaseNavigator()?.toolbarElevation

    fun setToolbarHasElevation(hasElevation: Boolean?) {
        getBaseNavigator()?.setToolbarHasElevation(hasElevation!!)
    }

    open fun setupViewToolbar(
        title: String?,
        hasSecondaryAction: Boolean,
        hasFilterAction: Boolean,
        hasBack: Boolean
    ) {
        baseNavigator?.setupViewToolbar(title, hasSecondaryAction, hasFilterAction, hasBack)
    }

    open fun setToolbarActionField(text: String?, listener: ClickListener?) {
        baseNavigator?.setToolbarActionField(text, listener)
    }


    open fun setToolbarSecondaryActionField(@DrawableRes icon: Int, listener: ClickListener?) {
        baseNavigator?.setToolbarSecondaryActionField(icon, listener)
    }

    open fun setToolbarFilterActionField(@DrawableRes icon: Int, listener: ClickListener?, isFilterEnabled :Boolean) {
        baseNavigator?.setToolbarFilterActionField(icon, listener, isFilterEnabled )
    }

    open  fun setToolbarSearchListener(
        listener: SearchActionListener?
    ) {
         baseNavigator?.setToolbarSearchListener(listener)
    }


    open fun setToolbarAction(hasAction: Boolean) {
        baseNavigator?.setToolbarAction(hasAction)
    }

    protected fun showConfirmationDialog(
        icon: Drawable,
        confirmationMessage: String,
        navigator: ConfirmationDialogNavigator
    ) {
        val viewModel = ConfirmationDialogViewModel(
            confirmationMessage,
            icon, navigator, currentActivityClass
        )
        viewModel.lifecycleOwner = lifecycleOwner
        viewModel.setBaseNavigator(this.navigator as BaseNavigator)
        showConfirmationDialog(viewModel, CONFIRMATION_DIALOG_TAG)
    }

    protected fun showInputDialog(
        inputTitle: String,
        buttonText: String,
        navigator: InputDialogNavigator
    ) {
        val viewModel = InputDialogViewModel(
            inputTitle,
            buttonText,
             navigator, currentActivityClass
        )
        viewModel.lifecycleOwner = lifecycleOwner
        viewModel.setBaseNavigator(this.navigator as BaseNavigator)
        showInputDialog(viewModel, INPUT_DIALOG_TAG)
    }

    protected fun showConfirmationDialog(
        confirmationMessage: String?,
        navigator: ConfirmationDialogNavigator
    ) {
        val viewModel = ConfirmationDialogViewModel(
            confirmationMessage, navigator, currentActivityClass
        )
        viewModel.lifecycleOwner = lifecycleOwner
        viewModel.setBaseNavigator(this.navigator as BaseNavigator)
        showConfirmationDialog(viewModel, CONFIRMATION_DIALOG_TAG)
    }

    protected fun showConfirmationDialog(
        icon: Drawable,
        hasTitle: Boolean,
        confirmationMessage: String,
        confirmationTitle: String,
        positiveTxt: String?,
        negativeTxt: String?,
        navigator: ConfirmationDialogNavigator,
        confirmationIconBackgroundColor: Drawable
    ) {


        val viewModel = ConfirmationDialogViewModel(
            icon,
            hasTitle,
            confirmationMessage!!,
            confirmationTitle,
            positiveTxt!!,
            negativeTxt!!,
            navigator,
            confirmationIconBackgroundColor,
            currentActivityClass
        )
        viewModel.lifecycleOwner = lifecycleOwner
        viewModel.setBaseNavigator(this.navigator as BaseNavigator)
        showConfirmationDialog(viewModel, CONFIRMATION_DIALOG_TAG)
    }

    protected fun showRequiredLoginDialog(icon: Drawable, navigator: ConfirmationDialogNavigator) {
        showConfirmationDialog(
            icon,
            getStringResource(R.string.require_login),
            getStringResource(R.string.require_login_pos_btn),
            getStringResource(R.string.require_login_neg_btn),
            true,
            navigator
        )
    }

    protected fun showInsuranceRequestConfirmationDialog(
        icon: Drawable,
        hasTitle: Boolean,
        navigator: ConfirmationDialogNavigator,
        confirmationIconBackgroundColor: Drawable
    ) {
        showConfirmationDialog(
            icon,
            hasTitle,
            getStringResource(R.string.str_car_insurance_request_sent_dialog_message),
            getStringResource(R.string.str_car_insurance_request_sent_dialog_title),
            "",
            getStringResource(R.string.str_ok),
            navigator,
            confirmationIconBackgroundColor
        )
    }


    protected fun showConfirmationDialog(
        icon: Drawable, confirmationMessage: String?,
        confirmationPosTxt: String?, isCancelable: Boolean,
        navigator: ConfirmationDialogNavigator
    ) {
        showConfirmationDialog(
            icon, confirmationMessage, confirmationPosTxt,
            null, isCancelable, navigator
        )
    }

    protected fun showConfirmationDialog(
        icon: Drawable, confirmationMessage: String?,
        confirmationPosTxt: String?, confirmationNegTxt: String?,
        isCancelable: Boolean, navigator: ConfirmationDialogNavigator
    ) {
        val viewModel = ConfirmationDialogViewModel(
            icon,
            confirmationMessage!!, confirmationPosTxt!!,
            if (TextUtils.isEmpty(confirmationNegTxt)) getStringResource(R.string.cancel) else confirmationNegTxt!!,
            navigator, currentActivityClass
        )
        viewModel.lifecycleOwner = lifecycleOwner
        viewModel.setBaseNavigator(this.navigator as BaseNavigator)
        viewModel.setIsCancelable(isCancelable)
        showConfirmationDialog(viewModel, CONFIRMATION_DIALOG_TAG)
    }

    protected fun showDefaultDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        navigator: DefaultDialogNavigator?
    ) {
        showDefaultDialog(title, message, positiveText, negativeText, navigator, DEFAULT_DIALOG_TAG)
    }

    protected fun showErrorDialog(errorMessage: String?) {
        showDefaultDialog(
            getStringResource(R.string.str_error),
            errorMessage,
            getStringResource(R.string.str_ok),
            null,
            defaultDialogNavigator,
            DEFAULT_DIALOG_TAG
        )
    }

    protected val defaultDialogNavigator: DefaultDialogNavigator
        protected get() = object : DefaultDialogNavigator {
            override fun onPositiveButtonClicked() {
                dismissDialog(DEFAULT_DIALOG_TAG)
            }

            override fun onNegativeButtonClicked() {
                dismissDialog(DEFAULT_DIALOG_TAG)
            }
        }

    protected fun dismissConfirmationDialog() {
        dismissDialog(CONFIRMATION_DIALOG_TAG)
    }

//    protected open fun <P : BaseDialog?> showCustomDialog(
//        dialog: P,
//        tag: String?
//    ) {
//        baseNavigator!!.showCustomDialog(dialog, tag)
//    }

    protected val watchListChangeObserver: Observer<List<Int>>?
        protected get() = null

    protected val addressListChangeObserver: Observer<List<Any>>?
        protected get() = null

    protected val cartListChangeObserver: Observer<List<CartProduct?>?>?
        protected get() = null

    @CallSuper
    open fun onViewCreated() {
        isViewModelAttached = true
        if (!isViewFragment) checkOpenFragment()

        if (cartListChangeObserver != null)
            appRepository.getLiveCartList(lifecycleOwner,cartListChangeObserver)


    }

    fun onViewStarted() {}
    fun onViewResumed() {}
    fun onViewPaused() {}
    fun onViewStopped() {}
    open fun onViewDestroyed() {}

    fun onFragmentAttached() {}
    fun onBackPressed(): Boolean {
        return true
    }



    @CallSuper
    open fun onRequestResultsReady(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray?
    ) {
    }

    protected fun showLoadingDialog() {
        isLoadingDialogShown = true
        baseNavigator?.showLoadingDialog()
    }

    protected fun hideLoadingDialog() {
        isLoadingDialogShown = false
        baseNavigator?.hideLoadingDialog()
    }

    protected fun showSuccessBanner(message: String?) {
        baseNavigator?.showSuccessBanner(message)
    }

     fun showErrorBanner(message: String?) {
        baseNavigator?.showErrorBanner(message)
    }

    protected fun showErrorBanner(title: String?, message: String?) {
        baseNavigator?.showErrorBanner(title, message)
    }

    protected fun showShortToast(message: String?) {
        baseNavigator?.showToast(message, Toast.LENGTH_SHORT)
    }

    protected fun showLongToast(message: String?) {
        baseNavigator?.showToast(message, Toast.LENGTH_LONG)
    }

    protected fun showToast(message: String?, duration: Int) {
        baseNavigator?.showToast(message, duration)
    }

    fun goBack() {
        baseNavigator?.goBack()
    }

    protected fun finish() {
        baseNavigator?.finish()
    }

    protected fun hasPermission(permission: String?): Boolean? {
        return baseNavigator?.hasPermission(permission)
    }

    protected fun isLocationEnabled(context: Context?): Boolean? {
        return baseNavigator?.isLocationEnabled(context)
    }

    protected fun startRequestLocation(): Location? {
        return baseNavigator?.startRequestLocation()
    }

    protected fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        baseNavigator?.requestPermissionsSafely(permissions, requestCode)
    }


    protected fun requestPermissionsSafely(
        permission: String?,
        rationalMessage: String?,
        requestPermissionLauncher: ActivityResultLauncher<String>
    ) {
        baseNavigator?.requestPermissionsSafely(
            permission,
            rationalMessage,
            requestPermissionLauncher
        )
    }

    protected val dataExtras: Bundle?
        protected get() = baseNavigator?.intentExtras

    protected fun launchLinkIntent(link: String?) {
        baseNavigator?.launchLinkIntent(link)
    }

    protected fun launchShareIntent(subject:String?,text: String?) {
        baseNavigator?.launchShareIntent(subject,text)
    }

    protected fun launchCallIntent(phone: String?) {
        baseNavigator?.launchCallIntent(phone)
    }

    protected fun launchMailIntent(to: Array<String?>?) {
        baseNavigator?.launchMailIntent(to)
    }

    protected fun launchMapIntent(lat: String?, lang: String?) {
        baseNavigator?.launchMapIntent(lat, lang)
    }

    protected fun showDefaultDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        navigator: DefaultDialogNavigator?,
        tag: String?
    ) {
        baseNavigator?.showDefaultDialog(title, message, positiveText, negativeText, navigator, tag)
    }

    protected fun showDefaultDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        navigator: DefaultDialogNavigator?,
        tag: String?,
        cancelable: Boolean
    ) {
        baseNavigator?.showDefaultDialog(
            title,
            message,
            positiveText,
            negativeText,
            navigator,
            tag,
            cancelable
        )
    }

    private fun showConfirmationDialog(
        viewModel: ConfirmationDialogViewModel,
        tag: String
    ) {
        baseNavigator?.showConfirmationDialog(viewModel, tag)
    }

    private fun showInputDialog(
        viewModel: InputDialogViewModel,
        tag: String
    ) {
        baseNavigator?.showInputDialog(viewModel, tag)
    }

    protected fun dismissDialog(tag: String?) {
        baseNavigator?.dismissDialog(tag)
    }

    protected fun openActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        if (!isPausing) baseNavigator?.openActivity(activity, extras)
    }

    protected fun setActivityResult(resultCode: Int, extras: Bundle?) {
        baseNavigator?.setActivityResult(resultCode, extras)
    }

    protected fun openActivityForResult(
        activity: Class<out BaseActivity<*, *>?>?,
        requestCode: Int,
        extras: Bundle?
    ) {
        baseNavigator?.openActivityForResult(activity, requestCode, extras)
    }

    protected fun openActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        baseNavigator?.openActivityForResult(intent, requestCode)
    }

    protected fun openActivityAndFinish(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        baseNavigator?.openActivityAndFinish(activity, extras)
    }

     fun openNewActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        baseNavigator?.openNewActivity(activity, extras)
    }

    protected fun finishToActivity(activity: Class<out BaseActivity<*, *>?>?) {
        baseNavigator?.finishToActivity(activity)
    }

    protected fun finishApp() {
        baseNavigator?.finishApp()
    }

    protected fun hideToolbar() {
        baseNavigator?.hideToolbar()
    }

    protected fun showToolbar() {
        baseNavigator?.showToolbar()
    }

    private fun checkOpenFragment() {
        if (dataExtras?.containsKey(OPEN_ACTIVITY_FRAGMENT)!!) {
            val bundle = dataExtras
            val openFragment =
                bundle?.getBoolean(OPEN_ACTIVITY_FRAGMENT)
            if (openFragment!!) {
                val actionId = bundle?.getInt(SELECTED_FRAGMENT_ACTION_ID)
                val extra = bundle?.getBundle(FRAGMENT_DATA)
                if (actionId != 0) {
                    openFragment(actionId!!, extra)
                }
            }
        }
    }

    fun isFragmentView(isViewFragment: Boolean) {
        this.isViewFragment = isViewFragment
    }

    @CallSuper
    open fun onViewRecreated() { //        appRoute.reInitLauncher();
    }

    protected fun setViewResult(resultCode: Int, extras: Bundle?) {
        if (isViewFragment) setFragmentResult(resultCode, extras) else setActivityResult(
            resultCode,
            extras
        )
    }

    private fun getActivityFragmentData(actionId: Int, bundle: Bundle?): Bundle {
        val extra = Bundle()
        extra.putBoolean(OPEN_ACTIVITY_FRAGMENT, true)
        extra.putInt(SELECTED_FRAGMENT_ACTION_ID, actionId)
        if (bundle != null) extra.putBundle(FRAGMENT_DATA, bundle)
        return extra
    }

    protected fun openFragmentNewActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        @IdRes actionId: Int, bundle: Bundle?
    ) {
        baseNavigator?.openNewActivity(activity, getActivityFragmentData(actionId, bundle))
    }

    protected fun openFragment(@IdRes actionId: Int, extras: Bundle?) {
        baseNavigator?.openFragment(actionId, extras)
    }

    protected fun openFragmentForResult(
        @IdRes actionId: Int, requestCode: Int,
        extras: Bundle?
    ) {
        baseNavigator?.openFragmentForResult(actionId, requestCode, extras)
    }

    protected fun setFragmentResult(resultCode: Int, extras: Bundle?) {
        baseNavigator?.setFragmentResult(getBaseNavigator()!!.requestCode, resultCode, extras)
    }

   open fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {

    }
    protected fun trackEvent(
        eventName: String,
        parameters: Map<String, Any>? = null
    ) {
        val parametersBundle = getParametersBundle(parameters)
        firebaseLog(eventName, parametersBundle)
        facebookLog(eventName, parametersBundle)
    }

    private fun firebaseLog(eventName: String, parameters: Bundle?) {
        mFirebaseAnalytics?.logEvent(eventName, parameters)
    }

    //Auto Complete for search
    public fun getAppEventsLogger(): AppEventsLogger {
        return if (appEventsLogger == null) AppEventsLogger.newLogger(context) else appEventsLogger!!
    }

    private fun facebookLog(eventName: String, parameters: Bundle?) {
        if (parameters != null) appEventsLogger?.logEvent(
            eventName,
            parameters
        ) else appEventsLogger?.logEvent(eventName)
        appEventsLogger?.flush()
    }

    private fun getParametersBundle(parametersMap: Map<String, Any>?): Bundle? {
        if (parametersMap == null) return null
        val parameters = Bundle()
        for ((key, value) in parametersMap) {
            parameters.putString(key, value.toString())
        }
        return parameters
    }

    fun searchSuggestions(text: String?) {

    }

    var showErrorMsg = ObservableBoolean(false)
    open fun searchItemClick(position: Int) {

    }

    @CallSuper
    fun preformSearch(text: String): Boolean {
        var text = text
        text = text.trim { it <= ' ' }
        val preformSearch: Boolean =
            text.length >= resourceProvider.getInt(R.integer.search_txt_size)
        if (resourceProvider.getBoolean(R.bool.enable_search_error_msg)) showErrorMsg.set(!preformSearch)
        return preformSearch
    }


   open val hasSearch: Boolean?
        get() = getBaseNavigator()?.hasSearch()

    open fun openSearchView() {
        val extras = Bundle()
        extras.putInt(EXTRAS_KEY_SEARCH_NAV, getBaseNavigator()!!.searchActionNav)
        openView(AppScreenRoute.SEARCH_VIEW, extras)
    }

    open fun setToolbarSecondaryActionEnabled(isEnabled: Boolean) {
        baseNavigator?.setToolbarSecondaryActionEnabled(isEnabled)
    }

    open fun setToolbarFilterActionEnabled(isEnabled: Boolean) {
        baseNavigator?.setToolbarFilterActionEnabled(isEnabled)
    }

    open fun setToolbarSearchText(searchTxt: String) {
        baseNavigator?.setToolbarSearchText(searchTxt)
    }


    protected val versionName: String?
        protected get() {
            val versionName: String? = getBaseNavigator()?.VERSION_NAME()
            return if (TextUtils.isEmpty(versionName)) "" else getStringResource(R.string.version) + versionName
        }

    protected val stagingName: String
        protected get() = if (resourceProvider.getBoolean(R.bool.is_testing_server)) if (getBaseNavigator()?.VERSION_CODE() === 0) "\n Staging Version" else " (" + getBaseNavigator()?.VERSION_CODE()
            .toString() + ")\n Staging Version" else ""

    protected val appName: String
        protected get() = versionName +
                stagingName


    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }


    open fun isNetworkError(errorCode: Int): Boolean {
        return errorCode == STATUS_CODE_NETWORK_ERROR || errorCode == STATUS_CODE_TIMEOUT_ERROR
    }

    init {
        initNotificationsHandler()

    }

    protected open fun showBottomSheetDialog(
        layout: Int?,
        bottomSheetTitle: String,
        selectionItems: ArrayList<String>,
        selectionIcons: ArrayList<Int>,
        selectedPos: Int,
        isCancelable: Boolean,
        bottomSheetDialogNavigator: BottomSheetDialogNavigator,
        resourceProvider: ResourceProvider
    ) {
        val viewModel = BottomSheetDialogViewModel(
            bottomSheetTitle,
            selectionItems,
            selectionIcons,
            selectedPos,
            bottomSheetDialogNavigator,
            currentActivityClass,
            resourceProvider

        )
        viewModel.lifecycleOwner = lifecycleOwner
        viewModel.setBaseNavigator(navigator as BaseNavigator?)
        viewModel.setIsCancelable(isCancelable)
        showBottomSheetDialog(layout, bottomSheetTitle, viewModel)
    }

    open fun showBottomSheetDialog(
        layout: Int?, title: String?,
        viewModel: BottomSheetDialogViewModel
    ) {

        baseNavigator?.showBottomSheetDialog(layout, title, viewModel)
    }


    fun dismissBottomSheetDialog() {
        baseNavigator?.dismissBottomSheetDialog()
    }


    protected open fun showDatePickerDialog(
        date: OnDateSetListener?,
        myCalendar: Calendar?,
        disablePast: Boolean,
        disableFuture: Boolean
    ) {
        getBaseNavigator()?.showDatePickerDialog(date, myCalendar,disablePast,disableFuture)
    }


    fun dispatchCapturePictureIntent(requestCode: Int) {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context.getPackageManager())?.also {
                openActivityForResult(takePictureIntent, requestCode)
            }
        }
    }

    fun dispatchTakePictureIntent(requestCode: Int, isMultiSelection: Boolean) {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if(isMultiSelection)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        openActivityForResult(
            Intent.createChooser(intent, getStringResource(R.string.str_profile_edit_gallary)),
            requestCode
        )
    }

    fun openTakePhotoBottomSheet(
        cameraRequestPermissionLauncher: ActivityResultLauncher<String>?,
        gallaryRequestPermissionLauncher: ActivityResultLauncher<String>?,
        cameraRequestCode: Int,
        galleryRequestCode: Int,
        isMultiSelection:Boolean
    ) {
        val layout = R.layout.bottom_sheet_layout

        val itemsList = arrayListOf(
            getStringResource(R.string.str_profile_edit_camera),
            getStringResource(R.string.str_profile_edit_gallary)
        )

        val itemsIcons = arrayListOf(
            R.drawable.ic_camera,
            R.drawable.ic_photo_album
        )
        showBottomSheetDialog(
            layout,
            getStringResource(R.string.str_change_photo),
            itemsList,
            itemsIcons,
            0,
            false,
            object : BottomSheetDialogNavigator {

                override fun onItemSelected(itemClicked: String, pos: Int) {

                    when (itemClicked) {
                        getStringResource(R.string.str_profile_edit_camera)
                        -> {
                            if (hasPermission(Manifest.permission.CAMERA)!!) {
                                dispatchCapturePictureIntent(cameraRequestCode)
                            } else {

                                requestPermissionsSafely(
                                    Manifest.permission.CAMERA,
                                    getStringResource(R.string.str_permission_camera_rational_message),
                                    cameraRequestPermissionLauncher!!
                                )
                            }


                        }
                        getStringResource(R.string.str_profile_edit_gallary)
                        -> {

                            if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!!) {

                                dispatchTakePictureIntent(galleryRequestCode, isMultiSelection)

                            } else {

                                requestPermissionsSafely(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    getStringResource(R.string.str_permission_external_rational_message),
                                    gallaryRequestPermissionLauncher!!
                                )
                            }
                        }
                    }
                }

            },
            resourceProvider
        )
    }


    fun addOrRemoveFavorite(
        addToFavoritesRequest: FavoritesRequest,
        navigator: ConfirmationDialogNavigator,
        isAdd: Boolean
    ) {
        if (appRepository.isUserLoggedIn()) {
            if (isAdd) {
                appRepository.addToFavorites(
                    addToFavoritesRequest,
                    lifecycleOwner,
                    object : APICommunicatorListener<FavoriteResponse?> {
                        override fun onSuccess(result: FavoriteResponse?) {
                            showSuccessBanner(getStringResource(R.string.str_added_to_favorites_successfully))
                        }

                        override fun onError(throwable: ErrorDetails?) {
                            showErrorBanner(throwable?.errorMsg)
                        }

                    })
            } else {

                appRepository.removeFromFavorites(
                    addToFavoritesRequest,
                    lifecycleOwner,
                    object : APICommunicatorListener<FavoriteResponse?> {
                        override fun onSuccess(result: FavoriteResponse?) {
                            showSuccessBanner(getStringResource(R.string.str_removed_to_favorites_successfully))
                        }

                        override fun onError(throwable: ErrorDetails?) {
                            showErrorBanner(throwable?.errorMsg)
                        }

                    })
            }


        } else {

            showRequiredLoginDialog(getDrawableResources(R.drawable.ic_login), navigator)
        }

    }


    fun openSignInView(key: String?, requestCode: Int?) {
        val extras = Bundle()
        extras.putInt(key, requestCode!!)
        openViewForResult(AppScreenRoute.SIGN_IN_SCREEN, requestCode, extras)
    }


     fun getGoToSignInConfirmationNavigator(): ConfirmationDialogNavigator? {

        return object : ConfirmationDialogNavigator {
            override fun onYesClicked() {


                openSignInView(APPConstants.EXTRAS_KEY_FAVORITE, APPConstants.REQUEST_CODE_FAVORITE)

            }

            override fun onNoClicked() {
                dismissConfirmationDialog()
            }
        }
    }
    fun getStringFromResources(id: Int, vararg formatArgs: Any?): String? {
        val mContext: Context = LocaleHelper.setLocale(
            context,
            appRepository.getAppLanguage()
        )
        return mContext.resources.getString(id, *formatArgs)
    }

}