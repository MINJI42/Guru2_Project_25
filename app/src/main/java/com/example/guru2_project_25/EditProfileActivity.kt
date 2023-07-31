package com.example.guru2_project_25;

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")


        val dbManager = DBManager(this, "appDB", null, 1)
        val db = dbManager.readableDatabase


        // 커서 설정
        val cursor = db.rawQuery("SELECT * FROM user WHERE id = '$userId'", null)

        if (cursor.moveToNext()) {
            // 유저의 정보 가져오기
            val usernameIndex = cursor.getColumnIndex("userName")
            val username = cursor.getString(usernameIndex)
            val ikkiNameIndex = cursor.getColumnIndex("ikkiName")
            val ikkiName = cursor.getString(ikkiNameIndex)
            val emailIndex = cursor.getColumnIndex("email")
            val email = cursor.getString(emailIndex)
            val pwIndex = cursor.getColumnIndex("pw")           //  db 통해 사용자 비밀번호 가져옴
            val pw = cursor.getString(pwIndex)

            // TextView에 유저의 정보 표시
            val tv_Username = findViewById<TextView>(R.id.tv_mp_username)
            tv_Username.text = username
            val tv_ikkiName = findViewById<TextView>(R.id.tv_mp_ikkiname)
            tv_ikkiName.text = ikkiName
            val tv_UserEmail = findViewById<TextView>(R.id.tv_mp_email)
            tv_UserEmail.text = email
            val tv_UserPw = findViewById<TextView>(R.id.tv_mp_pw)
            tv_UserPw.text = pw

            val tv_TitleName = findViewById<TextView>(R.id.tv_mp_titleName)
            tv_TitleName.text = username + "님의 마이페이지"

        }

        cursor.close()
        db.close()

        // 사용자 이름 변경
        val editUsername = findViewById<TextView>(R.id.tv_mp_username)

        editUsername.setOnClickListener {
            showEditDialog("사용자 이름 변경", "새로운 사용자 이름을 입력하세요.") { newText ->
                editUsername.text = newText
                // db 업데이트 내용
                val dbManager = DBManager(this, "user", null, 1)
                val db = dbManager.writableDatabase
                db.execSQL("UPDATE user SET userName = '$newText' WHERE id = '$userId'")
            }
        }

        // 이끼 이름 변경
        val editIkkiname = findViewById<TextView>(R.id.tv_mp_ikkiname)

        editIkkiname.setOnClickListener {
            showEditDialog("이끼 이름 변경", "새로운 이끼 이름을 입력하세요.") { newText ->
                editIkkiname.text = newText
                // db 업데이트 내용
                val dbManager = DBManager(this, "appDB", null, 1)
                val db = dbManager.writableDatabase
                db.execSQL("UPDATE user SET ikkiName = '$newText' WHERE id = '$userId'")
            }
        }

        // 등록된 이메일 변경
        val editEmail = findViewById<TextView>(R.id.tv_mp_email)

        editEmail.setOnClickListener {
            showEditDialog("등록된 이메일 변경", "새로운 이메일 주소를 입력하세요.") { newText ->
                editEmail.text = newText
                // db 업데이트 내용
                val dbManager = DBManager(this, "user", null, 1)
                val db = dbManager.writableDatabase
                db.execSQL("UPDATE user SET email = '$newText' WHERE id = '$userId'")
            }
        }

        // 비밀번호 변경
        val editPassword = findViewById<TextView>(R.id.tv_mp_pw)

        editPassword.setOnClickListener {
            showEditDialog("비밀번호 변경", "새로운 비밀번호를 입력하세요.") { newText ->
                editPassword.text = newText
                // db 업데이트 내용
                val dbManager = DBManager(this, "appDB", null, 1)
                val db = dbManager.writableDatabase
                db.execSQL("UPDATE user SET pw = '$newText' WHERE id = '$userId'")
            }
        }

        // 이전 화면
        val editFinshButton = findViewById<Button>(R.id.btn_finish)

        editFinshButton.setOnClickListener {
            finish() // 현재 액티비티 종료하고 이전으로 돌아가기 위해 finish() 사용
        }
    }

    private fun showEditDialog(title: String, message: String, onConfirm: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog, null)

        // 텍스트 변경
        dialogLayout.findViewById<TextView>(R.id.tv_dialogTitle).text = title
        dialogLayout.findViewById<TextView>(R.id.tv_dialogInfo).text = message

        val editText = dialogLayout.findViewById<EditText>(R.id.et_editInfo)

        builder.setView(dialogLayout)
        val dialog = builder.create()

        dialogLayout.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            onConfirm(editText.text.toString())
            dialog.dismiss() // 다이얼로그 화면 닫기
        }

        dialogLayout.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경색을 투명하게 설정
        }
        dialog.show()
    }

}