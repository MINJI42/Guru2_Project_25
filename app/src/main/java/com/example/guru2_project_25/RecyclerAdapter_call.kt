package com.example.guru2_project_25

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Create a data class to represent the counseling-related information
data class CounselingItem(val name: String, val info:String, val phoneNumber: String, val site :String)

class CounselingListAdapter(private val context: Context, private val items: List<CounselingItem>) : ArrayAdapter<CounselingItem>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.clinic_item, parent, false)

        val NameTextView = view.findViewById<TextView>(R.id.textViewName)
        val InfoTextView:TextView=view.findViewById<TextView>(R.id.textViewInfo)
        val phoneNumberTextView = view.findViewById<TextView>(R.id.textViewPhoneNumber)
        val SiteTextView = view.findViewById<TextView>(R.id.textViewSite)
        val btnClinicSiteConnect = view.findViewById<Button>(R.id.btn_clinicSiteConnect)
        val btnClinicCalling = view.findViewById<Button>(R.id.btn_clinicCalling)
        val item = items[position]



        // Bind data to the TextViews
        NameTextView.text = item.name
        InfoTextView.text = item.info
        phoneNumberTextView.text = item.phoneNumber
        SiteTextView.text=item.site


        btnClinicSiteConnect.setOnClickListener {
            val siteUrl = item.site
            openSiteInBrowser(siteUrl)
        }

        btnClinicCalling.setOnClickListener {
            val phoneNumber = item.phoneNumber
            makePhoneCall(phoneNumber)
        }

        return view
    }

    private fun openSiteInBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // 사이트를 열 수 없는 경우, 에러 메시지 또는 처리를 추가할 수 있습니다.
        }
    }

    private fun makePhoneCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // 전화를 걸 수 없는 경우, 에러 메시지 또는 처리를 추가할 수 있습니다.
        }
    }
}

