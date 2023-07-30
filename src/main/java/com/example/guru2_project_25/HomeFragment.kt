package com.example.guru2_project_25;

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment


class HomeFragment : Fragment(), ClosetFragment.ClosetItemSelectionListener, ClosetItemListener {

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

    override var closetItemSelectionListener: ClosetFragment.ClosetItemSelectionListener?
        get() = TODO("Not yet implemented")
        set(value) {}

    var resourceID : Int = 0

    private lateinit var appDB: DBManager
    lateinit var sqLiteDatabase: SQLiteDatabase



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        id = arguments?.getInt("userId", -1)
//        appDB = DBManager(requireContext(), "myDB", null, 1)
//    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        val btnCloset = v.findViewById<ImageButton>(R.id.btn_closet)
        val btnLevelupTest = v.findViewById<Button>(R.id.btn_leveluptest)
        ikki_img = v.findViewById(R.id.ikki_img)
        tv_ikkiSpeech = v.findViewById(R.id.tv_ikkiSpeech)
        ikki_name = v.findViewById(R.id.ikkiname)

        btn_call=v.findViewById(R.id.callBtn)

        ikki_name.text="$id 님"

        //선물 이미지 클릭시 이끼1레벨 이미지로 전환
        ikki_img.setOnClickListener {
            isClickIkki = true
            ikki_img.setImageResource(R.drawable.ikki_lvel1)
            this.level += 1
            tv_level.text = "레벨 $level"

            val userId = getUserId()
            if (userId != null) {
                sqLiteDatabase.execSQL("UPDATE 'user' SET 'ikkiLevel'='$level' WHERE email=? ", arrayOf(userId))//이끼레벨 업데이트
            }


            ikki_img.isClickable = false //한번 클릭하면 다시 클릭해도 이미지 바뀌지 않도록
            tv_ikkiSpeech.isInvisible = true //선물클릭하라는 메세지 숨김

            if (isClickIkki == true) {
                tv_ikkiSpeech.isInvisible = false
                tv_ikkiSpeech.text = "안녕~ 반가워요!"

                // 일정 시간이 지나면 대사를 숨김
                Handler().postDelayed({ tv_ikkiSpeech.setVisibility(View.INVISIBLE) }, 3000)
            }

            if(isClickIkki==true){
                ikki_img.setOnClickListener{
                    val randomMessage = cheerMessages.random()
                    tv_ikkiSpeech.text=randomMessage
                    tv_ikkiSpeech.visibility=View.VISIBLE

                    Handler().postDelayed({ tv_ikkiSpeech.setVisibility(View.INVISIBLE) }, 3000)
                }
            }

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

//            if(resourceID==1)
//                ikki_img.setImageResource(R.drawable.ikki_lev1_headphone)

        }

        val num1 = arguments?.getInt("num1")
        val num2 = arguments?.getInt("num2")

        if(num1==1){
            ikki_img.setImageResource(R.drawable.ikki_lev1_headphone)
        }






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

        //레벨업 잘 되는지 테스트 하기 위함
        btnLevelupTest.setOnClickListener {

                pb_levelBar.progress += 10

                if (pb_levelBar.progress == 100) {
                    level += 1
                    tv_level.text = "레벨 $level"
                    pb_levelBar.progress = 0
                }

            //레벨업 시 이끼 이미지 바꾸기
            if(level>=5){
                ikki_img.setImageResource(R.drawable.ikki_level2)
            }
            if (level>=10){
                ikki_img.setImageResource(R.drawable.ikki_level4)
            }
            if (level>=15){
                ikki_img.setImageResource(R.drawable.ikki_level3)
            }
            if (level>=20)
                ikki_img.setImageResource(R.drawable.ikki_level5)
            }

        val userId = getUserId()
        if (userId != null) {
            val id = getIkkiNameFromDatabase(userId.toString())
            ikki_name.text = id
        }

        fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            id = arguments?.getInt("userId", -1)
            appDB = DBManager(requireContext(), "myDB", null, 1)
        }

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
        //closetFragment.closetItemSelectionListener = this

    }

    //이끼 이름 받아오기
    @SuppressLint("Range")
    private fun getIkkiNameFromDatabase(userId: String): String {
        val db = appDB.readableDatabase
        val cursor = db.rawQuery(" SELECT id FROM user WHERE email = 'abc' ", arrayOf(id.toString()))
        var userID = ""
        if (cursor.moveToFirst()) {
            userID = cursor.getInt(cursor.getColumnIndex("id")).toString()
        }
        cursor.close()
        db.close()
        return userID
    }

    //옷장 아이템 클릭시 홈화면의 이끼 이미지 변경
    override fun onClosetItemClicked(itemImageResource: Int) {
        Log.d("ClosetFragment", "Item clicked. ResourceID: $itemImageResource")
        resourceID = itemImageResource
        ikki_img.setImageResource(itemImageResource)
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






