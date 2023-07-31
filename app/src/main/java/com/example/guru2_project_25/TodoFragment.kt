package com.example.guru2_project_25;

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DateFormat
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

        // 유저 id 가져오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")

        // 레이아웃 연결
        val layout_todo = view.findViewById<LinearLayout>(R.id.layout_todo)
        layout_todo.orientation = LinearLayout.VERTICAL
        layout_todo.setPadding(10, 50, 0, 20)
        // 컨텐츠 연결
        val fab_add = view.findViewById<FloatingActionButton>(R.id.fab_add)
        val fab_modify = view.findViewById<FloatingActionButton>(R.id.fab_modify)
        val tv_per = view.findViewById<TextView>(R.id.tv_per)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val dateFormat : DateFormat = SimpleDateFormat("yyyyMMdd")
        var selectDay = Date(calendarView.date)
        var date: Int = (dateFormat.format(selectDay)).toInt()

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater


        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            if(month<9) {
                if(dayOfMonth<10) {
                    date = (""+year+0+(month+1)+0+dayOfMonth).toInt()
                } else {
                    date = (""+year+0+(month+1)+dayOfMonth).toInt()
                }
            } else {
                if(dayOfMonth<10) {
                    date = (""+year+(month+1)+0+dayOfMonth).toInt()
                } else {
                    date = (""+year+(month+1)+dayOfMonth).toInt()
                }
            }
            tv_per.text = "0%"
            updateList(dbManager, layout_todo, tv_per, date, userId)
        }


        // 투두리스트 출력하기
        updateList(dbManager, layout_todo, tv_per, date, userId)


        // 투두리스트 추가하기
        fab_add.setOnClickListener {
            val todoDialog = inflater.inflate(R.layout.dialog_todo, null)

            // 다이얼로그 출력
            builder.setView(todoDialog)
            val dialog = builder.create()
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
                sqLiteDatabase.execSQL("INSERT INTO todo VALUES ('$userId', '$date', '$addTodo', 0);")
                sqLiteDatabase.close()
                Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                // 투두리스트 갱신
                updateList(dbManager, layout_todo, tv_per, date, userId)
            }
            btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        // 투두리스트 수정하기
        fab_modify.setOnClickListener {

            var sqLiteDatabase = dbManager.readableDatabase
            var cursor = sqLiteDatabase.rawQuery("SELECT todo FROM todo WHERE userId = '$userId' AND date = '$date';",null)
            var listCount: Int = 0 // 추가
            layout_todo.removeAllViews()

            // 투두리스트(tv) 출력
            while(cursor.moveToNext()) {
                var listTodo: TextView = TextView(activity)
                listTodo.text = cursor.getString(0).toString()
                listTodo.textSize = 20f
                listTodo.id = listCount
                layout_todo.addView(listTodo)
                listCount++ // 추가

                // 수정할 투두 터치
                listTodo.setOnClickListener {
                    val todoDialog = inflater.inflate(R.layout.dialog_todomodify, null)

                    // 다이얼로그 출력
                    builder.setView(todoDialog)
                    val dialog = builder.create()
                    if (dialog.window != null) {
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                    dialog.show()

                    // 다이얼로그 연결
                    val btn_modify = todoDialog.findViewById<Button>(R.id.btn_modify)
                    val btn_cancel = todoDialog.findViewById<Button>(R.id.btn_cancel)
                    val et_modifyTodo = todoDialog.findViewById<EditText>(R.id.et_modifyTodo)
                    var beforeTodo = listTodo.text.toString()

                    // 수정 버튼 터치
                    btn_modify.setOnClickListener {
                        var modifyTodo: String = et_modifyTodo.text.toString()

                        if(modifyTodo != "")
                        {
                            var dbTodo = dbManager.writableDatabase
                            // 공백x -> 수정. 튜플 갱신
                            dbTodo.execSQL("UPDATE todo SET todo = '$modifyTodo' WHERE todo = '$beforeTodo';")
                            dbTodo.close()
                        } else {
                            // 공백 -> 삭제. 튜플 삭제 및 체크 유무에 따라 user DB todoCount 변경
                            var sqLiteDatabase = dbManager.readableDatabase
                            var checkCursor = sqLiteDatabase.rawQuery("SELECT checked FROM todo WHERE todo = '$beforeTodo' ;", null)
                            checkCursor.moveToFirst()
                            // 삭제하려는 투두가 체킹된 투두 -> 카운트 1 감소.
                            if(checkCursor.getInt(0) == 1) {
                                var sqLiteDatabase = dbManager.writableDatabase
                                sqLiteDatabase.execSQL("UPDATE user SET todoCount = todoCount - 1 WHERE id = '$userId';")
                                sqLiteDatabase.close()
                            }
                            checkCursor.close()
                            sqLiteDatabase.close()
                            // 튜플 삭제
                            var dbTodo = dbManager.writableDatabase
                            dbTodo.execSQL("DELETE FROM todo WHERE todo = '$beforeTodo';")
                            dbTodo.close()
                        }
                        Toast.makeText(activity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        // 투두리스트 갱신
                        updateList(dbManager, layout_todo, tv_per, date, userId)
                    }
                    // 취소 버튼 터치
                    btn_cancel.setOnClickListener {
                        dialog.dismiss()
                        updateList(dbManager, layout_todo, tv_per, date, userId)
                    }
                }
            }

            Toast.makeText(activity, "수정할 리스트를 선택해주세요.", Toast.LENGTH_SHORT).show()
            cursor.close()
        }
    }

    private fun todoPer(dbManager: DBManager, tv_per: TextView, date: Int, userId: String? ) {
        var sqLiteDatabase = dbManager.readableDatabase
        var cursor = sqLiteDatabase.rawQuery("SELECT checked FROM todo WHERE date = '$date';", null)
        var allTodo = cursor.getCount()
        var completeTodo : Int = 0
        // 투두리스트 달성률 계산, 출력

        while(cursor.moveToNext()) {
            if(cursor.getInt(0) == 1) { completeTodo++ }
        }
        tv_per.text = ((completeTodo*100)/allTodo).toString() + "%"
    }

    private fun updateList(dbManager: DBManager, layout_todo: LinearLayout, tv_per: TextView, date: Int, userId: String?) {

        var sqLiteDatabase = dbManager.readableDatabase
        var cursor = sqLiteDatabase.rawQuery("SELECT * FROM todo WHERE userId = '$userId' AND date = '$date';",null)
        var listCount: Int = 0
        layout_todo.removeAllViews()

        // 투두리스트(cb) 출력
        while(cursor.moveToNext()) {
            var todoIndex = cursor.getColumnIndex("todo")
            var todo = cursor.getString(todoIndex).toString()
            var checkedIndex = cursor.getColumnIndex("checked")
            var checked = cursor.getString(checkedIndex).toInt()
            var chkTodo: CheckBox = CheckBox(activity)
            chkTodo.text = todo
            chkTodo.textSize = 20f
            chkTodo.id = listCount
            layout_todo.addView(chkTodo)
            listCount++

            var percentage: String
            var allTodo: String
            var clearTodo: String

            // 투두 별 체크 유무 표시
            if(checked == 1) { chkTodo.setChecked(true) }

            else { chkTodo.setChecked(false) }

            // 퍼센테이지 표시
            todoPer(dbManager, tv_per, date, userId)

            // 투두 체크 관련 이벤트
            chkTodo.setOnClickListener {
                if(chkTodo.isChecked)
                {
                    sqLiteDatabase.execSQL("UPDATE user SET todoCount = todoCount+1 WHERE id = '$userId';")
                    sqLiteDatabase.execSQL("UPDATE todo SET checked = 1 WHERE todo = '$todo';")
                    // 퍼센테이지 갱신
                    todoPer(dbManager, tv_per, date, userId)
                } else {
                    sqLiteDatabase.execSQL("UPDATE user SET todoCount = todoCount-1 WHERE id = '$userId';")
                    sqLiteDatabase.execSQL("UPDATE todo SET checked = 0 WHERE todo = '$todo';")
                    // 퍼센테이지 갱신
                    todoPer(dbManager, tv_per, date, userId)
                }
            }
        }
        cursor.close()
    }
}