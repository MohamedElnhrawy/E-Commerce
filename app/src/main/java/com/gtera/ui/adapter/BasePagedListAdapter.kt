package com.gtera.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.gtera.BR
import com.gtera.ui.adapter.ViewHolder
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.common.ViewHolderInterface
import java.util.*


class BasePagedListAdapter<T : BaseItemViewModel>(private val viewHolderInterface: ViewHolderInterface) :
    PagedListAdapter<T, ViewHolder>(DiffCallback<T>())/*, Filterable*/ {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.bind<ViewDataBinding>(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        )

        val holder = ViewHolder(Objects.requireNonNull<ViewDataBinding>(binding))
        val listener = View.OnClickListener { v ->
            val id = v.id
            viewHolderInterface.onViewClicked(holder.adapterPosition, id)
        }
        binding!!.root.setOnClickListener(listener)
        if (binding.root is ViewGroup)
            addClickListeners(binding.root as ViewGroup, listener)

        return holder
    }


    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.layoutId
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
        holder.binding?.setVariable(BR.viewModel, model)
        holder.binding?.executePendingBindings()
    }


    class DiffCallback<T : BaseItemViewModel> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return Objects.equals(oldItem.hashKey(), newItem.hashKey())
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return Objects.equals(oldItem, newItem)
        }

    }

    private fun addClickListeners(parent: ViewGroup, listener: View.OnClickListener) {
        //        try {
        for (i in 0 until parent.childCount) {
            if (parent.getChildAt(i) is ViewGroup)
                addClickListeners(parent.getChildAt(i) as ViewGroup, listener)
            else
                parent.getChildAt(i).setOnClickListener(listener)
        }
    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(charSequence: CharSequence): FilterResults {
//                val query = charSequence.toString()
//                var filtered: PagedList<T>?
//                if (query.isEmpty()) {
//                    filtered = currentList
//                } else {
//                    filtered = currentList
//
//                    for (item in currentList!!) {
//                        if (!item.filter(query)) {
//                            filtered!!.remove(item)
//                        }
//                    }
//                }
//                val results = FilterResults()
//                results.count = filtered!!.size
//                results.values = filtered
//                return results
//            }
//
//            override fun publishResults(
//                charSequence: CharSequence,
//                results: FilterResults
//            ) {
//                submitList(results.values as PagedList<T>?)
//            }
//        }
//    }


}