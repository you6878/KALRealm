package com.company.howl.howlyoutuberealm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import io.realm.*
import kotlinx.android.synthetic.main.activity_main.*

open class Item : RealmObject(){
    open var name : String? = null
}

class MainActivity : AppCompatActivity() {
    var shoppingList = mutableListOf<Item>() //자바 : List<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview.adapter = RcAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this)
        Realm.init(this)
        var config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()

        var realm = Realm.getInstance(config)
        button.setOnClickListener {
            realm.beginTransaction()
            var item = realm.createObject(Item::class.java)
            item.name = editText.text.toString()
            realm.commitTransaction()
        }

        realm.where(Item::class.java).findAll().addChangeListener {
            t: RealmResults<Item>?, changeSet: OrderedCollectionChangeSet? ->
            shoppingList.clear()
            shoppingList.addAll(t!!)
            recyclerview.adapter.notifyDataSetChanged()
        }


    }
    inner class RcAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var textview = TextView(this@MainActivity)
            return  CustomViewHolder(textview)
        }

        inner class CustomViewHolder(textview: TextView) : RecyclerView.ViewHolder(textview) {

        }

        override fun getItemCount(): Int {
            return  shoppingList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var textView = holder!!.itemView as TextView
            textView.text = shoppingList[position].name
        }

    }
}
