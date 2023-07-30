package com.example.guru2_project_25

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Create a data class to represent the counseling-related information
data class CounselingItem(val site: String, val phoneNumber: String)

class CounselingListAdapter(private val context: Context, private val items: List<CounselingItem>) : ArrayAdapter<CounselingItem>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.clinic_item, parent, false)

        val siteTextView = view.findViewById<TextView>(R.id.textViewName)
        val phoneNumberTextView = view.findViewById<TextView>(R.id.textViewPhoneNumber)

        val item = items[position]

        // Bind data to the TextViews
        siteTextView.text = item.site
        phoneNumberTextView.text = item.phoneNumber

        return view
    }
}

