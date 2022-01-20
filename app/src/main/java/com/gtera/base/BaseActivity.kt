package com.gtera.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Rect
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.databinding.*
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import com.gtera.BR
import com.gtera.R
import com.gtera.databinding.BaseEmptyLayoutBinding
import com.gtera.databinding.BaseLayoutBinding
import com.gtera.databinding.BaseLoadingLayoutBinding
import com.gtera.databinding.BottomSheetLayoutBinding
import com.gtera.di.providers.ResourceProvider
import com.gtera.event.ActivityLauncher
import com.gtera.event.ActivityLauncher.ActivityMode
import com.gtera.event.ErrorData
import com.gtera.ui.base.BaseNavigator
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.base.toolbar.ToolbarNavigator
import com.gtera.ui.base.toolbar.ToolbarViewModel
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.LifecycleComponent
import com.gtera.ui.dialog.BaseDialog
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogViewModel
import com.gtera.ui.dialog.confirmation.ConfirmationDialog
import com.gtera.ui.dialog.confirmation.ConfirmationDialogViewModel
import com.gtera.ui.dialog.defaultdialog.DefaultDialog
import com.gtera.ui.dialog.defaultdialog.DefaultDialogNavigator
import com.gtera.ui.dialog.inputdialog.InputDialog
import com.gtera.ui.dialog.inputdialog.InputDialogViewModel
import com.gtera.ui.helper.EmptyView
import com.gtera.utils.APPConstants
import com.gtera.utils.LocaleHelper
import com.gtera.utils.Utilities.hideSoftKeyboard
import com.gtera.utils.ViewModelFactory
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.shasin.notificationbanner.Banner
import com.wang.avi.AVLoadingIndicatorView
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject

//@Obfuscate
@BindingMethods(
    BindingMethod(
        type = ImageView::class,
        attribute = "app:srcCompat",
        method = "setImageDrawable"
    )
)
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>?> :
    DaggerAppCompatActivity(), BaseFragment.Callback, ToolbarNavigator, BaseNavigator {

    private lateinit var bottomSheetDialog: RoundedBottomSheetDialog
    protected lateinit var mViewDataBinding: T

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var resourceProvider: ResourceProvider

    protected var viewModel: V? = null
    protected var mToolbarViewModel: ToolbarViewModel? = null
    protected var mbottomSheetDialogViewModel: BottomSheetDialogViewModel? = null
    private var loadingDialog: Dialog? = null
    private var mBaseLayoutBinding: BaseLayoutBinding? = null
    private var mBottomSheetDialogBinding: BottomSheetLayoutBinding? = null
    override var requestCode = 0
    private var keyboardListenersAttached = false
    private var rootLayout: ViewGroup? = null
    private val keyboardLayoutListener = OnGlobalLayoutListener {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        rootLayout!!.getWindowVisibleDisplayFrame(r)
        val screenHeight = rootLayout!!.rootView.height
        val keypadHeight = screenHeight - r.bottom
        val broadcastManager =
            LocalBroadcastManager.getInstance(this@BaseActivity)
        if (keypadHeight > screenHeight * 0.15) {
            onShowKeyboard()
            val intent = Intent("KeyboardWillShow")
            broadcastManager.sendBroadcast(intent)
        } else {
            onHideKeyboard()
            val intent = Intent("KeyboardWillHide")
            broadcastManager.sendBroadcast(intent)
        }
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    protected abstract val viewModelClass: Class<V>
    open protected abstract fun setNavigator(viewModel: V?)
    override fun VERSION_NAME(): String? {
        return  /*((BaseApplication) getApplication()).VERSION_NAME()*/null
    }

    override fun VERSION_CODE(): Int {
        return 0 /* ((BaseApplication) getApplication()).VERSION_CODE()*/
    }

    private fun createViewModel(viewModelClass: Class<V>) {
        viewModel = ViewModelProvider(this, this.viewModelFactory).get(viewModelClass)
        viewModel?.setAppRoute((application as BaseApplication).getAppRoute())
        viewModel?.setBaseNavigator(this)
        viewModel?.lifecycleOwner = this
        setNavigator(viewModel)
    }

    private fun createToolbarViewModel(): ToolbarViewModel? {
        mToolbarViewModel =
            ViewModelProvider(this, this.viewModelFactory).get(ToolbarViewModel::class.java)
        mToolbarViewModel?.setAppRoute((application as BaseApplication).getAppRoute())
        mToolbarViewModel?.setBaseNavigator(this)
        mToolbarViewModel?.setNavigator(this)
        mToolbarViewModel?.lifecycleOwner = this
        return mToolbarViewModel
    }


    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {
        goBack()
    }

    open protected fun hasToolbar(): Boolean {
        return true
    }

    override val toolbarTitle: String?
        get() = ""

    override val toolbarElevation: Boolean
        get() = true

    override fun setToolbarHasElevation(hasElevation: Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.hasElevation.set(
            hasElevation
        )
    }

    override fun hasSecondaryActions(): Boolean {
        return false
    }

    override fun hasFilterActions(): Boolean {
        return false
    }

    override fun hasBack(): Boolean {
        return false
    }

    override fun hasSearch(): Boolean {
        return false
    }

    override fun openSearchView() {}
    open protected val isListingView: Boolean
        protected get() = false

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
//        FacebookSdk.sdkInitialize(getApplicationContext())

        createViewModel(viewModelClass)
        //if (BuildConfig.DEBUG)
        openApp()


        //        else
//            checkPiracy();
    }

    private fun openApp() {
        performDataBinding()
        viewModel!!.onViewCreated()
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.onViewCreated()
    }

    private fun checkPiracy() {}
    override fun onStart() {
        super.onStart()
        registerReceiver(viewModel?.broadcastReceiver, viewModel?.intentFilter)
        setupStatusBarColor()
        viewModel!!.onViewStarted()
    }

    override fun onResume() {
        viewModel!!.setPausing(false)
        super.onResume()
        viewModel!!.onViewResumed()
    }

    override fun onPause() {
        viewModel!!.setPausing(false)
        super.onPause()
        viewModel!!.onViewPaused()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(viewModel!!.broadcastReceiver)
        viewModel!!.onViewStopped()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(requestCode, resultCode, data)
        var bundle = Bundle().also {
            it.putParcelable(APPConstants.INTENT_DATA, data?.data)
            it.putAll(data?.extras ?: Bundle())

            if(data?.clipData!= null) {

                val mClipData: ClipData = data.getClipData()!!
                val mArrayUri = ArrayList<String>()
                val count: Int = data.getClipData()?.getItemCount()!!


                it.run {

                    for (i in 0 until count) {
                        val imageUri = mClipData.getItemAt(i).uri

                        mArrayUri.add(imageUri.toString())
                    }

                    putStringArrayList(APPConstants.CLIP_DATA, mArrayUri)
                }
            }

        }

        viewModel!!.onViewResult(
            requestCode,
            resultCode,
            bundle

        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel!!.onRequestResultsReady(requestCode, permissions, grantResults)
    }

    override fun setActivityResult(resultCode: Int, extras: Bundle?) {
        val intent = Intent()
        if (extras != null) intent.putExtras(extras)
        setResult(resultCode, intent)
        finish()
    }

//    override fun attachBaseContext(newBase: Context) {
//        if(viewModel == null)
//            createViewModel(viewModelClass)
//        super.attachBaseContext(onAttach(newBase, viewModel?.appRepository?.getAppLanguage()))
//    }

    //TODO:: Remove this method when update androidx appcompact from version 1.1.0
// This method is the solution for this issue of change language with androidx appcomcat v1.1.0
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private val bindingVariable: Int
        private get() = BR.viewModel


    private fun initializeViewBinding() {
        if (isBottomNavigationView) {
            mViewDataBinding = setContentView(this, getLayoutRes())

        } else {

            mViewDataBinding =
                inflate(LayoutInflater.from(this), getLayoutRes(), null, false)

            mBaseLayoutBinding = setContentView(this, R.layout.base_layout)


            if (hasToolbar()) {
                val toolbar: ViewStubProxy = mBaseLayoutBinding!!.toolbar
                addToolbar(toolbar)
            }
            val container: ViewGroup = mBaseLayoutBinding!!.baseContainer
            container.addView(mViewDataBinding.getRoot())
            mBaseLayoutBinding!!.executePendingBindings()
        }
    }

    private fun performDataBinding() {
        DataBindingUtil.setDefaultComponent(LifecycleComponent(lifecycle))
        initializeViewBinding()
        viewModel = if (viewModel == null) viewModel else viewModel
        mViewDataBinding.setVariable(bindingVariable, viewModel)
        mViewDataBinding.executePendingBindings()
        mViewDataBinding.setVariable(bindingVariable, viewModel)

        //        launcherObserver(mViewModel.getLauncherLiveData());
        if (!isBottomNavigationView) {
            if (isListingView) {
                addEmptyView()
                addLoadingView()
            }
            addErrorView()
        }
    }

    protected fun launcherObserver(liveData: LiveData<ActivityLauncher>) {
        liveData.observe(
            this,
            Observer { launcher: ActivityLauncher ->
                this.launcherObserver(launcher)
            }
        )
    }

    fun launcherObserver(launcher: ActivityLauncher) {
        if (launcher.mode != null) when (launcher.mode) {
            ActivityMode.OPEN_ACTIVITY -> openActivity(launcher.activity, launcher.extras)
            ActivityMode.OPEN_FINISH_ACTIVITY -> openActivityAndFinish(
                launcher.activity,
                launcher.extras
            )
            ActivityMode.FINISH_ACTIVITY -> finishActivity()
            ActivityMode.OPEN_RESULT_ACTIVITY -> openActivityForResult(
                launcher.activity,
                launcher.requestCode!!,
                launcher.extras
            )
            ActivityMode.SET_RESULT -> setActivityResult(launcher.resultCode!!, launcher.extras)
            ActivityMode.OPEN_NEW_ACTIVITY -> openNewActivity(launcher.activity, launcher.extras)
            ActivityMode.OPEN_FRAGMENT -> openFragment(launcher.actionId, launcher.extras)
            ActivityMode.OPEN_RESULT_FRAGMENT -> openFragmentForResult(
                launcher.actionId,
                launcher.requestCode!!,
                launcher.extras
            )
            ActivityMode.OPEN_NEW_FRAGMENT -> {
            }
        }
    }

    private fun addToolbar(toolbar: ViewStubProxy) {
        val viewStub = toolbar.viewStub ?: return
        toolbar.setOnInflateListener { stub: ViewStub?, inflated: View? ->
            createToolbarViewModel()
            val mToolbarBinding = DataBindingUtil.bind<ViewDataBinding>(inflated!!)
            mToolbarBinding?.setVariable(
                BR.viewModel,
                mToolbarViewModel
            )
        }
        viewStub.inflate()

    }


    private fun addErrorView() {
        val viewStub = emptyView.viewStub ?: return
        viewModel!!.getErrorLiveData()?.observe(
            this@BaseActivity,
            Observer { errorData: ErrorData ->
                if (errorData.isError) {
                    if (emptyView.isInflated) {
                        val mEmptyBinding: BaseEmptyLayoutBinding =
                            DataBindingUtil.bind(emptyView.root)!!
                        if (mEmptyBinding != null) mEmptyBinding.emptyView = errorData.data
                    } else {
                        emptyView.setOnInflateListener { stub: ViewStub?, inflated: View? ->
                            val mEmptyBinding: BaseEmptyLayoutBinding =
                                DataBindingUtil.bind(inflated!!)!!
                            if (mEmptyBinding != null) mEmptyBinding.emptyView = errorData.data
                        }
                    }
                    viewStub.visibility = View.VISIBLE
                    if (loadingView.viewStub != null) loadingView.viewStub!!.visibility = View.GONE
                    dataView.visibility = View.GONE
                } else {
                    viewStub.visibility = View.GONE
                    dataView.visibility = View.VISIBLE
                }
            } as Observer<ErrorData?>
        )
    }

    private fun addEmptyView() {
        val viewStub = emptyView.viewStub ?: return
        emptyView.setOnInflateListener { stub: ViewStub?, inflated: View? ->
            val mEmptyBinding: BaseEmptyLayoutBinding =
                DataBindingUtil.bind(inflated!!)!!
            if (mEmptyBinding != null) {
                mEmptyBinding.emptyView = screenEmptyView()
                viewModel?.getErrorLiveData()?.observe(this@BaseActivity,
                    Observer { errorData: ErrorData ->
                        if (errorData.isError) {
                            mEmptyBinding.emptyView = errorData.data
                            viewStub.visibility = View.VISIBLE
                            if (loadingView.viewStub != null) loadingView.viewStub!!.visibility =
                                View.GONE
                            dataView.visibility = View.GONE
                        } else {
                            viewStub.visibility = View.GONE
                            dataView.visibility = View.VISIBLE
                        }
                    } as Observer<ErrorData?>
                )
            }
        }
        viewModel?.getEmptyLiveData()?.observe(
            this,
            Observer { isEmpty: Boolean ->
                if (isEmpty) {
                    viewStub.visibility = View.VISIBLE
                    if (loadingView.viewStub != null) loadingView.viewStub!!.visibility = View.GONE
                    dataView.visibility = View.GONE
                } else {
                    viewStub.visibility = View.GONE
                    dataView.visibility = View.VISIBLE
                }
            } as Observer<Boolean?>
        )
    }

    private fun addLoadingView() {
        val viewStub = loadingView.viewStub ?: return
        loadingView.setOnInflateListener { stub: ViewStub?, inflated: View? ->
            val mLoadingBinding: BaseLoadingLayoutBinding =
                DataBindingUtil.bind(inflated!!)!!
            if (mLoadingBinding != null) {
                mLoadingBinding.isLoading = viewModel?.getLoadingLiveData()
                mLoadingBinding.shimmerCount = shimmerLoadingCount()
                mLoadingBinding.shimmerLayout = shimmerLoadingLayout()
            }
        }
        viewModel?.getLoadingLiveData()?.observe(
            this,
            Observer { isLoading: Boolean ->
                if (isLoading) {
                    viewStub.visibility = View.VISIBLE
                    if (emptyView.viewStub != null) emptyView.viewStub!!.visibility = View.GONE
                    dataView.visibility = View.GONE
                } else {
                    viewStub.visibility = View.GONE
                    dataView.visibility = View.VISIBLE
                }
            } as Observer<Boolean?>
        )
    }

    protected val dataView: View
        get() = mBaseLayoutBinding!!.baseContainer

    protected val loadingView: ViewStubProxy
        get() = mBaseLayoutBinding!!.loadingView

    protected val emptyView: ViewStubProxy
        get() = mBaseLayoutBinding!!.emptyView

    open val isBottomNavigationView: Boolean
        get() = false


    //=========================CommonActionsInterface=========================
    open fun shimmerLoadingCount(): Int {
        return 6
    }

     open fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_base_fragment_layout
    }

    open fun screenEmptyView(): EmptyView {
        return EmptyView(
            R.drawable.ic_empty_list,
            R.string.oops,
            R.string.empty_default_txt,
            viewModel?.resourceProvider!!
        )
    }

    override fun showLoadingDialog() {
        hideLoadingDialog()
        loadingDialog = Dialog(this)
        loadingDialog!!.setCanceledOnTouchOutside(false)
        loadingDialog!!.setCancelable(false)
        loadingDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog!!.setContentView(R.layout.loading_layout)
        loadingDialog!!.show()
        Objects.requireNonNull(loadingDialog!!.window)
            ?.setBackgroundDrawableResource(android.R.color.transparent)
        val loading: AVLoadingIndicatorView = loadingDialog!!.findViewById(R.id.loading_indicator)
        loading.show()
    }

    override fun hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) loadingDialog!!.dismiss()
    }

    override fun showToast(message: String?, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }

    override fun goBack() {
        hideSoftKeyboard(this)
        finish()
    }

    protected fun finishActivity() {
        finish()
    }


    override fun hasPermission(permission: String?): Boolean {
//        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
//                checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED

        return ContextCompat.checkSelfPermission(
            this,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun isLocationEnabled(context: Context?): Boolean {
        val locationMode: Int
        locationMode = try {
            Settings.Secure.getInt(
                context!!.contentResolver,
                Settings.Secure.LOCATION_MODE
            )
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun requestPermissionsSafely(
        permission: String?,
        rationalMessage: String?,
        requestPermissionLauncher: ActivityResultLauncher<String>
    ) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(permission!!, requestCode)
//        }


        when {

            shouldShowRequestPermissionRationale(permission!!) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
//            showInContextUI(...)
                val alertBuilder =
                    AlertDialog.Builder(this)
                alertBuilder.setCancelable(true)
                alertBuilder.setTitle(getString(R.string.str_permission_notice))
                alertBuilder.setMessage(rationalMessage)
                alertBuilder.setPositiveButton(
                    android.R.string.yes
                ) { dialog, which ->

                    requestPermissionLauncher.launch(
                        permission
                    )
                }
                val alert = alertBuilder.create()
                alert.show()

            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    permission
                )
            }
        }
    }

    override val intentExtras: Bundle
        get() = if (intent.extras == null) Bundle() else intent.extras!!

    override fun launchLinkIntent(link: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startActivity(intent)
    }

    override fun launchShareIntent(
        subject: String?,
        text: String?
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, getString(R.string.share_content)))
    }

    override fun launchCallIntent(phone: String?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        if (intent.resolveActivity(packageManager) != null) startActivity(intent) else showToast(
            "No Installed Phone App",
            Toast.LENGTH_LONG
        )
    }

    override fun launchMailIntent(to: Array<String?>?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, to)
        if (intent.resolveActivity(packageManager) != null) startActivity(intent) else showToast(
            "No Installed Mail App",
            Toast.LENGTH_LONG
        )
    }

    override fun launchMapIntent(
        addressLat: String?,
        addressLng: String?
    ) {
        val geoUri =
            "http://maps.google.com/maps?q=loc:$addressLat,$addressLng"
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun showBanner(message: String, status: Int) {
        showBanner(null, message, status)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun showBanner(
        label: String?,
        message: String,
        status: Int
    ) {
        val banner = Banner.make(
            mViewDataBinding.root,
            baseContext,
            Banner.TOP,
            R.layout.banner_layout
        )
        //Customize View in RunTime
        (banner.bannerView.findViewById<View>(R.id.banner_message) as TextView).text = message
        val statusIcon =
            banner.bannerView.findViewById<ImageView>(R.id.status_icon)
        val labelView = banner.bannerView.findViewById<TextView>(R.id.banner_label)
        if (status == Banner.SUCCESS) {
            statusIcon.background = resourceProvider.getDrawable(R.drawable.ic_stat_done)
            banner.bannerView
                .setBackgroundColor(resourceProvider.getColor(R.color.colorSuccess))
            labelView.text =
                if (TextUtils.isEmpty(label)) resourceProvider!!.getString(R.string.banner_success) else label
        } else if (status == Banner.ERROR) {
            statusIcon.background = resourceProvider.getDrawable(R.drawable.ic_stat_error)
            banner.bannerView.setBackgroundColor(resourceProvider.getColor(R.color.colorError))
            labelView.text =
                if (TextUtils.isEmpty(label)) resourceProvider!!.getString(R.string.banner_error) else label
        }
        banner.duration = 4000
        banner.bannerView
            .setOnClickListener { v: View? -> banner.dismissBanner() }
        banner.show()
    }

    override fun showSuccessBanner(message: String?) {
        showBanner(message!!, Banner.SUCCESS);
    }

    override fun showErrorBanner(message: String?) {
        showBanner(message!!, Banner.ERROR);
    }

    override fun showErrorBanner(
        title: String?,
        message: String?
    ) { //        showBanner(title, message, Banner.ERROR);
    }

    override fun showDefaultDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        navigator: DefaultDialogNavigator?,
        tag: String?
    ) {
        dismissDialog(tag)
        val ft =
            supportFragmentManager.beginTransaction()
        val prev =
            supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val dialog = DefaultDialog.getInstance(
            title,
            message,
            positiveText,
            negativeText,
            navigator!!,
            javaClass
        )
        dialog.show(ft, tag)
    }

    override fun showDefaultDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        navigator: DefaultDialogNavigator?,
        tag: String?,
        cancelable: Boolean
    ) {
        dismissDialog(tag)
        val ft =
            supportFragmentManager.beginTransaction()
        val prev =
            supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val dialog = DefaultDialog.getInstance(
            title,
            message,
            positiveText,
            negativeText,
            navigator!!,
            javaClass,
            cancelable
        )
        dialog.show(ft, tag)
    }

    override fun <T : BaseDialog<*, *>?> showCustomDialog(dialog: T, tag: String?) {
        dismissDialog(tag)
        val ft =
            supportFragmentManager.beginTransaction()
        val prev =
            supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialog!!.show(ft, tag)
    }

    override fun showConfirmationDialog(
        viewModel: ConfirmationDialogViewModel?,
        tag: String?
    ) {
        dismissDialog(tag)
        val ft =
            supportFragmentManager.beginTransaction()
        val prev =
            supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val dialog = ConfirmationDialog.getInstance(viewModel!!)
        if (!isDestroyed && !isFinishing) dialog.show(ft, tag)
    }


    override fun showInputDialog(
        viewModel: InputDialogViewModel?,
        tag: String?
    ) {
        dismissDialog(tag)
        val ft =
            supportFragmentManager.beginTransaction()
        val prev =
            supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(tag)
        val dialog = InputDialog.getInstance(viewModel!!)
        if (!isDestroyed && !isFinishing) dialog.show(ft, tag)
    }

    override fun dismissDialog(tag: String?) {
        val prev =
            supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            val dialog =
                prev as DialogFragment
            dialog.dismiss()
        }
    }


    override fun openActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        viewModel?.setPausing(true)
        val intent = Intent(this, activity)
        if (extras != null) intent.putExtras(extras)
        startActivity(intent)
    }

    override fun openActivityAndFinish(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        finish()
        val intent = Intent(this, activity)
        if (extras != null) intent.putExtras(extras)
        startActivity(intent)
    }

    override fun openNewActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        val intent = Intent(this, activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        if (extras != null) intent.putExtras(extras)
        finishAffinity()
        startActivity(intent)
    }

    //=============================DrawerNavigator================================

    //=============================DrawerNavigator================================
    override fun openActivityForResult(
        activity: Class<out BaseActivity<*, *>?>?,
        requestCode: Int,
        extras: Bundle?
    ) {
        val intent = Intent(this, activity)
        if (extras != null) intent.putExtras(extras)
        startActivityForResult(intent, requestCode)
    }

    override fun openActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        startActivityForResult(intent, requestCode)
    }

//    ===============================================ToolbarNavigator===========================

    //    ===============================================ToolbarNavigator===========================
    override fun finishToActivity(activity: Class<out BaseActivity<*, *>?>?) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun finishApp() {
        finishAffinity()
    }


    override fun onBackPressed() {
        if (viewModel!!.onBackPressed()) super.onBackPressed()
    }

    override fun hideToolbar() {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.hideToolbar.set(true)
    }

    override fun showToolbar() {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.hideToolbar.set(false)
    }

    override fun setupViewToolbar(
        title: String?,
        hasSecondaryAction: Boolean,
        hasFilterAction: Boolean,
        hasBack: Boolean
    ) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setupViewToolbar(
            title,
            hasSecondaryAction,
            hasFilterAction,
            hasBack
        )
    }

    override fun setToolbarSecondaryActionField(@DrawableRes icon: Int, listener: ClickListener?) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarSecondaryActionField(
            icon,
            listener
        )
    }

    override fun setToolbarSearchListener(listener: SearchActionListener?) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarSearchListener(
            listener
        )
    }


    override fun setToolbarFilterActionField(
        @DrawableRes icon: Int,
        listener: ClickListener?,
        isFilterEnabled: Boolean
    ) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarFilterActionField(
            icon,
            listener,
            isFilterEnabled
        )
    }


    override fun setToolbarActionField(
        text: String?,
        listener: ClickListener?
    ) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarActionField(
            text,
            listener
        )
    }


    override fun setToolbarAction(action: Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.hasAction.set(action)
    }

    ////////////////////////////////////////////////////
    protected fun navContainer(): Int {
        return 0
    }

    protected open fun navController(): NavController? {
        return if (navContainer() != 0) Navigation.findNavController(this, navContainer()) else null
    }

    fun navOnBackPress() {
        if (navController() != null)
            Objects.requireNonNull(navController())?.popBackStack();
    }

    override fun openFragment(@IdRes actionId: Int, extras: Bundle?) {
        if (actionId != 0) {
            if (navController() != null && checkSafeNavigate(actionId)) Objects.requireNonNull(
                navController()
            )?.navigate(actionId, extras)
        }
    }

    //TODO:: Check this in adding new navigation screen
    private fun checkSafeNavigate(@IdRes actionId: Int): Boolean {
        val navDestination: NavDestination =
            Objects.requireNonNull(navController())?.getCurrentDestination()!!
        if (navDestination != null) {
            val navAction = navDestination.getAction(actionId)
            return navAction != null && navAction.destinationId != 0 /*&&
                    navAction.getDestinationId() != navDestination.getId();*/
        }
        return false
    }

    override fun openFragmentForResult(
        @IdRes actionId: Int, requestCode: Int,
        extras: Bundle?
    ) {
//        requestCode = requestCode
        openFragment(actionId, extras)
    }

    override fun setFragmentResult(
        requestCode: Int,
        resultCode: Int,
        extras: Bundle?
    ) {
        supportFragmentManager.popBackStackImmediate()
        if (navController() != null &&
            Objects.requireNonNull(navController())!!.popBackStack()
        ) {
            onFragmentResult(requestCode, resultCode, extras);
        }
    }

    protected fun currentNavFragment(): BaseFragment<*, *>? { //        NavHostFragment navHostFragment =
//                (NavHostFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
//        if (navHostFragment == null)
//            return null;
//        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
//        return (BaseFragment) fragment;
        return null
    }

    //============= navigationView with edittext on keyboard shown ==============================
    override fun onFragmentResult(
        requestCode: Int,
        resultCode: Int,
        extras: Bundle?
    ) {
        val fragment = currentNavFragment()
        fragment?.viewModel?.onViewResult(requestCode, resultCode, extras)
    }

    protected open fun onShowKeyboard() {}
    protected open fun onHideKeyboard() {}
    protected open fun attachKeyboardListeners(rootLayout: ViewGroup?) {
        if (keyboardListenersAttached) {
            return
        }
        this.rootLayout = rootLayout
        this.rootLayout?.viewTreeObserver?.addOnGlobalLayoutListener(keyboardLayoutListener)
        keyboardListenersAttached = true
    }

    override fun onDestroy() {
        super.onDestroy()
//        viewModel?.getAppEventsLogger()?.flush()
        if (keyboardListenersAttached) {
            rootLayout?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardLayoutListener)
            viewModel?.onViewDestroyed()

            super.onDestroy()
        }
    }

    //=============================================================================================


    override fun startRequestLocation(): Location? {
        val mLocationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers =
            Objects.requireNonNull(mLocationManager).getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            @SuppressLint("MissingPermission") val l =
                mLocationManager.getLastKnownLocation(provider)
                    ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) { // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation
    }

    override val searchActionNav: Int
        get() = 0

    override fun setToolbarSecondaryActionEnabled(isEnabled: Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarSecondaryActionEnabled(
            isEnabled
        )
    }

    override fun setToolbarFilterActionEnabled(isEnabled: Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarFilterActionEnabled(
            isEnabled
        )
    }

    override fun setToolbarSearchText(searchText: String?) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarSearchText(
            searchText!!
        )
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun setStatusBarActionColor(@ColorRes color: Int) {
        window.statusBarColor = resources.getColor(color)
    }


    protected open fun setupStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isStatusBarLight()) {
                val view = window.decorView
                view.systemUiVisibility =
                    view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                val view = window.decorView
                view.systemUiVisibility =
                    view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            window.statusBarColor = ContextCompat.getColor(this, getStatusBarColor())
        }
        viewModel?.onViewStarted()
    }

    protected open fun getStatusBarColor(): Int {
        return if (hasToolbar()) R.color.colorStatusBar else R.color.colorStatusBarNoToolbar
    }

    protected open fun isStatusBarLight(): Boolean {
        return if (hasToolbar()) resourceProvider.getBoolean(R.bool.status_bar_light) else resourceProvider.getBoolean(
            R.bool.status_bar_no_toolbar_light
        )
    }

    override fun showBottomSheetDialog(
        @LayoutRes layout: Int?, title: String?,
        viewModel: BottomSheetDialogViewModel
    ) {

        mBottomSheetDialogBinding = inflate(LayoutInflater.from(this), layout!!, null, false)


        mBottomSheetDialogBinding?.viewModel = viewModel
        mBottomSheetDialogBinding?.setVariable(
            BR.viewModel,
            viewModel
        )
        bottomSheetDialog = RoundedBottomSheetDialog(this)
        bottomSheetDialog.setContentView(mBottomSheetDialogBinding?.root!!)
        bottomSheetDialog.show()

    }

    override fun dismissBottomSheetDialog() {
        if (bottomSheetDialog.isShowing) bottomSheetDialog.dismiss()
    }


    override fun showDatePickerDialog(
        date: OnDateSetListener?,
        myCalendar: Calendar?,
        disablePast: Boolean,
        disableFuture: Boolean
    ) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        showDatePickerDialog(date, myCalendar!!, calendar.time,disablePast,disableFuture)
    }

    fun showDatePickerDialog(
        date: OnDateSetListener?, myCalendar: Calendar,
        minDate: Date?, disablePast: Boolean, disableFuture: Boolean
    ) {
        val dialog = DatePickerDialog(
            this, R.style.PickerTheme, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
        if (minDate != null) {
            val calendar = Calendar.getInstance()
            calendar.time = minDate
            if (disablePast)
                dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            if(disableFuture)
            dialog.datePicker.maxDate = calendar.timeInMillis  // for now max date and if we need it we shall customize it
        }
        if (!isDestroyed && !isFinishing) dialog.show()
    }

}