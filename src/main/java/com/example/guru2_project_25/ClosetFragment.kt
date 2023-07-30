package com.example.guru2_project_25;

import android.app.AlertDialog
import android.content.Context
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
        fun onClosetItemSelectionConfirmed(itemImageResource : Int)
        var closetItemSelectionListener: ClosetItemSelectionListener?
    }

    var resourceID: Int = 0
    var closetItemListener: ClosetItemListener? = null

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
            // Add other closet item images here
        )

//        // Find the GridView in the layout
//        val gridView: GridView = view.findViewById(R.id.gridView)
//
//        // Create an instance of the ImageAdapter and set it to the GridView
//        val adapter = ImageAdapter(requireContext(), closetItemImages)
//        gridView.adapter = adapter
//
//        // Set item click listener for the GridView
//        gridView.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, v, position, id ->
//                val selectedItemImageResource = closetItemImages[position]
//                closetItemListener?.onClosetItemClicked(selectedItemImageResource)
//            }


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


//                    closetItemListener?.onClosetItemClicked(resourceID)

                    // 각 이미지 버튼 클릭하면 해당 아이템 이미지가 알림창에 뜨도록
                    when (i) {
                        0 -> {
                            imageView.setImageResource(R.drawable.ikki_headphone)
                            resourceID = 1
                            closetItemListener?.onClosetItemClicked(1)
                        }

                        1 -> {
                            imageView.setImageResource(R.drawable.ikki_hat)
                            resourceID = 2
                            closetItemListener?.onClosetItemClicked(resourceID)
                        }

                        2 -> {
                            imageView.setImageResource(R.drawable.ikki_devil)
                            selectItem = 2
                            closetItemListener?.onClosetItemClicked(resourceID)
                        }

                        3 -> {
                            imageView.setImageResource(R.drawable.ikki_bd)
                            selectItem = 3
                            closetItemListener?.onClosetItemClicked(resourceID)
                        }

                        4 -> {
                            imageView.setImageResource(R.drawable.ikki_crown)
                            selectItem = 4
                            closetItemListener?.onClosetItemClicked(resourceID)
                        }

                        5 -> {
                            imageView.setImageResource(R.drawable.ikki_flower)
                            selectItem = 5
                            closetItemListener?.onClosetItemClicked(resourceID)
                        }

                        6 -> {
                            imageView.setImageResource(R.drawable.ikki_cherry)
                            selectItem = 6
                            closetItemListener?.onClosetItemClicked(resourceID)
                        }

                        7 -> {
                            imageView.setImageResource(R.drawable.ikki_glasses)
                            selectItem = 7
                            closetItemListener?.onClosetItemClicked(resourceID)
                        }

                        8 -> {
                            imageView.setImageResource(R.drawable.ikki_sunglasses)
                            selectItem = 8
                            closetItemListener?.onClosetItemClicked(resourceID)
                        }
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
                            closetItemListener?.onClosetItemClicked(resourceId as Int)
                        }
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
                    dialog.show()


                }
            }

            )

////            // Store the resource ID of each image button in the tag
//            img_array_item[i]?.tag = img_array_item[i]?.drawable?.constantState?.javaClass
//        }


        }

//        val bundle = Bundle()
//        bundle.putString("key", "value")
//
//        val passBundleBFragment = HomeFragment()
//        passBundleBFragment.arguments = bundle parentFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container_bundle, PassBundleFragment())
//            .commit()

    }
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is ClosetItemSelectionListener) {
//            closetItemListener = context
//        } else {
//            throw RuntimeException("$context must implement ClosetItemSelectionListener")
//        }
//    }

//    override fun onDetach() {
//        super.onDetach()
//        closetItemSelectionListener = null
//    }
}














