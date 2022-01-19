package com.gtera.ui.base

import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.gtera.base.BaseActivity
import com.gtera.ui.common.ClickListener
import com.gtera.ui.dialog.BaseDialog
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogViewModel
import com.gtera.ui.dialog.confirmation.ConfirmationDialogViewModel
import com.gtera.ui.dialog.defaultdialog.DefaultDialogNavigator
import com.gtera.ui.dialog.inputdialog.InputDialogViewModel
import java.util.*

interface BaseNavigator {
    fun showLoadingDialog()
    fun hideLoadingDialog()
    fun showSuccessBanner(message: String?)
    fun showErrorBanner(message: String?)
    fun showErrorBanner(title: String?, message: String?)
    fun showToast(message: String?, duration: Int)
    fun goBack()
    fun finish()
    fun hasPermission(permission: String?): Boolean
    fun requestPermissionsSafely(
        permission: String?,
        rationalMessage: String?,
        requestPermissionLauncher: ActivityResultLauncher<String>
    )
     fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int)

    val intentExtras: Bundle?
    fun launchShareIntent(subject: String?, text: String?)
    fun launchLinkIntent(link: String?)
    fun launchCallIntent(phone: String?)
    fun launchMailIntent(to: Array<String?>?)
    fun launchMapIntent(addressLat: String?, addressLng: String?)
    fun showDefaultDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        navigator: DefaultDialogNavigator?,
        tag: String?
    )

    fun showDefaultDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        navigator: DefaultDialogNavigator?,
        tag: String?,
        cancelable: Boolean
    )

    //    fun <T : BaseDialog?> showCustomDialog(dialog: T, tag: String?)
    fun <T : BaseDialog<*, *>?> showCustomDialog(dialog: T, tag: String?)

    fun showConfirmationDialog(
        viewModel: ConfirmationDialogViewModel?,
        tag: String?
    )


    fun showInputDialog(
        viewModel: InputDialogViewModel?,
        tag: String?
    )

    fun showBottomSheetDialog(
        @LayoutRes
        layout: Int?,
        title: String?,
        viewModel: BottomSheetDialogViewModel
    )

    fun dismissBottomSheetDialog()
    fun dismissDialog(tag: String?)
    fun openActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    )

    fun openActivityForResult(
        activity: Class<out BaseActivity<*, *>?>?,
        requestCode: Int,
        extras: Bundle?
    )

    fun openActivityForResult(
        intent: Intent?,
        requestCode: Int
    )

    fun openActivityAndFinish(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    )

    fun openNewActivity(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    )

    fun setActivityResult(resultCode: Int, extras: Bundle?)
    fun finishToActivity(activity: Class<out BaseActivity<*, *>?>?)
    fun finishApp()
    fun openFragment(@IdRes actionId: Int, extras: Bundle?)
    fun openFragmentForResult(
        @IdRes actionId: Int, requestCode: Int,
        extras: Bundle?
    )

    fun setFragmentResult(requestCode: Int, resultCode: Int, extras: Bundle?)
    fun onFragmentResult(requestCode: Int, resultCode: Int, extras: Bundle?)
    var requestCode: Int
    fun hasSecondaryActions(): Boolean
    fun hasFilterActions(): Boolean
    fun hasBack(): Boolean
    fun VERSION_NAME(): String?
    fun VERSION_CODE(): Int
    val toolbarTitle: String?
    fun setToolbarHasElevation(hasElevation: Boolean)
    val toolbarElevation: Boolean
    fun hideToolbar()
    fun showToolbar()
    fun setupViewToolbar(
        title: String?,
        hasSecondaryAction: Boolean,
        hasFilterAction: Boolean,
        hasBack: Boolean
    )

    fun setToolbarActionField(text: String?, listener: ClickListener?)
    fun setToolbarSecondaryActionField(@DrawableRes icon: Int, listener: ClickListener?)
    fun setToolbarFilterActionField(@DrawableRes icon: Int, listener: ClickListener?, isFilterEnabled :Boolean)
    fun setToolbarSearchListener( listener: SearchActionListener?)
    fun setStatusBarActionColor(@ColorRes color: Int)
    fun setToolbarAction(action: Boolean)
    fun openSearchView()
    fun startRequestLocation(): Location?
    fun isLocationEnabled(context: Context?): Boolean
    val searchActionNav: Int
    fun setToolbarSecondaryActionEnabled(isEnabled: Boolean)
    fun setToolbarFilterActionEnabled(isEnabled: Boolean)
    fun setToolbarSearchText(textSearch: String?)
    fun hasSearch(): Boolean
    fun showDatePickerDialog(
        date: OnDateSetListener?,
        myCalendar: Calendar?,
        disablePast: Boolean,
        disableFuture: Boolean
    )
}