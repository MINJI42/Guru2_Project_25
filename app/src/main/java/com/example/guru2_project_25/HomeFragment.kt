package com.example.guru2_project_25;

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.w3c.dom.Text

class HomeFragment : Fragment() {


    lateinit var tv_level : TextView
    lateinit var pb_levelBar : ProgressBar
    var level : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        val btnCloset = v.findViewById<ImageButton>(R.id.btn_closet)
        val btnLevelupTest = v.findViewById<Button>(R.id.btn_leveluptest)

        //옷장 이미지 클릭 시 프레그먼트 전환
        btnCloset.setOnClickListener {
            // 프레그먼트 전환
            val closetFragment = ClosetFragment()
            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()

            fragmentManager.popBackStackImmediate()

            transaction.addToBackStack(null) //뒤로가기 버튼으로 되돌아가기
            transaction.replace(R.id.mainlayout, closetFragment)
            transaction.commit()
        }

        tv_level = v.findViewById(R.id.tv_level)
        pb_levelBar = v.findViewById(R.id.pb_levelBar)

        //레벨업 잘 되는지 테스트 하기 위함
        btnLevelupTest.setOnClickListener {

                pb_levelBar.progress += 10

                if (pb_levelBar.progress == 100) {
                    level += 1
                    tv_level.text = "레벨 $level"
                    pb_levelBar.progress = 0
                }
            }
        return v
    }

}






