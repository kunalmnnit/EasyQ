package com.kunal.vqms.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.alansdk.AlanConfig
import com.kunal.vqms.R
import com.kunal.vqms.adapter.ItemListAdapter
import com.kunal.vqms.model.Item
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ItemListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        item_list_view.layoutManager = LinearLayoutManager(this)
        item_list_view.setHasFixedSize(true)
        item_list_view.layoutManager = LinearLayoutManager(this)
        val itemList = listOf(Item(R.mipmap.ic_launcher,"Government Offices"),
            Item(R.mipmap.ic_launcher,"Ration Shops")
        )
        adapter = ItemListAdapter(itemList,baseContext)
        item_list_view.adapter = adapter
        val key = "d68ce1a947377a1b1cd3f86d54b7142e2e956eca572e1d8b807a3e2338fdd0dc/stage"
        val config = AlanConfig.builder()
            .setProjectId(key)
            .build()

        alan_button.initWithConfig(config)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(homeIntent)
    }
}