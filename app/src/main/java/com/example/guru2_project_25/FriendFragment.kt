package com.example.guru2_project_25;

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

class FriendFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)

        val friendList = listOf(
            Friend("홍길동", "hong@example.com"),
            Friend("김철수", "kim@example.com"),
            Friend("이영희", "lee@example.com")
        )

        val adapter = FriendAdapter(requireContext(), friendList)
        val listView = view.findViewById<ListView>(R.id.friend_list_view)
        listView.adapter = adapter

        return view
    }

}