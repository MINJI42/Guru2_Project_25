package com.example.guru2_project_25

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TutorialActivity2 : AppCompatActivity() {

    lateinit var btn_next : Button
    lateinit var btn_prev : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial2)

        btn_next = findViewById(R.id.btn_nextTutoial)
        btn_prev = findViewById(R.id.btn_prevLogin)

        btn_prev.setOnClickListener {
            val prev = Intent(this, TutorialActivity1::class.java)
            startActivity(prev)
        }

        btn_next.setOnClickListener {
            val next= Intent(this, TutorialActivity3::class.java)
            startActivity(next)

        }
    }

}