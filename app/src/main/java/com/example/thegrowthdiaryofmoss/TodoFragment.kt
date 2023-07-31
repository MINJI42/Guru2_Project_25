package com.example.thegrowthdiaryofmoss

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
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
        val intent = Intent(activity, MainActivity::class.java)
        var user_email = intent.getStringExtra("intent_email").toString()

        // 레이아웃 연결
        val layout_todo = view.findViewById<LinearLayout>(R.id.layout_todo)
        layout_todo.orientation = LinearLayout.VERTICAL
        layout_todo.setPadding(10, 50, 0, 20)
        // 버튼 연결
        val fab_add = view.findViewById<FloatingActionButton>(R.id.fab_add)
        val fab_modify = view.findViewById<FloatingActionButton>(R.id.fab_modify)

        var date = SimpleDateFormat("yyyyMMdd").format(Date()).toInt()

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater

        // 투두리스트 출력하기
        updateList(dbManager, layout_todo, date)

        // 투두리스트 추가하기
        fab_add.setOnClickListener {
            //val builder = AlertDialog.Builder(requireContext())
            //val inflater = layoutInflater
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

            btn_confirm.setOnClickListener {
                // 삽입
                var sqLiteDatabase = dbManager.writableDatabase
                var addTodo: String = et_addTodo.text.toString()
                //sqLiteDatabase.execSQL("INSERT INTO todo VALUES ('"
                //        +user_email+"', '"+date+"', '"+listCount+"', '"+addTodo+"' );")
                sqLiteDatabase.execSQL("INSERT INTO todo VALUES ('"
                        +user_email+"', '"+date+"', '"+addTodo+"' );")

                sqLiteDatabase.close()
                Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                // 투두리스트 갱신
                updateList(dbManager, layout_todo, date)
            }
            btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        // 투두리스트 수정하기
        fab_modify.setOnClickListener {

            var sqLiteDatabase = dbManager.readableDatabase
            var cursor = sqLiteDatabase.rawQuery("SELECT * FROM todo WHERE date = '"+date+"';",null)
            var listCount: Int = 0 // 추가
            layout_todo.removeAllViews()

            // 투두리스트(tv) 출력
            while(cursor.moveToNext()) {
                var todoIndex = cursor.getColumnIndex("todo")
                var todo = cursor.getString(todoIndex).toString()
                var listTodo: TextView = TextView(activity)
                listTodo.text = todo
                listTodo.textSize = 20f
                listTodo.id = listCount
                layout_todo.addView(listTodo)
                listCount++ // 추가

                listTodo.setOnClickListener {

                    val todoDialog = inflater.inflate(R.layout.dialog_todomodify, null)

                    // 다이얼로그 출력
                    builder.setView(todoDialog)
                    val dialog = builder.create()
                    if (dialog.window != null) {
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))       // 배경색을 투명하게 설정
                    }
                    dialog.show()

                    // 다이얼로그 아이템 연결
                    val btn_modify = todoDialog.findViewById<Button>(R.id.btn_modify)
                    val btn_cancel = todoDialog.findViewById<Button>(R.id.btn_cancel)
                    val et_modifyTodo = todoDialog.findViewById<EditText>(R.id.et_modifyTodo)
                    var beforeTodo = listTodo.text.toString()

                    btn_modify.setOnClickListener {
                        // 수정
                        var sqLiteDatabase = dbManager.writableDatabase
                        var modifyTodo: String = et_modifyTodo.text.toString()

                        if(modifyTodo != "")
                        {
                            sqLiteDatabase.execSQL("UPDATE todo SET todo = '"+
                                    modifyTodo+"' WHERE todo = '"+beforeTodo+"';")
                        } else {
                            sqLiteDatabase.execSQL("DELETE FROM todo WHERE todo = '"+beforeTodo+"';")
                        }

                        sqLiteDatabase.close()
                        Toast.makeText(activity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        // 투두리스트 갱신
                        updateList(dbManager, layout_todo, date)
                    }
                    btn_cancel.setOnClickListener {
                        dialog.dismiss()
                        updateList(dbManager, layout_todo, date)
                    }

                }

            }

            Toast.makeText(activity, "수정할 리스트를 선택해주세요.", Toast.LENGTH_SHORT).show()
            cursor.close()
        }

    }

    private fun updateList(dbManager: DBManager, layout_todo: LinearLayout, date: Int) {

        var sqLiteDatabase = dbManager.readableDatabase
        var cursor = sqLiteDatabase.rawQuery("SELECT * FROM todo WHERE date = '"+date+"';",null)
        var listCount: Int = 0 // 추가
        layout_todo.removeAllViews()

        // 투두리스트(cb) 출력

        while(cursor.moveToNext()) {
            var layout_item: LinearLayout = LinearLayout(activity)
            var todoIndex = cursor.getColumnIndex("todo")
            //var listIndex = cursor.getColumnIndex("listIndex")
            var todo = cursor.getString(todoIndex).toString()
            var chkTodo: CheckBox = CheckBox(activity)
            //listCount = cursor.getInt(listIndex)
            chkTodo.text = todo
            chkTodo.textSize = 20f
            chkTodo.id = listCount
            layout_todo.addView(chkTodo)
            listCount++ // 추가
        }

        cursor.close()

    }

    private fun modifyList(dbManager: DBManager, layout_todo: LinearLayout, date: Int) {}

}