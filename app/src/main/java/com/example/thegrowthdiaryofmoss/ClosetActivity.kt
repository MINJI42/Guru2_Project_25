package com.example.thegrowthdiaryofmoss

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ClosetActivity : AppCompatActivity() {
    private var resourceID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet)

        val imgbtnIDs = arrayOf(
            R.id.IB_ikkiItem1,
            R.id.IB_ikkiItem2,
            R.id.IB_ikkiItem3,
            R.id.IB_ikkiItem4,
            R.id.IB_ikkiItem5,
            R.id.IB_ikkiItem6,
            R.id.IB_ikkiItem7,
            R.id.IB_ikkiItem8,
            R.id.IB_ikkiItem9
        )
        val itemIDs = arrayOf(
            R.drawable.home_closet_ikki_headphone,
            R.drawable.home_closet_ikki_hat,
            R.drawable.home_closet_ikki_devil,
            R.drawable.home_closet_ikki_bd,
            R.drawable.home_closet_ikki_crown,
            R.drawable.home_closet_ikki_flower,
            R.drawable.home_closet_ikki_cherry,
            R.drawable.home_closet_ikki_glasses,
            R.drawable.home_closet_ikki_sunglasses
        )

        for (i in imgbtnIDs.indices) {
            val imgbtn = findViewById<ImageButton>(imgbtnIDs[i])
            imgbtn.setOnClickListener {
                resourceID = itemIDs[i]
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.dialog_closet, null)
                builder.setView(dialogView)
                val dialog = builder.create()

                val iv_applyImage = dialogView.findViewById<ImageView>(R.id.iv_applyImage)
                iv_applyImage.setImageResource(itemIDs[i])

                val btn_closetCancel = dialogView.findViewById<Button>(R.id.btn_closetCancel)
                btn_closetCancel.setOnClickListener {
                    // 취소 버튼 클릭시 처리
                    val myToast =
                        Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT)
                    myToast.show()
                    dialog.dismiss()
                }

                val btn_closetApply = dialogView.findViewById<Button>(R.id.btn_closetApply)
                btn_closetApply.setOnClickListener {
                    // 확인 버튼 클릭시 처리
                    val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putInt("selectedItem", resourceID)
                    editor.apply()
                    dialog.dismiss() // Close the dialog
                    val myToast =
                        Toast.makeText(this, "적용되었습니다.", Toast.LENGTH_SHORT)
                    myToast.show()
                }
                if (dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))       // 배경색을 투명하게 설정
                }
                dialog.show()
            }
        }

        var prev = findViewById<ImageButton>(R.id.imgbtn_prev3)
        prev.setOnClickListener {
            finish()
        }
    }
}
