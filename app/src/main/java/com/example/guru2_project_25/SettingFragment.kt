package com.example.guru2_project_25;

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SettingFragment : Fragment() {

    private var userPw = ""         // 사용자 비밀번호 저장하는 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")

        // DB 연결
        val dbManager = DBManager(requireContext(), "user", null, 1)
        val db = dbManager.readableDatabase

        // 커서 설정
        val cursor = db.rawQuery("SELECT * FROM user WHERE id = '$userId'", null)

        if (cursor.moveToNext()) {
            // 유저의 정보 가져오기
            val idIndex = cursor.getColumnIndex("id")
            val id = cursor.getString(idIndex)
            val usernameIndex = cursor.getColumnIndex("userName")
            val username = cursor.getString(usernameIndex)
            val ikkiNameIndex = cursor.getColumnIndex("ikkiName")
            val ikkiName = cursor.getString(ikkiNameIndex)
            val pwIndex = cursor.getColumnIndex("pw")           //  db 통해 사용자 비밀번호 가져옴
            userPw = cursor.getString(pwIndex)

            // TextView에 유저의 정보 표시
            val tv_UserId = view.findViewById<TextView>(R.id.tv_userId)
            tv_UserId.text = id
            val tv_Username = view.findViewById<TextView>(R.id.tv_username)
            tv_Username.text = username
            val tv_ikkiName = view.findViewById<TextView>(R.id.tv_ikkiname)
            tv_ikkiName.text = ikkiName
            val tv_TitleName = view.findViewById<TextView>(R.id.tv_titleName)
            tv_TitleName.text = username + "님의 마이페이지"

        }

        cursor.close()
        db.close()

        val profileEditButton = view.findViewById<Button>(R.id.btn_edit) // 버튼 연결
        profileEditButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog, null)
            val password = dialogLayout.findViewById<EditText>(R.id.et_editInfo)          // 비밀번호 확인용 edittext 연결

            builder.setView(dialogLayout)
            val dialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
                if (checkPassword(password)) {
                    // 비밀번호 일치하면 정보 수정 페이지로 이동
                    val profileEdit = Intent(requireActivity(), EditProfileActivity::class.java)
                    startActivity(profileEdit)
                    dialog.dismiss()     // 다이얼로그 화면 닫기
                } else {
                    // 비밀번호 일치하지 않으면 오류 메시지 표시
                    Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            dialogLayout.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
                dialog.dismiss()
            }

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))       // 배경색을 투명하게 설정
            }
            dialog.show()

        }

        // 로그아웃
        val logoutButton = view.findViewById<Button>(R.id.btn_logout)

        logoutButton.setOnClickListener {
            // SharedPreferences에서 로그인 상태를 false로 변경
            val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("isLogin", false)
            editor.apply()

            val logout = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(logout)
            requireActivity().finish()             // fragment에서 액티비티 메서드를 사용하가위해 requireActivity() 사용
        }
    }

    // 수정된 정보를 바로 반영하기위해 onResume() 사용, onResume()은 프래그먼트가 화면에 표시될 때마다 호출됨
    override fun onResume() {
        super.onResume()

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")

        // DB 연결
        val dbManager = DBManager(requireContext(), "user", null, 1)
        val db = dbManager.readableDatabase

        // 커서 설정
        val cursor = db.rawQuery("SELECT * FROM user WHERE id = '$userId'", null)

        if (cursor.moveToNext()) {
            // 유저의 정보 가져오기
            val usernameIndex = cursor.getColumnIndex("userName")
            val username = cursor.getString(usernameIndex)
            val ikkiNameIndex = cursor.getColumnIndex("ikkiName")
            val ikkiName = cursor.getString(ikkiNameIndex)
            val pwIndex = cursor.getColumnIndex("pw")
            userPw = cursor.getString(pwIndex)

            // TextView에 유저의 정보 표시
            val tv_Username = view?.findViewById<TextView>(R.id.tv_username)
            tv_Username?.text = username
            val tv_ikkiName = view?.findViewById<TextView>(R.id.tv_ikkiname)
            tv_ikkiName?.text = ikkiName
            val tv_TitleName = view?.findViewById<TextView>(R.id.tv_titleName)
            tv_TitleName?.text = username + "님의 마이페이지"
        }

        cursor.close()
        db.close()
    }

    // 비밀번호 확인 : 입력된 문자열이 비밀번호와 같으면 true 아니면 false 반환
    private fun checkPassword(password: EditText): Boolean {
        return password.text.toString() == userPw
    }
}