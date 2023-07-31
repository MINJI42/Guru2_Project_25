package com.example.thegrowthdiaryofmoss

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity() : AppCompatActivity(){

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
                    // 일치하는 경우 -> 화면 이동(test 토스트 메세지 출력)
                    cursor.close()
                    sqLiteDatabase.close()
                    Toast.makeText(applicationContext, "로그인되었습니다.", Toast.LENGTH_SHORT).show()

                    // SharedPreferences에 로그인 상태 저장
                    val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putBoolean("isLogin", true)
                    editor.putString("userId", enterId)
                    // apply() or commit() 메소드 호출하여 변경사항 저장
                    editor.apply()

                    // DB 연결
                    val dbManager = DBManager(this, "appDB", null, 1)
                    val db = dbManager.readableDatabase

                    // 커서 설정
                    val cursor = db.rawQuery("SELECT * FROM user WHERE id = '$enterId'", null)

                    if (cursor.moveToNext()) {
                        // 유저의 정보 가져오기
                        val usernameIndex = cursor.getColumnIndex("userName")
                        val username = cursor.getString(usernameIndex)
                        val ikkiNameIndex = cursor.getColumnIndex("ikkiName")
                        val ikkiName = cursor.getString(ikkiNameIndex)

                        // SharedPreferences에 로그인 상태 저장
                        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putBoolean("isLogin", true)
                        editor.putString("userId", enterId)
                        editor.putBoolean(
                            "isTutorialCompleted",
                            !username.isNullOrEmpty() && !ikkiName.isNullOrEmpty()
                        )
                        // apply() or commit() 메소드 호출하여 변경사항 저장
                        editor.apply()

                        if (username.isNullOrEmpty() || ikkiName.isNullOrEmpty()) {
                            val intent = Intent(this, TutorialActivity1::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    cursor.close()
                    db.close()

                } else {
                    // 일치하지 않는 경우 -> 토스트 메세지 출력
                    Toast.makeText(applicationContext, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            cursor.close()
        }

        // 하단 텍스트 터치 시 화면 전환
        tv_signup.setOnClickListener {
            // signup 화면으로 넘기기
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}