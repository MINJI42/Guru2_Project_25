package com.example.guru2_project_25;

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
import android.widget.EditText
import android.widget.Toast

class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileEditButton = view.findViewById<Button>(R.id.btn_edit) // 버튼 연결

        profileEditButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.settingdialog, null)
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
            val logout = Intent(requireActivity(), LoginActivity::class.java)
            // logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 이전 액티비티 제거 (이전 액티비티 뜨지 않는 방법 찾아야 함)
            startActivity(logout)
        }
    }

    // 비밀번호 확인
    private fun checkPassword(password: EditText): Boolean {
        return password.text.toString() == "password"
    }

}