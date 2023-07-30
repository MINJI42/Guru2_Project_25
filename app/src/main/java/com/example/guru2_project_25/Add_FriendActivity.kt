package com.example.guru2_project_25

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

@Suppress("DEPRECATION")
class Add_FriendActivity : AppCompatActivity() {

    private lateinit var friendId: TextInputEditText
    private lateinit var friendNickname : TextInputEditText
    private lateinit var friendAdd : Button
    private lateinit var prev : ImageButton
    private lateinit var myNickname : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfriend)

        friendId = findViewById(R.id.ev_inputFriend)
        friendNickname = findViewById(R.id.ev_inputNickname)
        myNickname = findViewById(R.id.ev_inputMyNickname)
        friendAdd = findViewById(R.id.btn_addFriend)
        prev = findViewById(R.id.imgbtn_prev)

        friendAdd.setOnClickListener {
            // 입력된 친구 ID와 별명 가져오기
            val friendId = friendId.text.toString()
            val friendNickname = friendNickname.text.toString()
            val myNickname = myNickname.text.toString()

            // SharedPreferences에서 유저의 아이디 불러오기
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            val userId = pref.getString("userId", "")

            if (friendId == userId) {
                // 자신을 친구로 추가하려는 경우
                Toast.makeText(this, "자신을 친구로 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // DB에서 친구 검색
                val dbManager = DBManager(this, "appDB", null, 1)
                val db = dbManager.readableDatabase
                val cursor = db.rawQuery("SELECT * FROM user WHERE id = '$friendId'", null)

                if (cursor.moveToNext()) {
                    // 친구가 존재하는 경우
                    // 이미 친구로 추가되어 있는지 검사
                    val cursor2 = db.rawQuery("SELECT * FROM friend WHERE userId = '$userId' AND friendId = '$friendId'", null)
                    if (cursor2.moveToNext()) {
                        // 이미 친구로 추가되어 있는 경우
                        Toast.makeText(this, "이미 등록된 친구입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 친구 목록에 추가
                        val values = ContentValues()
                        values.put("userId", userId)
                        values.put("friendId", friendId)
                        values.put("friendNickname", friendNickname)
                        db.insert("friend", null, values)

                        // 상대방의 친구 목록에도 추가
                        val values2 = ContentValues()
                        values2.put("userId", friendId)
                        values2.put("friendId", userId)
                        values2.put("friendNickname", myNickname) // 상대방의 친구 목록에서는 입력된 호칭을 사용
                        db.insert("friend", null, values2)

                        Toast.makeText(this, "친구가 추가되었습니다.", Toast.LENGTH_SHORT).show()

                        // 입력된 내용 지우기
                        this.friendId.setText("")
                        this.friendNickname.setText("")
                        this.myNickname.setText("")
                    }
                    cursor2.close()
                } else {
                    // 친구가 존재하지 않는 경우
                    Toast.makeText(this, "친구를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }

                cursor.close()
                db.close()
            }
        }

        prev.setOnClickListener {
            finish()
        }
    }
}