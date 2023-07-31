package com.example.thegrowthdiaryofmoss

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView


class CallFragment : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_call, container, false)

        val listViewCounseling = view.findViewById<ListView>(R.id.lv_counceling)

        
        // Dummy data for counseling-related sites and phone numbers
        val counselingItems = listOf(
            CounselingItem("국립건강정신센터", "다양한 정신건강문제에 대해 의료적 및 심리적 개입을 진행하여 치료합니다. \n 평일 08:30 ~ 17:00 (토요일 및 공휴일은 휴무)","02-2204-0114", "https://www.ncmh.go.kr/ncmh/main.do"),
            CounselingItem("한국심리상담센터", "심리,정서적으로 어려움을 겪고 있는 사람들을 위한 종합심리상담센터로 각 분야별 맞춤형 상담을 진행합니다.\n 평일 10:00 ~ 20:00(일요일은 휴무)","031-226-9197", "http://www.mykpcc.com/"),
            CounselingItem("건강가정지원센터", "긴급, 일시적인 시간제 돌봄서비스인 아이돌봄서비스 지원 및 가정생활 전반에 관한 문제에 대한 상담을 진행합니다.\n 센터별 이용시간 상이" ,"1577-9337", "https://www.familynet.or.kr/web/index.do"),
            CounselingItem("여성가족부 가족상담", "다양한 가족 대상 양육비 이행, 출산 및 양육, 임신‧출산 갈등상담, 가족 갈등 관련 심리‧정서상담 등에 대한 상담을 진행합니다. \n 지원내용에 따라 이용시간 상이","1644-6621", "http://www.mogef.go.kr/cc/opc/cc_opc_f002.do"),
            CounselingItem("여성가족부 청소년상담", "청소년기에 나타날 수 있는 다양한 고민들에 대해 심리상담을 진행합니다. \n 365일 24시간 서비스 운영","1388", "http://www.mogef.go.kr/cc/ccy/cc_ccy_f001.do"),
            CounselingItem("중독관리통합지원센터", "지역사회 내 알코올 및 기타 중독(마약, 인터넷 게임, 도박)에 대해 상담 및 치료를 진행하여 사회복귀를 지원합니다. \n 센터별 이용시간 상이","1577-0199", "http://www.mohw.go.kr/react/policy/index.jsp?PAR_MENU_ID=06&MENU_ID=06330404&PAGE=4&topTitle=")
        )



        listViewCounseling.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = counselingItems[position]
                val siteUrl = selectedItem.site
                openSiteInBrowser(siteUrl)
            }

        val btnClinicSiteConnect = view.findViewById<Button>(R.id.btn_clinicSiteConnect)
        listViewCounseling.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = counselingItems[position]
                val siteUrl = selectedItem.site
                openSiteInBrowser(siteUrl)
            }

        // Create and set the adapter for the ListView
        val adapter = CounselingListAdapter(requireContext(), counselingItems)
        listViewCounseling.adapter = adapter

        return view
    }

    private fun openSiteInBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            Log.d("CallFragment", "openSiteInBrowser: URL=$url")
        } catch (e: Exception) {
            e.printStackTrace()
            // 에러 메시지를 출력하여 원인 파악에 도움을 줄 수 있습니다.
        }
    }
}

