package com.gtera.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

//    class ViewHolder<V : ViewDataBinding>(val binding: V) : RecyclerView.ViewHolder(binding.getRoot())

class ViewHolder/*<V : ViewDataBinding?> */internal constructor(val binding: ViewDataBinding?) :
    RecyclerView.ViewHolder(binding?.root!!)