package com.example.thegrowthdiaryofmoss

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

class TutorialActivity3 : AppCompatActivity() {

    lateinit var et_ikkiname : EditText
    lateinit var btn_next : Button
    lateinit var btn_prev : ImageView

    lateinit var dbManager: DBManager
    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial3)

        et_ikkiname = findViewById(R.id.et_ikkiName)
        btn_next = findViewById(R.id.btn_nextTutoial)
        btn_prev = findViewById(R.id.btn_prevLogin)

        // DB 연결
        dbManager = DBManager(this, "appDB", null, 1)

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")?: ""

        btn_prev.setOnClickListener {
            val prev = Intent(this, TutorialActivity2::class.java)
            startActivity(prev)
        }

        btn_next.setOnClickListener {
            var ikkiname = et_ikkiname.getText().toString()

            if (TextUtils.isEmpty(ikkiname)) { // TextUtils.isEmpty() 문자열이 비어있는 지 확인
                Toast.makeText(this, "이름이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            } else {
                updateIkki(userId, ikkiname)  // 사용자 정보, 이름 업데이트
                val tutorial2 = Intent(this, TutorialActivity4::class.java)
                startActivity(tutorial2)
                finish()
            }
        }
    }

    fun updateIkki(userId: String, ikkiname: String) {
        db = dbManager.writableDatabase
        val values = ContentValues()
        values.put("ikkiName", ikkiname)
        db.update("user", values, "id=?", arrayOf(userId)) // 사용자 id에 해당하는 행을 업데이트
        db.close()
    }

}