package com.example.guru2_project_25;

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    // 레이아웃 변수
    lateinit var et_id : EditText
    lateinit var et_pw : EditText
    lateinit var btn_login : Button
    lateinit var tv_signup : TextView
    // db 변수 선언
    lateinit var dbManager: DBManager
    lateinit var sqLiteDatabase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 레이아웃 연결
        et_id = findViewById(R.id.et_id)
        et_pw = findViewById(R.id.et_pw)
        btn_login = findViewById(R.id.btn_login)
        tv_signup = findViewById(R.id.tv_signup)

        // DB 연결
        dbManager = DBManager(this, "appDB", null, 1)
        sqLiteDatabase = dbManager.readableDatabase


        // Login 버튼 터치 -> 사용자가 입력한 id로 DB 튜플 찾아 pw를 비교
        btn_login.setOnClickListener {
            var enterId: String = et_id.text.toString()
            var enterPw: String = et_pw.text.toString()
            var correctPw: String = ""
            var pwIndex: Int = 0
            var cursor: Cursor

            // 커서 설정
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM user WHERE id = '"+enterId+"';", null)

            if(cursor.moveToNext()) {
                pwIndex = cursor.getColumnIndex("pw")
                correctPw = cursor.getString(pwIndex).toString()

                if(enterPw == correctPw) {
                    val emailIndex = cursor.getColumnIndex("email")
                    val user_email: String = cursor.getString(emailIndex).toString()
                    cursor.close()
                    sqLiteDatabase.close()

                    // 일치 -> 화면 이동(test 토스트 메세지 출력)
                    Toast.makeText(applicationContext, "로그인되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("intent_email", user_email)
                    startActivity(intent)
                }
            }

            if(enterPw != correctPw) {
                // 일치x -> 토스트 메세지 출력
                cursor.close()
                Toast.makeText(applicationContext, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 하단 텍스트 터치 시 화면 전환
        tv_signup.setOnClickListener {
            // signup 화면으로 넘기기
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}