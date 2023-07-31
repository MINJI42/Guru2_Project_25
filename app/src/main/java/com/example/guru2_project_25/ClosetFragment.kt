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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class ClosetFragment : Fragment() {
    interface ClosetItemSelectionListener{
        //fun onClosetItemClicked(itemImageResource:Int)
       fun onClosetItemSelectionConfirmed(itemImageResource : Int)
//        var closetItemSelectionListener: ClosetItemSelectionListener?
    }

    var resourceID: Int = 0
    var closetFragmentClosetItemListener: ClosetItemSelectionListener? = null
   // var closetItemListener: ClosetItemListener? = null

    lateinit var img_array_item: Array<ImageButton?>
    var selectItem: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_closet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val closetItemImages = arrayOf(
            R.drawable.ikki_headphone,
            R.drawable.ikki_hat,
            R.drawable.ikki_devil,
            R.drawable.ikki_bd,
            R.drawable.ikki_crown,
            R.drawable.ikki_flower,
            R.drawable.ikki_cherry,
            R.drawable.ikki_glasses,
            R.drawable.ikki_sunglasses
        )



        img_array_item = arrayOf(
            view.findViewById(R.id.IB_ikkiItem1),
            view.findViewById(R.id.IB_ikkiItem2),
            view.findViewById(R.id.IB_ikkiItem3),
            view.findViewById(R.id.IB_ikkiItem4),
            view.findViewById(R.id.IB_ikkiItem5),
            view.findViewById(R.id.IB_ikkiItem6),
            view.findViewById(R.id.IB_ikkiItem7),
            view.findViewById(R.id.IB_ikkiItem8),
            view.findViewById(R.id.IB_ikkiItem9)
        )

        // 각 이미지 버튼 클릭시 해당 아이템을 선택할거냐는 알림창 뜨게 함
        for (i in 0 until img_array_item.size) {
            img_array_item[i]?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val builder = AlertDialog.Builder(requireContext())
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.closetdialog, null)

                    val imageView = dialogLayout.findViewById<ImageView>(R.id.iv_applyImage)

                    imageView.setImageResource(closetItemImages[i])
                    resourceID = closetItemImages[i]

                    closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
//                    closetItemListener?.onClosetItemClicked(resourceID)

                    if (closetFragmentClosetItemListener != null) {
                        // 각 이미지 버튼 클릭하면 해당 아이템 이미지가 알림창에 뜨도록
                        when (i) {
                            0 -> {
                                imageView.setImageResource(R.drawable.ikki_headphone)
                                resourceID = R.drawable.ikki_lev1_headphone
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                            1 -> {
                                imageView.setImageResource(R.drawable.ikki_hat)
                                resourceID = R.drawable.ikki_lev1_headphone
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                            2 -> {
                                imageView.setImageResource(R.drawable.ikki_devil)
                                selectItem = 2
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                            3 -> {
                                imageView.setImageResource(R.drawable.ikki_bd)
                                selectItem = 3
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                            4 -> {
                                imageView.setImageResource(R.drawable.ikki_crown)
                                selectItem = 4
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                            5 -> {
                                imageView.setImageResource(R.drawable.ikki_flower)
                                selectItem = 5
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                            6 -> {
                                imageView.setImageResource(R.drawable.ikki_cherry)
                                selectItem = 6
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                            7 -> {
                                imageView.setImageResource(R.drawable.ikki_glasses)
                                selectItem = 7
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                            8 -> {
                                imageView.setImageResource(R.drawable.ikki_sunglasses)
                                selectItem = 8
                                closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                            }

                        }
                        closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)

                    }
                   
                    builder.setView(dialogLayout)
                    val dialog = builder.create()

                    dialogLayout.findViewById<Button>(R.id.btn_closetApply)?.setOnClickListener {
                        dialog.dismiss() // Close the dialog
                        val myToast =
                            Toast.makeText(requireContext(), "적용되었습니다.", Toast.LENGTH_SHORT)
                        myToast.show()

                        // Pass the resource ID of the selected item image
                        img_array_item[i]?.tag?.let { resourceId ->
                           closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceId as Int) }
                        closetFragmentClosetItemListener?.onClosetItemSelectionConfirmed(resourceID)
                        dialog.dismiss()

                        // 적용되었을 경우 홈화면으로 돌아가기
                        val fragmentManager: FragmentManager =
                            requireActivity().supportFragmentManager
                        fragmentManager.popBackStack()
                    }

                    dialogLayout.findViewById<Button>(R.id.btn_closetCancel)?.setOnClickListener {
                        val myToast =
                            Toast.makeText(requireContext(), "취소되었습니다.", Toast.LENGTH_SHORT)
                        myToast.show()
                        dialog.dismiss()
                    }

//                    if (dialog.window != null) {
//                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))       // 배경색을 투명하게 설정
//                    }
                    dialog.show()


                }
            }

            )


        }


    }

}














