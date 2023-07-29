package com.example.guru2_project_25

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class FriendAdapter(private val context: Context, private val friendList: List<Friend>) : BaseAdapter() {

    override fun getCount(): Int {
        return friendList.size
    }

    override fun getItem(position: Int): Any {
        return friendList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false)

        val friend = friendList[position]
        val nameTextView = view.findViewById<TextView>(R.id.home_item_tv)
        val emailTextView = view.findViewById<TextView>(R.id.home_item_email)
        nameTextView.text = friend.name
        emailTextView.text = friend.email

        return view
    }
}