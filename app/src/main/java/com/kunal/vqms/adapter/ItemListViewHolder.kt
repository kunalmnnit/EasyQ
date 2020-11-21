package com.kunal.vqms.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kunal.vqms.Interface.ItemClickListener
import kotlinx.android.synthetic.main.shop.view.*

/* @author kunal on 16/10/2020 */
/* View Holder for UI of Ration Shop Recylcer View Adpter */

class ItemListViewHolder(itemVew:View) : RecyclerView.ViewHolder(itemVew),View.OnClickListener {
    private lateinit var itemClickListener: ItemClickListener

    var item_image = itemVew.item_image
    var item_title = itemView.item_title

    init {
        itemView.setOnClickListener(this)
    }
    fun setItemClickListener(itemClickListener: ItemClickListener)
    {
        this.itemClickListener = itemClickListener
    }
    override fun onClick(v: View?) {
        itemClickListener.onClick(v!!,adapterPosition)
    }

}