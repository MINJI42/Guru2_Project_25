package com.example.thegrowthdiaryofmoss

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

class HomeFragment : Fragment() {

    private lateinit var db: SQLiteDatabase

    private val itemIDs = arrayOf(
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

    private val levelImageIDs = arrayOf(
        R.drawable.home_gift,
        R.drawable.home_img_ikki_level1,
        R.drawable.home_img_ikki_level2,
        R.drawable.home_img_ikki_level3,
        R.drawable.home_img_ikki_level4,
        R.drawable.home_img_ikki_level5
    )

    private val itemNames = arrayOf(
        "headphone",
        "hat",
        "devil",
        "bd",
        "crown",
        "flower",
        "cherry",
        "glasses",
        "sunglasses"
    )

    private fun getIkkiImageResource(level: Int, item: Int): Int {
        val itemIndex = itemIDs.indexOf(item)
        if (itemIndex == -1) {
            return levelImageIDs[level]
        }
        val itemName = itemNames[itemIndex]
        val resourceName = "home_img_ikki_level${level}_${itemName}"
        return resources.getIdentifier(resourceName, "drawable", requireContext().packageName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")
        val selectedItem = pref.getInt("selectedItem", 0)

        // DB 연결
        val dbManager = DBManager(requireContext(), "appDB", null, 1)
        db = dbManager.readableDatabase

        // 커서 설정
        val cursor = db.rawQuery("SELECT * FROM user WHERE id = '$userId'", null)

        if (cursor.moveToNext()) {
            // 유저의 정보 가져오기
            val ikkiNameIndex = cursor.getColumnIndex("ikkiName")
            val ikkiName = cursor.getString(ikkiNameIndex)
            val ikkiLevelIndex = cursor.getColumnIndex("ikkiLevel")
            var ikkiLevel = cursor.getInt(ikkiLevelIndex)
            val todoCountIndex = cursor.getColumnIndex("todoCount")
            val todoCount = cursor.getInt(todoCountIndex)

            // TextView에 유저의 정보 반영
            val tv_ikkiName = view.findViewById<TextView>(R.id.tv_home_ikkiname)
            tv_ikkiName?.text = ikkiName
            val tv_ikkiLevel = view.findViewById<TextView>(R.id.tv_level)
            tv_ikkiLevel?.text = "레벨 " + ikkiLevel

            // ImageView에 이끼의 이미지 반영
            val img_ikki = view.findViewById<ImageView>(R.id.ikki_img)
            img_ikki?.setImageResource(getIkkiImageResource(ikkiLevel, selectedItem))

            // ImageView 클릭 이벤트 리스너 추가
            img_ikki?.setOnClickListener {
                if (ikkiLevel == 0) {
                    // 이끼의 레벨 업데이트
                    ikkiLevel += 1
                    // DB 업데이트
                    db.execSQL("UPDATE user SET ikkiLevel=$ikkiLevel WHERE id='$userId'")
                    // TextView에 유저의 정보 반영
                    tv_ikkiLevel?.text = "레벨 " + ikkiLevel
                    // ImageView에 이끼의 이미지 반영
                    img_ikki?.setImageResource(R.drawable.home_img_ikki_level1)
                    // 텍스트
                    val ikkiSpeechText = view.findViewById<TextView>(R.id.tv_ikkiSpeech)
                    ikkiSpeechText.text = "만나서 반가워요!"
                    ikkiSpeechText.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        ikkiSpeechText.visibility = View.INVISIBLE
                    }, 3000)
                } else {    // 처음 클릭 이후에는 랜덤 응원메시지 출력
                    val randomTexts = arrayOf(
                        "네가 제일 소중해♡",
                        "오늘도 널 응원할게!",
                        "충분히 잘 하고 있어:)",
                        "이 또한 지나갈거야 :)",
                        "오늘하루도 정말 수고많았어:)",
                        "하루하루 행복하길 ♥ ",
                        "진짜 최고야!"
                    )
                    val randomText = randomTexts.random()
                    val ikkiSpeechText = view.findViewById<TextView>(R.id.tv_ikkiSpeech)
                    ikkiSpeechText.text = randomText
                    ikkiSpeechText.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        ikkiSpeechText.visibility = View.INVISIBLE
                    }, 3000)
                }
            }
        }

        cursor.close()

        val btn_call= view.findViewById<ImageButton>(R.id.callBtn)
        val btn_closet = view.findViewById<ImageButton>(R.id.btn_closet)

        //전화 아이콘 클릭시 상담센터 리스트 프레그먼트로 이동
        btn_call.setOnClickListener {
            val callFragment = CallFragment()
            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()

            fragmentManager.popBackStackImmediate()

            transaction.addToBackStack(null) //뒤로가기 버튼으로 되돌아가기
            transaction.replace(R.id.mainlayout, callFragment)
            transaction.commit()
        }

        // 옷장 아이콘 클릭시 옷장 화면으로 이동
        btn_closet.setOnClickListener {
            val intent = Intent(requireActivity(), ClosetActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        db.close()
    }

    // 수정된 정보를 바로 반영하기위해 onResume() 사용, onResume()은 프래그먼트가 화면에 표시될 때마다 호출됨
    override fun onResume() {
        super.onResume()

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")
        val selectedItem = pref.getInt("selectedItem", 0)

        // DB 연결
        if (!db.isOpen()) {
            val dbManager = DBManager(requireContext(), "appDB", null, 1)
            db = dbManager.readableDatabase
        }

        // 커서 설정
        val cursor = db.rawQuery("SELECT * FROM user WHERE id = '$userId'", null)

        if (cursor.moveToNext()) {
            // 유저의 정보 가져오기
            val ikkiLevelIndex = cursor.getColumnIndex("ikkiLevel")
            var ikkiLevel = cursor.getInt(ikkiLevelIndex)
            val todoCountIndex = cursor.getColumnIndex("todoCount")
            val todoCount = cursor.getInt(todoCountIndex)

            // 이끼의 레벨 업데이트
            if (todoCount >= 5 && ikkiLevel < 5) {
                when (todoCount) {
                    in 5..14 -> ikkiLevel = 1
                    in 15..29 -> ikkiLevel = 2
                    in 30..44 -> ikkiLevel = 3
                    in 45..59 -> ikkiLevel = 4
                    else -> ikkiLevel = 5
                }
                // DB 업데이트
                db.execSQL("UPDATE user SET ikkiLevel=$ikkiLevel WHERE id='$userId'")
            }

            // TextView에 유저의 정보 반영
            val tv_ikkiLevel = view?.findViewById<TextView>(R.id.tv_level)
            tv_ikkiLevel?.text = "레벨 " + ikkiLevel

            // ImageView에 이끼의 이미지 반영
            val iv_ikkiImage = view?.findViewById<ImageView>(R.id.ikki_img)
            iv_ikkiImage?.setImageResource(getIkkiImageResource(ikkiLevel, selectedItem))

            // ProgressBar에 todoCount 반영
            val pb_levelBar = view?.findViewById<ProgressBar>(R.id.pb_levelBar)
            pb_levelBar?.progress = todoCount * 2
        }

        cursor.close()
    }
}