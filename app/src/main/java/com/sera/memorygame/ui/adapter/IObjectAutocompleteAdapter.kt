@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.sera.memorygame.R
import com.sera.memorygame.databinding.SimpleIobjectAutocompleteLayoutBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.ui.view_holder.SimpleIObjectViewHolder

class IObjectAutocompleteAdapter<T>(context: Context,val list:List<T>,val handlers: Handlers?) : ArrayAdapter<T>(context, R.layout.simple_iobject_autocomplete_layout) {

    /**
     *
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: SimpleIObjectViewHolder<T>?
        if (convertView == null) {
            val itemBinding: SimpleIobjectAutocompleteLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.simple_iobject_autocomplete_layout, parent, false)
            holder = SimpleIObjectViewHolder(binding = itemBinding, callback = handlers)
            holder.view = itemBinding.root
            holder.view.tag = holder
        } else {
            holder = convertView.tag as SimpleIObjectViewHolder<T>
        }
        holder.bind(item = list[position], itemPosition = position)
        return holder.view
    }

    /**
     *
     */
    override fun getCount(): Int = list.size

    /**
     *
     */
    override fun getItem(position: Int): T = list[position]
}