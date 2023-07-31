package com.example.guru2_project_25

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessageActivity : AppCompatActivity() {

    private lateinit var prev: ImageButton

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_list)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val messages = mutableListOf<Message>()
        val adapter = MessageAdapter(messages)
        recyclerView.adapter = adapter

        fun updateRecyclerView() {
            // SharedPreferences에서 유저의 아이디 불러오기
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            val userId = pref.getString("userId", "") ?: ""

            // DB에서 메시지 조회
            val dbManager = DBManager(this, "appDB", null, 1)
            val db = dbManager.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM message WHERE receiverId = '$userId' ORDER BY timestamp DESC", null)
            messages.clear()
            while (cursor.moveToNext()) {
                val senderId = cursor.getString(cursor.getColumnIndex("senderId"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"))

                // 발신자의 별명 조회
                val cursor2 = db.rawQuery("SELECT * FROM friend WHERE userId = '$userId' AND friendId = '$senderId'", null)
                if (cursor2.moveToNext()) {
                    val senderNickname = cursor2.getString(cursor2.getColumnIndex("friendNickname"))
                    messages.add(Message(senderId, senderNickname, content, timestamp))
                }
                cursor2.close()
            }
            cursor.close()
            db.close()

            adapter.notifyDataSetChanged()
        }

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "") ?: ""

        // DB에서 메시지 조회
        val dbManager = DBManager(this, "appDB", null, 1)
        val db = dbManager.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM message WHERE receiverId = '$userId' ORDER BY timestamp DESC", null)
        while (cursor.moveToNext()) {
            val senderId = cursor.getString(cursor.getColumnIndex("senderId"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"))

            // 발신자의 별명 조회
            val cursor2 = db.rawQuery("SELECT * FROM friend WHERE userId = '$userId' AND friendId = '$senderId'", null)
            if (cursor2.moveToNext()) {
                val senderNickname = cursor2.getString(cursor2.getColumnIndex("friendNickname"))
                messages.add(Message(senderId, senderNickname, content, timestamp))
            }
            cursor2.close()
        }
        cursor.close()
        db.close()

        adapter.notifyDataSetChanged()

        prev = findViewById(R.id.imgbtn_prev2)

        prev.setOnClickListener {
            finish()
        }
    }
}