package com.sera.memorygame

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.model.IObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

abstract class BaseRecyclerViewAdapter(private val callback: Handlers? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: ArrayList<IObject> = ArrayList()

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return setViewHolder(parent, viewType, callback)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(holder, position)
    }

    /**
     *
     */
    override fun getItemViewType(position: Int): Int {
        return getType(position)
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     *  Add items to [list] and trigger animation (assign to each adapter independently)
     */
    open fun setList(list: ArrayList<IObject>) {
        items.clear()
        items = ArrayList()
        items.addAll(list)
        notifyItemRangeChanged(0, items.size)
    }

    /**
     *
     */
    open fun getData(): ArrayList<IObject>? {
        return items
    }

    /**
     *  Remove by [position]
     */
    open fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    /**
     *  Returns selected [ArrayList] as List
     */
    open fun getSelectedList(): ArrayList<IObject> {
        val result: ArrayList<IObject> = ArrayList()
        if (items.isNullOrEmpty()) {
            return ArrayList()
        }
        items.forEach {
            if (it.isItemSelected()) {
                result.add(it)
            }
        }
        return result
    }

    /**
     *  Returns selected data as [HashMap]
     */
    open fun getSelectedData(): HashMap<Int, IObject> {
        val result = HashMap<Int, IObject>()
        repeat(items.size) {
            val card = items[it]
            if (card.isItemSelected()) {
                result[it] = card
            }
        }
        return result
    }

    /**
     * Returns all data as [HashMap]
     */
    open fun getAllMapData(): HashMap<Int, IObject> {
        val result = HashMap<Int, IObject>()
        repeat(items.size) {
            result[it] = items[it]
        }
        return result
    }

    /**
     * Refresh [items] with new data (will trigger all list to be re-draw)
     */
    open fun refresh(list: ArrayList<IObject>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * Return IObject from list by [position]
     */
    open fun getItemByPosition(position: Int): IObject? {
        if (items.isNotEmpty()) {
            return items[position]
        }
        return null
    }

    /**
     *
     */
    open fun addItemByPosition(position: Int, item: IObject) {
        items.add(position, item)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, itemCount)
    }

    /**
     *
     */
    open fun updateWithRange(nList: ArrayList<IObject>) {
        items.clear()
        items.addAll(nList)
        notifyItemRangeChanged(0, itemCount)
    }

    /**
     *
     */
    open fun updateRange(newItems: ArrayList<IObject>) {
        val prevPosition = items.size
        items.addAll(newItems)
        notifyItemRangeChanged(prevPosition, newItems.size)
    }


    /**
     *
     */
    open fun swapeItem(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        notifyItemRangeChanged(0, itemCount)
    }


    abstract fun setViewHolder(parent: ViewGroup?, viewType: Int, callback: Handlers?): RecyclerView.ViewHolder

    abstract fun onBindData(holder: RecyclerView.ViewHolder?, position: Int)

    abstract fun getType(position: Int): Int
}