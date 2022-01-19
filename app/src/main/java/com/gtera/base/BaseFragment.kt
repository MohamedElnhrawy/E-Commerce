package com.gtera.base

import android.annotation.SuppressLint
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gtera.BR
import com.gtera.R
import com.gtera.databinding.BaseEmptyLayoutBinding
import com.gtera.databinding.BaseLayoutFragmentBinding
import com.gtera.databinding.BaseLoadingLayoutBinding
import com.gtera.event.ActivityLauncher
import com.gtera.ui.base.BaseNavigator
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.base.toolbar.ToolbarNavigator
import com.gtera.ui.base.toolbar.ToolbarViewModel
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.LifecycleComponent
import com.gtera.ui.dialog.BaseDialog
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogViewModel
import com.gtera.ui.dialog.confirmation.ConfirmationDialogViewModel
import com.gtera.ui.dialog.defaultdialog.DefaultDialogNavigator
import com.gtera.ui.dialog.inputdialog.InputDialogViewModel
import com.gtera.ui.helper.EmptyView
import com.gtera.utils.APPConstants
import com.gtera.utils.Logger
import com.gtera.utils.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import io.michaelrocks.paranoid.Obfuscate
import java.util.*
import javax.inject.Inject

@Obfuscate
@BindingMethods(
    BindingMethod(
        type = ImageView::class,
        attribute = "app:srcCompat",
        method = "setImageDrawable"
    )
)
abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>?> :
    DaggerFragment(), BaseNavigator {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var toolbarViewModel: ToolbarViewModel

    var baseActivity: BaseActivity<*, *>? = null
        private set
    protected lateinit var viewDataBinding: T
        private set
    var viewModel: V? = null
    private var mBaseLayoutBinding: BaseLayoutFragmentBinding? = null
    private var mToolbarViewModel: ToolbarViewModel? = null
    val bindingVariable: Int
        get() = BR.viewModel

    fun handleArguments(args: Bundle?) {}

    @get:LayoutRes
    abstract val layoutId: Int

    protected abstract val viewModelClass: Class<V>
    protected abstract fun setNavigator(viewModel: V?)

    override fun VERSION_NAME(): String? {
        return baseActivity!!.VERSION_NAME()
    }

    override fun VERSION_CODE(): Int {
        return baseActivity!!.VERSION_CODE()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context
            baseActivity = activity
            activity.onFragmentAttached()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        var bundle = Bundle().also {
            it.putParcelable(APPConstants.INTENT_DATA, data?.data)
            it.putAll(data?.extras?:Bundle())

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        createViewModel(viewModelClass)
        handleArguments(arguments)
        setHasOptionsMenu(false)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        val mRootView = performDataBinding(inflater, container)
        if (baseActivity!!.isBottomNavigationView) {
            initializeViews()
        }
        return mRootView
    }

    open fun launcherObserver(liveData: LiveData<ActivityLauncher>) {
        Logger.instance()?.v("OpenViewObserver", "Fragment LauncherMode Init")
        //        getBaseActivity().launcherObserver(liveData);
        liveData.observe(
            this,
            Observer { launcher: ActivityLauncher ->
                Logger.instance()?.v("OpenViewObserver", "Fragment LauncherMode: " + launcher.mode)
                baseActivity!!.launcherObserver(launcher)
            }
        )
    }

    private fun launcherObserver(launcher: ActivityLauncher) {
        baseActivity!!.launcherObserver(launcher)
    }

    private fun performDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return if (baseActivity!!.isBottomNavigationView) {
            DataBindingUtil.setDefaultComponent(LifecycleComponent(lifecycle))
            viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, null, false)

            mBaseLayoutBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.base_layout_fragment,
                container,
                false
            )
            mBaseLayoutBinding?.baseContainer?.addView(viewDataBinding.root)
            mBaseLayoutBinding?.executePendingBindings()
            mBaseLayoutBinding?.getRoot()!!


        } else {
            viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            viewDataBinding.root
        }


    }

    private fun createToolbarViewModel(): ToolbarViewModel {

        toolbarViewModel = ViewModelProvider(this, this.viewModelFactory).get(ToolbarViewModel::class.java)
        toolbarViewModel.setAppRoute((baseActivity!!.application as BaseApplication).getAppRoute())
        toolbarViewModel.setBaseNavigator(this)
        toolbarViewModel.setNavigator(object : ToolbarNavigator {

        })
        toolbarViewModel.lifecycleOwner = this
        return toolbarViewModel
    }

    private fun addToolbar() {

        val viewStub = toolbarView.viewStub ?: return
        toolbarView.setOnInflateListener { stub: ViewStub?, inflated: View? ->
            mToolbarViewModel = createToolbarViewModel()
            val mToolbarBinding = DataBindingUtil.bind<ViewDataBinding>(inflated!!)
            mToolbarBinding?.setVariable(
                BR.viewModel,
                mToolbarViewModel
            )
        }
        viewStub.inflate()
    }

    private fun addEmptyView() {

        val viewStub = emptyView.viewStub ?: return
        emptyView.setOnInflateListener(object : ViewStub.OnInflateListener {

            override fun onInflate(stub: ViewStub?, inflated: View?) {

                val mEmptyBinding: BaseEmptyLayoutBinding =
                    DataBindingUtil.bind(inflated!!)!!
                if (mEmptyBinding != null) {
                    mEmptyBinding.emptyView = screenEmptyView()
                }

            }
        })


        viewModel?.getEmptyLiveData()?.observe(
            viewLifecycleOwner,
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
            val mLoadingBinding: BaseLoadingLayoutBinding? =
                DataBindingUtil.bind(inflated!!)
            if (mLoadingBinding != null) {
                mLoadingBinding.isLoading = viewModel?.getLoadingLiveData()
                mLoadingBinding.shimmerCount = shimmerLoadingCount()
                mLoadingBinding.shimmerLayout = shimmerLoadingLayout()
            }
        }
        viewModel?.getLoadingLiveData()?.observe(
            viewLifecycleOwner,
            Observer { isLoading: Boolean ->
                if (isLoading) {
                    viewStub.visibility = View.VISIBLE
                    if (emptyView.binding?.root != null) emptyView.binding?.root!!.visibility = View.GONE
                    dataView.visibility = View.GONE
                } else {
                    viewStub.visibility = View.GONE
                    dataView.visibility = View.VISIBLE
                }
            } as Observer<Boolean?>
        )
    }

    protected val isCurvyToolbar: Boolean
        protected get() = false

    protected val isLargeTitleToolbar: Boolean
        protected get() = resources.getBoolean(R.bool.is_large_title_toolbar)

    protected open fun hasToolbar(): Boolean {
        return true
    }

    open val isListingView: Boolean
        get() = false

    protected open fun shimmerLoadingCount(): Int {
        return 6
    }

    protected open fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_base_fragment_layout
    }

    protected open fun screenEmptyView(): EmptyView {
        return EmptyView(R.drawable.ic_search, R.string.oops, R.string.empty_default_txt, viewModel?.resourceProvider!!)
    }

    protected open val dataView: View
        protected get() = mBaseLayoutBinding!!.baseContainer

    protected val toolbarView: ViewStubProxy
        protected get() = mBaseLayoutBinding!!.toolbar

    protected open val loadingView: ViewStubProxy
        protected get() = mBaseLayoutBinding!!.loadingView

    protected open val emptyView: ViewStubProxy
        protected get() = mBaseLayoutBinding!!.emptyView

    override fun onDetach() {
        super.onDetach()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
        if (!viewModel!!.isViewModelAttached) {
            if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.onViewCreated()
            viewModel!!.isFragmentView(true)
            viewModel!!.onViewCreated()
        } else viewModel!!.onViewRecreated()
        //        launcherObserver(mViewModel.getLauncherLiveData());
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.onViewStarted()
    }

    override fun onPause() {
        super.onPause()
        viewModel!!.onViewPaused()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window?.statusBarColor = resources.getColor(statusBarColor)
        viewModel!!.onViewResumed()
    }

    override fun onStop() {
        super.onStop()
        viewModel!!.onViewStopped()
    }

    protected val statusBarColor: Int
        protected get() = R.color.colorStatusBar

    private fun initializeViews() {
        if (hasToolbar()) addToolbar()
        if (isListingView) {
            addEmptyView()
            addLoadingView()
        }
    }

    private fun createViewModel(viewModelClass: Class<V>) {

        viewModel = ViewModelProvider(this, this.viewModelFactory).get(viewModelClass)
        viewModel!!.setAppRoute((baseActivity!!.application as BaseApplication).getAppRoute())
        viewModel!!.setBaseNavigator(this)
        viewModel!!.lifecycleOwner = this
        setNavigator(viewModel)
    }

    val pageTitle: String
        get() = this.javaClass.simpleName

    override fun openActivityForResult(
        activity: Class<out BaseActivity<*, *>?>?,
        requestCode: Int,
        extras: Bundle?
    ) {
        val intent = Intent(baseActivity, activity)
        if (extras != null) intent.putExtras(extras)
        startActivityForResult(intent, requestCode)
    }

    override fun openActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        startActivityForResult(intent, requestCode)
    }

    override fun setActivityResult(resultCode: Int, extras: Bundle?) {
        baseActivity!!.setActivityResult(resultCode, extras)
    }

    override fun showLoadingDialog() {
        baseActivity!!.showLoadingDialog()
    }

    //============================BaseNavigator Interface=========================
    override fun hideLoadingDialog() {
        baseActivity!!.hideLoadingDialog()
    }

    override fun showSuccessBanner(message: String?) {
        baseActivity!!.showSuccessBanner(message)
    }

    override fun showErrorBanner(message: String?) {
        baseActivity!!.showErrorBanner(message)
    }

    override fun showErrorBanner(
        title: String?,
        message: String?
    ) {
        baseActivity!!.showErrorBanner(title, message)
    }

    override fun showToast(message: String?, duration: Int) {
        baseActivity!!.showToast(message, duration)
    }

    override fun hasPermission(permission: String?): Boolean {
        return baseActivity!!.hasPermission(permission)
    }

    override fun isLocationEnabled(context: Context?): Boolean {
        val locationMode: Int
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(
                    requireContext().contentResolver,
                    Settings.Secure.LOCATION_MODE
                )
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            !TextUtils.isEmpty(locationProviders)
        }
    }

    override fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        baseActivity!!.requestPermissionsSafely(permissions, requestCode)
    }

    override fun requestPermissionsSafely(
        permission: String?,
        rationalMessage: String?,
        requestPermissionLauncher: ActivityResultLauncher<String>
    ) {
        baseActivity!!.requestPermissionsSafely(permission, rationalMessage, requestPermissionLauncher)
    }

    override val intentExtras: Bundle?
        get() = if (arguments != null) arguments else baseActivity!!.intentExtras

    override fun launchCallIntent(phone: String?) {
        baseActivity!!.launchCallIntent(phone)
    }

    override fun launchMailIntent(to: Array<String?>?) {
        baseActivity!!.launchMailIntent(to)
    }

    override fun launchMapIntent(
        addressLat: String?,
        addressLng: String?
    ) {
        baseActivity!!.launchMapIntent(addressLat, addressLng)
    }

    override fun launchShareIntent(
        subject: String?,
        text: String?
    ) {
        baseActivity!!.launchShareIntent(subject, text)
    }

    override fun launchLinkIntent(link: String?) {
        baseActivity!!.launchLinkIntent(link)
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
        baseActivity!!.showDefaultDialog(
            title,
            message,
            positiveText,
            negativeText,
            navigator,
            tag,
            cancelable
        )
    }

    override fun <P : BaseDialog<*, *>?> showCustomDialog(dialog: P, tag: String?) {
        baseActivity!!.showCustomDialog(dialog, tag)
    }

    override fun showConfirmationDialog(
        viewModel: ConfirmationDialogViewModel?,
        tag: String?
    ) {
        baseActivity!!.showConfirmationDialog(viewModel, tag)
    }


    override fun showInputDialog(
        viewModel: InputDialogViewModel?,
        tag: String?
    ) {
        baseActivity!!.showInputDialog(viewModel, tag)
    }

    override fun showDefaultDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        navigator: DefaultDialogNavigator?,
        tag: String?
    ) {
        baseActivity!!.showDefaultDialog(
            title,
            message,
            positiveText,
            negativeText,
            navigator,
            tag
        )
    }

    override fun dismissDialog(tag: String?) {
        baseActivity!!.dismissDialog(tag)
    }

    override fun openActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        baseActivity!!.openActivity(activity, extras)
    }

    override fun openActivityAndFinish(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        baseActivity!!.openActivityAndFinish(activity, extras)
    }

    override fun openNewActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        baseActivity!!.openNewActivity(activity, extras)
    }

    override fun finishToActivity(activity: Class<out BaseActivity<*, *>?>?) {
        baseActivity!!.finishToActivity(activity)
    }

    override fun finish() {
        if (baseActivity!!.isBottomNavigationView) baseActivity!!.navOnBackPress() else baseActivity!!.finish()
    }

    override fun finishApp() {
        baseActivity!!.finishApp()
    }


    override fun hasSecondaryActions(): Boolean {
        return baseActivity!!.hasSecondaryActions()
    }

    override fun hasFilterActions(): Boolean {
        return baseActivity!!.hasFilterActions()
    }

    override fun hasBack(): Boolean {
        return if (hasToolbar() && mToolbarViewModel != null) true else baseActivity!!.hasBack()
    }

    override fun hasSearch(): Boolean {
        return if (hasToolbar() && mToolbarViewModel != null) false else baseActivity!!.hasSearch()
    }

    override fun openSearchView() {
        baseActivity!!.openSearchView()
    }

    override val toolbarTitle: String?
        get() = baseActivity!!.toolbarTitle

    override val toolbarElevation: Boolean
        get() = baseActivity!!.toolbarElevation

    override fun setToolbarHasElevation(hasElevation: Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.hasElevation.set(
            hasElevation
        ) else baseActivity!!.setToolbarHasElevation(hasElevation)
    }

    override fun setupViewToolbar(
        title: String?,
        hasDrawer: Boolean,
        hasCart: Boolean,
        hasBack: Boolean
    ) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setupViewToolbar(
            title,
            hasCart,
            hasDrawer,
            hasBack
        ) else baseActivity!!.setupViewToolbar(title, hasDrawer, hasCart, hasBack)
    }

    override fun setToolbarActionField(
        text: String?,
        listener: ClickListener?
    ) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarActionField(
            text,
            listener
        ) else baseActivity!!.setToolbarActionField(text, listener)
    }


    override fun setToolbarAction(action: Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.hasAction.set(action) else baseActivity!!.setToolbarAction(
            action
        )
    }

    override fun hideToolbar() {
        baseActivity!!.hideToolbar()
    }

    override fun showToolbar() {
        baseActivity!!.showToolbar()
    }

    override fun openFragment(@IdRes actionId: Int, extras: Bundle?) {
        baseActivity!!.openFragment(actionId, extras)
    }

    override fun openFragmentForResult(
        @IdRes actionId: Int, requestCode: Int,
        extras: Bundle?
    ) {
        baseActivity!!.openFragmentForResult(actionId, requestCode, extras)
    }

    override fun setFragmentResult(
        requestCode: Int,
        resultCode: Int,
        extras: Bundle?
    ) {
        baseActivity!!.setFragmentResult(requestCode, resultCode, extras)
    }

    override fun onFragmentResult(
        requestCode: Int,
        resultCode: Int,
        extras: Bundle?
    ) {
    }

    override var requestCode: Int
        get() = baseActivity!!.requestCode
        set(requestCode) {
            baseActivity!!.requestCode = requestCode
        }

    override fun goBack() {
        baseActivity!!.onBackPressed();
    }


    override fun startRequestLocation(): Location? {
        val mLocationManager =
            baseActivity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager.getProviders(true)
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

    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String?)
    }

    override val searchActionNav: Int
        get() = 0

    override fun setToolbarSecondaryActionEnabled(isEnabled: Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarSecondaryActionEnabled(
            isEnabled
        ) else baseActivity!!.setToolbarSecondaryActionEnabled(isEnabled)
    }

    override fun setToolbarFilterActionEnabled(isEnabled: Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarFilterActionEnabled(
            isEnabled
        ) else baseActivity!!.setToolbarFilterActionEnabled(isEnabled)
    }

    override fun setToolbarSearchText(searchText: String?) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarSearchText(
            searchText!!
        ) else baseActivity!!.setToolbarSearchText(searchText)
    }

    override fun setToolbarSecondaryActionField(@DrawableRes icon: Int, listener: ClickListener?) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarSecondaryActionField(
            icon,
            listener
        )else baseActivity?.setToolbarSecondaryActionField(icon,listener)
    }

    override fun setToolbarFilterActionField(@DrawableRes icon: Int, listener: ClickListener?, isFilterEnabled :Boolean) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarFilterActionField(
            icon,
            listener,
            isFilterEnabled
        )else baseActivity?.setToolbarFilterActionField(icon,listener, isFilterEnabled)
    }

    override fun setToolbarSearchListener( listener: SearchActionListener?) {
        if (hasToolbar() && mToolbarViewModel != null) mToolbarViewModel!!.setToolbarSearchListener(
            listener
        )else baseActivity?.setToolbarSearchListener(listener)
    }


    override fun setStatusBarActionColor(@ColorRes color: Int) {
        requireActivity()?.window?.statusBarColor = resources.getColor(color)
    }

    override fun showBottomSheetDialog(
        layout: Int?, title: String?,
        viewModel: BottomSheetDialogViewModel
    ) {

        baseActivity?.showBottomSheetDialog(layout, title, viewModel)
    }

    override fun dismissBottomSheetDialog() {
        baseActivity?.dismissBottomSheetDialog()
    }

    override fun showDatePickerDialog(
        date: OnDateSetListener?,
        myCalendar: Calendar?,
        disablePast: Boolean,
        disableFuture: Boolean
    ) {
        baseActivity?.showDatePickerDialog(date, myCalendar,disablePast,disableFuture)
    }



}