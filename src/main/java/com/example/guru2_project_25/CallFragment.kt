package com.example.guru2_project_25

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView


class CallFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_call, container, false)

        val listViewCounseling = view.findViewById<ListView>(R.id.lv_counceling)

        // Dummy data for counseling-related sites and phone numbers
        val counselingItems = listOf(
            CounselingItem("국립건강정신센터", "123-456-7890"),
            CounselingItem("Counseling Site 2", "987-654-3210"),
            CounselingItem("Counseling Site 3", "555-555-5555")
            // Add more items here as needed
        )

        // Create and set the adapter for the ListView
        val adapter = CounselingListAdapter(requireContext(), counselingItems)
        listViewCounseling.adapter = adapter

        return view
    }
}

