package com.gtera.ui.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gtera.BR
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.common.ViewHolderInterface
import java.util.*

class BaseAdapter<T : BaseItemViewModel?>(
    list: ArrayList<T>?,
    viewHolderInterface: ViewHolderInterface
) : RecyclerView.Adapter<BaseAdapter.ViewHolder<ViewDataBinding>>() {

    private var list: ArrayList<T>? = null
    private var viewHolderInterface: ViewHolderInterface? = null
    var absoluteOffset: Int? = null
    var horizontalRecyclerViews = mutableListOf<RecyclerView>()

    fun updateList(list: ArrayList<T>?) {
        this.list!!.clear()
        this.list!!.addAll(list!!)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list!![position]!!.layoutId
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<ViewDataBinding> {

        val binding = DataBindingUtil.bind<ViewDataBinding>(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        )
        val holder: ViewHolder<ViewDataBinding>? =
            ViewHolder(
                Objects.requireNonNull(binding)
            )
        val listener =
            View.OnClickListener { v ->
                val id = v.id
                viewHolderInterface?.onViewClicked(holder?.getAdapterPosition()!!, id)
            }
        binding!!.root.setOnClickListener(listener)
        if (binding.root is ViewGroup) addClickListeners(
            binding.root as ViewGroup,
            listener
        )
        return holder!!
    }


    override fun onBindViewHolder(
        holder: ViewHolder<ViewDataBinding>,
        position: Int
    ) {
        val model = list!![position]
        holder.binding?.setVariable(BR.viewModel, model)
        holder.binding?.executePendingBindings()



        setRecyclersViewScrollListener(holder, position)
    }

    private fun setRecyclersViewScrollListener(
        holder: ViewHolder<ViewDataBinding>,
        position: Int
    ) {
        if (holder.binding?.root is ViewGroup) {

            if ((holder.binding?.root as ViewGroup).getChildAt(0) is RecyclerView) {

                val onTouchListener = object : RecyclerView.OnItemTouchListener {
                    override fun onTouchEvent(p0: RecyclerView, p1: MotionEvent) {
                    }

                    override fun onInterceptTouchEvent(p0: RecyclerView, p1: MotionEvent): Boolean {
                        if (p1.action == MotionEvent.ACTION_UP) {
                            absoluteOffset = p0.computeHorizontalScrollOffset()
                            return true
                        }
                        return false
                    }

                    override fun onRequestDisallowInterceptTouchEvent(p0: Boolean) {
                    }
                }

                val onScrollListener = object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val value = recyclerView.computeHorizontalScrollOffset()
                        matchOffset(value)
                    }
                }

                var recyclerView = (holder.binding?.root as ViewGroup).getChildAt(0) as RecyclerView
                recyclerView.tag = position
                recyclerView.clearOnScrollListeners()
                recyclerView.addOnItemTouchListener(onTouchListener)
                recyclerView.addOnScrollListener(onScrollListener)
                horizontalRecyclerViews.add(recyclerView)



                horizontalRecyclerViews.add((holder.binding?.root as ViewGroup).getChildAt(0) as RecyclerView)
            }
        }
    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    private fun addClickListeners(
        parent: ViewGroup,
        listener: View.OnClickListener
    ) {
        try {
            for (i in 0 until parent.childCount) {
                if (parent.getChildAt(i) is ViewGroup) addClickListeners(
                    parent.getChildAt(i) as ViewGroup,
                    listener
                ) else parent.getChildAt(i).setOnClickListener(listener)
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }


    fun matchOffset(offset: Int? = absoluteOffset) {
        offset?.let { offsetValue ->
            horizontalRecyclerViews?.forEach { recyclerView ->
                val currentOffset = recyclerView.computeHorizontalScrollOffset()
                if (offsetValue != currentOffset) {
                    recyclerView.scrollBy(offsetValue-currentOffset, 0)
                }
            }
        }
    }


    class ViewHolder<V : ViewDataBinding?> internal constructor(val binding: ViewDataBinding?) :
        RecyclerView.ViewHolder(binding?.getRoot()!!)

    init {
        if (list == null) this.list = ArrayList() else this.list = list
        this.viewHolderInterface = viewHolderInterface
    }
}