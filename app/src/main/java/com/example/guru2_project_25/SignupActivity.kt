package com.example.guru2_project_25

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    // 레이아웃 변수 선언
    lateinit var et_email : EditText
    lateinit var et_id : EditText
    lateinit var et_pw : EditText
    lateinit var btn_signup : Button
    lateinit var tv_login : TextView
    // db 변수 선언
    lateinit var dbManager: DBManager
    lateinit var sqLiteDatabase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // 레이아웃 연결
        et_email = findViewById(R.id.et_email)
        et_id = findViewById(R.id.et_id)
        et_pw = findViewById(R.id.et_pw)
        btn_signup = findViewById(R.id.btn_signup)
        tv_login = findViewById(R.id.tv_login)
        // user db manager에 연결
        dbManager = DBManager(this, "appDB", null, 1)

        // signup 버튼 터치 -> 회원 정보 user DB에 삽입
        btn_signup.setOnClickListener {
            // DB에 삽입할 유저 정보 가져오기
            var set_email: String = et_email.text.toString()
            var set_id: String = et_id.text.toString()
            var set_pw: String = et_pw.text.toString()
            // 삽입
            sqLiteDatabase = dbManager.writableDatabase
            sqLiteDatabase.execSQL("INSERT INTO user VALUES ('"
                    + set_email+"', '"+set_id+"', '"+set_pw+"', null, null, 0, 0, 0);")
            sqLiteDatabase.close()

            Toast.makeText(applicationContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()

            // 로그인 화면으로 넘어가기
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        // 텍스트 터치 시 화면전환
        tv_login.setOnClickListener {
            // 로그인 화면으로 넘어가기
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}