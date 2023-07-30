package com.example.guru2_project_25

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TutorialActivity1 : AppCompatActivity() {
    lateinit var et_username : EditText
    lateinit var btn_next : Button
    lateinit var btn_prev : ImageView

    lateinit var dbManager: DBManager
    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial1)

        et_username = findViewById(R.id.et_usernameInput)
        btn_next = findViewById(R.id.btn_nextTutoial)
        btn_prev = findViewById(R.id.btn_prevLogin)

        // DB 연결
        dbManager = DBManager(this, "appDB", null, 1)

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")?: ""

        btn_prev.setOnClickListener {
            val prev = Intent(this, LoginActivity::class.java)
            startActivity(prev)
        }

        btn_next.setOnClickListener {
            var username = et_username.getText().toString()

            if (TextUtils.isEmpty(username)) { // TextUtils.isEmpty() 문자열이 비어있는 지 확인
                Toast.makeText(this, "이름이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            } else {
                updateUser(userId, username)  // 사용자 정보, 이름 업데이트
                val tutorial2 = Intent(this, TutorialActivity2::class.java)
                startActivity(tutorial2)
                finish()
            }
        }
    }

    fun updateUser(userId: String, username: String) {
        db = dbManager.writableDatabase
        val values = ContentValues()
        values.put("userName", username)
        db.update("user", values, "id=?", arrayOf(userId)) // 사용자 id에 해당하는 행을 업데이트
        db.close()
    }

}