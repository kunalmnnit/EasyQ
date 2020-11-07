package com.kunal.vqms.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.kunal.vqms.Interface.ItemClickListener
import com.kunal.vqms.ui.MapsActivity
import com.kunal.vqms.R
import com.kunal.vqms.model.Item

class ItemListAdapter(private val itemList:List<Item>, private val context: Context) : RecyclerView.Adapter<ItemListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item,parent,false)
        return ItemListViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val item = itemList[position]
        holder.item_image.setImageResource(item.url)
        holder.item_title.text = item.title
        holder.setItemClickListener(object : ItemClickListener{
            override fun onClick(view: View, position: Int) {
                    startActivity(context,Intent(context, MapsActivity::class.java),null)

            }

        })
    }

}