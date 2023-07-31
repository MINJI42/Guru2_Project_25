package com.example.guru2_project_25;

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment


class HomeFragment : Fragment(), ClosetFragment.ClosetItemSelectionListener {
    lateinit var ikki_name: TextView
    lateinit var tv_level: TextView
    lateinit var pb_levelBar: ProgressBar
    lateinit var ikki_img: ImageView
    lateinit var tv_ikkiSpeech: TextView

    var isClickIkki: Boolean = false

    lateinit var btn_call : ImageButton

    private val cheerMessages = arrayOf(
        "네가 제일 소중해♡",
        "오늘도 널 응원할게!",
        "충분히 잘 하고 있어:)",
        "이 또한 지나갈거야 :)",
        "오늘하루도 정말 수고많았어:)",
        "하루하루 행복하길 ♥ ",
        "진짜 최고야!"
    )

    var level: Int = 0

    var id: Int?= null

    fun getUserId(): Int? {
        return id
    }

    override fun onClosetItemSelectionConfirmed(itemImageResource: Int) {
        // Update the UI with the selected item
        ikki_img.setImageResource(itemImageResource)
    }

    var resourceID : Int = 0

    private lateinit var appDB: DBManager
    lateinit var sqLiteDatabase: SQLiteDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        val btnCloset = v.findViewById<ImageButton>(R.id.btn_closet)
        ikki_img = v.findViewById(R.id.ikki_img)
        tv_level = v.findViewById(R.id.tv_level)
        tv_ikkiSpeech = v.findViewById(R.id.tv_ikkiSpeech)
        ikki_name = v.findViewById(R.id.ikkiname)

        btn_call=v.findViewById(R.id.callBtn)

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")

        // DB 연결
        appDB = DBManager(requireContext(), "appDB", null, 1)
        sqLiteDatabase = appDB.readableDatabase

        // 커서 설정
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM user WHERE id = '$userId'", null)

        if (cursor.moveToNext()) {
            // 유저의 정보 가져오기
            val ikkiLevelIndex = cursor.getColumnIndex("ikkiLevel")
            val ikkiLevel = cursor.getInt(ikkiLevelIndex)

            // 레벨에 따라 이끼 이미지 변경
            when (ikkiLevel) {
                1 -> ikki_img.setImageResource(R.drawable.ikki_level1)
                2 -> ikki_img.setImageResource(R.drawable.ikki_level2)
                3 -> ikki_img.setImageResource(R.drawable.ikki_level3)
                4 -> ikki_img.setImageResource(R.drawable.ikki_level4)
                5 -> ikki_img.setImageResource(R.drawable.ikki_level5)
            }

            tv_level.text = "레벨 $ikkiLevel"
        }

        cursor.close()

        //선물 이미지 클릭시 이끼1레벨 이미지로 전환
        ikki_img.setOnClickListener {

            ikki_img.setImageResource(R.drawable.ikki_level1)
            if (userId != null) {
                sqLiteDatabase.execSQL("UPDATE 'user' SET 'ikkiLevel'=1 WHERE id=? ", arrayOf(userId))//이끼레벨 db업데이트
            }

        }

            //이끼 클릭하면 응원메세지 랜덤 출력
            if(isClickIkki==true){
                ikki_img.setOnClickListener{
                    val randomMessage = cheerMessages.random()
                    tv_ikkiSpeech.text=randomMessage
                    tv_ikkiSpeech.visibility=View.VISIBLE

                    Handler().postDelayed({ tv_ikkiSpeech.setVisibility(View.INVISIBLE) }, 3000)
                }
            }

        if(resourceID==1){
            ikki_img.setImageResource(R.drawable.ikki_lev1_headphone)
        }


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

        tv_level = v.findViewById(R.id.tv_level)
        pb_levelBar = v.findViewById(R.id.pb_levelBar)

        return v

    }

    companion object {
        fun newInstance(userId: String, itemImageResource: Int): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putInt("userId", userId)
            args.putInt("itemImageResource", itemImageResource)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closetFragment = ClosetFragment()

        // SharedPreferences에서 유저의 아이디 불러오기
        val pref = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", "")

        // DB 연결
        val dbManager = DBManager(requireContext(), "appDB", null, 1)
        val db = dbManager.readableDatabase

        // 커서 설정
        val cursor = db.rawQuery("SELECT * FROM user WHERE id = '$userId'", null)

        if (cursor.moveToNext()) {
            // 유저의 정보 가져오기
            val ikkiNameIndex = cursor.getColumnIndex("ikkiName")
            val ikkiName = cursor.getString(ikkiNameIndex)

            // TextView에 유저의 정보 표시
            ikki_name.text = ikkiName + "님"
        }

        cursor.close()
        db.close()

        closetFragment.closetFragmentClosetItemListener = this

    }

    private fun showClosetFragment() {
        val closetFragment = ClosetFragment()
        //closetFragment.closetItemSelectionListener = this
        closetFragment.setTargetFragment(this, 1) // 1 is the requestCode, you can use any unique number
        // Now, when an item is clicked in the ClosetFragment, the result will be sent back to this HomeFragment
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()

       // fragmentManager.popBackStackImmediate()

        transaction.addToBackStack(null)
        transaction.replace(R.id.mainlayout, closetFragment)
        transaction.commit()
    }

}

private fun Bundle.putInt(s: String, userId: String): String {
return userId.toString()
}






