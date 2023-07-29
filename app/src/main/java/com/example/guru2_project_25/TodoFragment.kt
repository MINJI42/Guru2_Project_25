package com.example.guru2_project_25;

import android.app.AlertDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date

class TodoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB 연결
        val dbManager = DBManager(activity, "appDB", null, 1)
        var sqLiteDatabase = dbManager.readableDatabase
        val intent = Intent(activity, MainActivity::class.java)
        val user_email = intent.getStringExtra("intent_email").toString()

        // 레이아웃 연결
        val layout_todo = view.findViewById<LinearLayout>(R.id.layout_todo)
        // 버튼 연결
        val fab_add = view.findViewById<FloatingActionButton>(R.id.fab_add)
        val fab_modify = view.findViewById<FloatingActionButton>(R.id.fab_modify)



        // 투두리스트 출력



        // 투두리스트 추가하기
        fab_add.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val todoDialog = inflater.inflate(R.layout.dialog_todo, null)

            // 다이얼로그 출력
            builder.setView(todoDialog)
            val dialog = builder.create()
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))       // 배경색을 투명하게 설정
            }
            dialog.show()

            // 다이얼로그 아이템 연결
            val btn_confirm = todoDialog.findViewById<Button>(R.id.btn_confirm)
            val btn_cancel = todoDialog.findViewById<Button>(R.id.btn_cancel)
            val et_addTodo = todoDialog.findViewById<EditText>(R.id.et_addTodo)
            val date = SimpleDateFormat("yyyyMMdd").format(Date()).toInt()

            btn_confirm.setOnClickListener {
                // 삽입
                var addTodo: String = et_addTodo.text.toString()
                sqLiteDatabase = dbManager.writableDatabase
                sqLiteDatabase.execSQL("INSERT INTO todo VALUES ('"
                        +user_email+"', '"+date+"', '"+addTodo+"' );")
                sqLiteDatabase.close()
                Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
        }

    }
}