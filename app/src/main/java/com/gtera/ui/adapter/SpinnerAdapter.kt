package com.gtera.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.*

class  SpinnerAdapter(
context: Context?,
resource: Int,
items: List<String?>?,
private var enableFirstItem: Boolean = false
) : ArrayAdapter<String?>(
context!!,
resource,
items ?: ArrayList<String>() as List<String>
) {


    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return super.getView(position, convertView, parent)
    }

    // Affects opened state of the spinner
    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        return super.getDropDownView(position, convertView, parent) as TextView
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0 || enableFirstItem
    }
}