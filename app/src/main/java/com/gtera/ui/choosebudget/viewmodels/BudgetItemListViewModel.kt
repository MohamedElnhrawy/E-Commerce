package com.gtera.ui.choosebudget.viewmodels

import android.view.View
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Brand
import com.gtera.data.model.Budget
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.utils.Utilities
import javax.inject.Inject

class BudgetItemListViewModel @Inject constructor() :
    BaseItemViewModel() {

    init {
        initBudgetList()
    }

    var selectedBrands: ArrayList<Int>? = ArrayList<Int>()
    var budget: Budget? = null
    var resourceProvider: ResourceProvider? = null
    var isexpanded = ObservableField(false)
    var budgetRange = ObservableField("")
    var budgetBrands: ArrayList<Brand>? = ArrayList<Brand>()

    var expandCollapseClick =
        View.OnClickListener { v: View? ->
            isexpanded.set(isexpanded.get()?.not())
        }
    var budgetAdapter: BaseAdapter<BudgetBrandItemViewModel>? = null
    var budgetItemOrientation: ListOrientation? = ListOrientation.ORIENTATION_GRID

    // adapter's lists
    protected var budgeBrandstList: ArrayList<BudgetBrandItemViewModel> =
        ArrayList<BudgetBrandItemViewModel>()

    constructor(
        budget: Budget,
        resourceProvider: ResourceProvider
    ) : this() {

        this.budget = budget
        this.resourceProvider = resourceProvider
        this.budgetRange.set(
            resourceProvider.getString(
                R.string.str_from,
                resourceProvider.getString(R.string.str_egp, budget.from)
            ) + " " +
                    resourceProvider.getString(
                        R.string.str_to, resourceProvider.getString(
                            R.string.str_egp, budget.to
                        )
            )
        )
        budget.brands?.let {
            this.budgetBrands?.addAll(it)
            addBudgetBrands(budgetBrands!!)

        }


    }

    override val layoutId: Int
        get() = R.layout.budget_list_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


    protected fun initBudgetList() {
        budgetAdapter = BaseAdapter(budgeBrandstList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {

                budgeBrandstList?.get(position).isSelected.let { it.set(it.get()?.not()) }
                if (selectedBrands?.contains(budgeBrandstList?.get(position).brand?.id)!!) {
                    selectedBrands?.removeAll({ it == budgeBrandstList?.get(position).brand?.id})
                } else
                    selectedBrands!!.add(budgeBrandstList?.get(position).brand?.id!!)
            }
        })
    }

    protected fun addBudgetBrands(List: List<Brand?>) {
        if (Utilities.isNullList(List)) return


        for (brand in List) {
            val budgetBrandItemViewModel = BudgetBrandItemViewModel(
                brand!!
            )
            this.budgeBrandstList.add(budgetBrandItemViewModel)
        }
        budgetAdapter?.updateList(budgeBrandstList)
        budgetAdapter?.notifyDataSetChanged()
    }


}

