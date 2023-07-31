package com.example.guru2_project_25;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FriendFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_addFriend : FloatingActionButton
    private lateinit var btn_message : FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)

        recyclerView = view.findViewById(R.id.rv_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        btn_addFriend = view.findViewById(R.id.fabtn_addFriend)
        btn_message = view.findViewById(R.id.fabtn_message)

        btn_addFriend.setOnClickListener{
            val intent = Intent(requireActivity(), Add_FriendActivity::class.java)
            startActivity(intent)
        }

        btn_message.setOnClickListener {
            val intent = Intent(requireActivity(), MessageActivity::class.java)
            startActivity(intent)
        }

        recyclerView = view.findViewById(R.id.rv_list)

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")

        // DB에서 친구 목록 가져오기
        val dbManager = DBManager(requireContext(), "appDB", null, 1)
        val db = dbManager.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM friend WHERE userId = '$userId'", null)

        val friends = mutableListOf<Friend>()
        while (cursor.moveToNext()) {
            val friendIdIndex = cursor.getColumnIndex("friendId")
            val friendId = cursor.getString(friendIdIndex)

            val friendNicknameIndex = cursor.getColumnIndex("friendNickname")
            val friendNickname = cursor.getString(friendNicknameIndex)

            // DB에서 친구의 이름 검색
            val cursor2 = db.rawQuery("SELECT * FROM user WHERE id = '$friendId'", null)
            if (cursor2.moveToNext()) {
                val nameIndex = cursor2.getColumnIndex("userName")
                val name = cursor2.getString(nameIndex)
                friends.add(Friend(friendId, name, friendNickname))
            }
            cursor2.close()
        }

        cursor.close()
        db.close()

        // 어댑터 설정
        recyclerView.adapter = FriendAdapter(friends)

        return view
    }

    override fun onResume() {
        super.onResume()

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")

        // DB에서 친구 목록 가져오기
        val dbManager = DBManager(requireContext(), "appDB", null, 1)
        val db = dbManager.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM friend WHERE userId = '$userId'", null)

        val friends = mutableListOf<Friend>()
        while (cursor.moveToNext()) {
            val friendIdIndex = cursor.getColumnIndex("friendId")
            val friendId = cursor.getString(friendIdIndex)

            val friendNicknameIndex = cursor.getColumnIndex("friendNickname")
            val friendNickname = cursor.getString(friendNicknameIndex)

            // DB에서 친구의 이름 검색
            val cursor2 = db.rawQuery("SELECT * FROM user WHERE id = '$friendId'", null)
            if (cursor2.moveToNext()) {
                val nameIndex = cursor2.getColumnIndex("userName")
                val name = cursor2.getString(nameIndex)
                friends.add(Friend(friendId, name, friendNickname))
            }
            cursor2.close()
        }

        cursor.close()
        db.close()

        // 어댑터 설정
        recyclerView.adapter = FriendAdapter(friends)
    }
}