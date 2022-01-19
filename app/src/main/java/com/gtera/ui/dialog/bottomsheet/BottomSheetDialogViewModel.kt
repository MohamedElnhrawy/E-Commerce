package com.gtera.ui.dialog.bottomsheet

import android.os.Handler
import androidx.databinding.ObservableField
import com.gtera.base.BaseViewModel
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import javax.inject.Inject

class BottomSheetDialogViewModel @Inject constructor() :
    BaseViewModel<BottomSheetDialogNavigator?>(),
    ViewHolderInterface {

    private var isCancelable = false
    private var selectionItems = ArrayList<String>()
    private var selectionIcons = ArrayList<Int>()
    var bottomSheetTitle = ObservableField("")
    private var selectedPosition = 0
    var orientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL
    var adapter: BaseAdapter<BottomSheetDialogItemViewModel>? = null
    private var list = ArrayList<BottomSheetDialogItemViewModel>()

    constructor(
        bottomSheetTitle: String,
        selectionItems: ArrayList<String>,
        selectionIcons: ArrayList<Int>,
        selectedPosition: Int,
        navigator: BottomSheetDialogNavigator,
        currentActivityClass: Class<*>?,
        resourceProvider: ResourceProvider
    ) : this() {
        setNavigator(navigator)
        this.selectionItems = selectionItems
        this.bottomSheetTitle.set(bottomSheetTitle)
        this.selectionIcons = selectionIcons
        this.selectedPosition = selectedPosition
        this.resourceProvider = resourceProvider
        list = ArrayList()
        adapter = BaseAdapter(list, this)
        orientation = ListOrientation.ORIENTATION_VERTICAL
        getItemsList()

    }

    init {
        adapter = BaseAdapter(list, this)
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
        list.clear()

        for (i in 0 until selectionItems.size) {

            list.add(
                BottomSheetDialogItemViewModel(
                    selectionIcons[i],
                    selectionItems[i],
                    i == selectedPosition,
                    resourceProvider
                )
            )

        }

        adapter!!.notifyDataSetChanged()
    }

    override fun onViewClicked(position: Int, id: Int) {

        list[selectedPosition].isSelected.set(false)
        selectedPosition = position
        list[position].isSelected.let { it.set(it.get()?.not()) }
        list[position].itemText.get()?.let { navigator?.onItemSelected(it, position) }
        val handler = Handler()
        handler.postDelayed({
            dismissBottomSheetDialog()
        }, 100)

    }


    fun setIsCancelable(isCancelable: Boolean) {
        this.isCancelable = isCancelable
    }

}