package com.gtera.ui.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import java.util.*

class AutoCompleteAdapter(context: Context, resource: Int) :
    ArrayAdapter<String?>(context, resource), Filterable {
    private val mListData: MutableList<String>
    fun setData(list: List<String>) {
        mListData.clear()
        mListData.addAll(list)
    }

    override fun getCount(): Int {
        return mListData.size
    }

    override fun getItem(position: Int): String? {
        return mListData[position]
    }

    /**
     * Used to Return the full object directly from productsAdapter.
     *
     * @param position the selected item position in the variantList
     * @return the object of the variantList in index 'position'
     */
    fun getObject(position: Int): String {
        return mListData[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = mListData
                    filterResults.count = mListData.size
                }
                return filterResults
            }

            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    init {
        mListData = ArrayList()
    }
}