package com.example.thegrowthdiaryofmoss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // 선택된 아이템(메뉴 아이콘)에 따라 해당 화면으로 전환
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.item_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.item_todo -> {
                    replaceFragment(TodoFragment())
                    true
                }
                R.id.item_friend -> {
                    replaceFragment(FriendFragment())
                    true
                }
                R.id.item_setting -> {
                    replaceFragment(SettingFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment()) // 선택된 화면이 없을 때, 기본화면은 Home화면

    }

    // fragment 교체
    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }

}