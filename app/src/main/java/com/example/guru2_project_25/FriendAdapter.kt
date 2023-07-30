package com.example.guru2_project_25

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter(private val friends: List<Friend>) : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nicknameTextView: TextView = itemView.findViewById(R.id.tv_nickname)
        val nameTextView: TextView = itemView.findViewById(R.id.tv_friendUsername)
        val cheerupMsgButton: Button = itemView.findViewById(R.id.btn_cheerupMsg)

        private lateinit var currentFriend: Friend

        fun bind(friend: Friend) {
            currentFriend = friend
            nicknameTextView.text = friend.nickname
            nameTextView.text = friend.name
        }

        init {
            cheerupMsgButton.setOnClickListener {
                // 다이얼로그 생성
                val builder = AlertDialog.Builder(itemView.context)
                val inflater = LayoutInflater.from(itemView.context)
                val dialogLayout = inflater.inflate(R.layout.dialog, null)

                // 다이얼로그의 제목과 메시지 변경
                dialogLayout.findViewById<TextView>(R.id.tv_dialogTitle).text = "응원 메시지 작성"
                dialogLayout.findViewById<TextView>(R.id.tv_dialogInfo).text = "메시지를 입력하세요."

                // 다이얼로그에 EditText 추가
                val input = dialogLayout.findViewById<EditText>(R.id.et_editInfo)

                builder.setView(dialogLayout)
                val dialog = builder.create()

                // 다이얼로그의 확인 버튼 클릭 시 동작 정의
                dialogLayout.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
                    val message = input.text.toString()

                    // SharedPreferences에서 유저의 아이디 불러오기
                    val pref = itemView.context.getSharedPreferences("pref", Context.MODE_PRIVATE)
                    val userId = pref.getString("userId", "") ?: ""

                    // DB에 메시지 저장
                    val dbManager = DBManager(itemView.context, "appDB", null, 1)
                    val db = dbManager.writableDatabase
                    val values = ContentValues()
                    values.put("senderId", userId)
                    //values.put("receiverId", currentFriend.id)
                    values.put("content", message)
                    values.put("timestamp", System.currentTimeMillis())
                    db.insert("message", null, values)
                    db.close()

                    dialog.dismiss()
                }

                // 다이얼로그의 취소 버튼 클릭 시 동작 정의
                dialogLayout.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
                    dialog.dismiss()
                }

                if (dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                dialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return friends.size
    }
}