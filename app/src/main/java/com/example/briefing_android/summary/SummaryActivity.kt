package com.example.briefing_android.summary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.briefing_android.R
import com.example.briefing_android.api.URLRequest
import com.example.briefing_android.api.UserServiceImpl
import com.example.briefing_android.api.safeEnqueue
import com.example.briefing_android.main.MainActivity
import com.example.briefing_android.summary.recyclerview_comment.Graph_Viewpager_adapter
import com.google.android.material.tabs.TabLayout
import java.security.AccessController.getContext


class SummaryActivity : AppCompatActivity() {
    private lateinit var btn_back: ImageButton
    lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        var user_idx: Int
        user_idx = 1
        url = intent.getStringExtra("url")
        Log.d("url value= ",url)

        var indicator1 : ImageView = findViewById(R.id.indicator1)
        var indicator2 : ImageView = findViewById(R.id.indicator2)

        var comment_viewPager = findViewById<ViewPager>(R.id.comment_viewPager)
        var comment_tablayout = findViewById<TabLayout>(R.id.comment_tablayout)

        var channelname :TextView = findViewById(R.id.channelname)
        var thumbnail :ImageButton = findViewById(R.id.btn_thumbnail)
        var video_time :TextView = findViewById(R.id.time)
        var topic :TextView = findViewById(R.id.summary)
        var title :TextView = findViewById(R.id.title)



        //-------------server-----------------
        var anal_str = "https://www.youtube.com/watch?v="+url
        Log.v("str url값",anal_str)
        val analysispost = UserServiceImpl.AnalysisService.requestURL(urlRequest = URLRequest(anal_str,user_idx))
        analysispost.safeEnqueue {
            if(it.isSuccessful){
                val longtilte : String
                val VideoInfo = it.body()!!.data
                channelname.setText(VideoInfo.channel_name)
                if(VideoInfo.title.length>45){
                    longtilte = VideoInfo.title.substring(0,45) + "..."
                    title.setText(longtilte)
                }else{
                    title.setText(VideoInfo.title)
                }
                Glide.with(this)
                        .load(VideoInfo.thumnail)
                        .override(1500,600)
                        .into(thumbnail)
                video_time.setText(VideoInfo.video_time)
                topic.setText(VideoInfo.topic)
            }
        }
        //---------------------------------------

        //그래프 뷰페이저
//        var viewpager = findViewById<ViewPager>(R.id.viewpager)
//        var viewpagerAdapter = ViewPagerAdapter(this)
//        viewpager.adapter=viewpagerAdapter

        val graph_viewPager = findViewById<ViewPager>(R.id.viewpager)
        val graph_fragmentAdapter = Graph_Viewpager_adapter(supportFragmentManager)
        graph_viewPager.adapter = graph_fragmentAdapter

        graph_viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                indicator1.setImageDrawable(getDrawable(R.drawable.shape_circle1))
                indicator2.setImageDrawable(getDrawable(R.drawable.shape_circle1))

                when(position){
                    0-> indicator1.setImageDrawable(getDrawable(R.drawable.shape_circle2))
                    1-> indicator2.setImageDrawable(getDrawable(R.drawable.shape_circle2))
                }
            }
        })



        //댓글창 프래그먼트 뷰페이저
        val commnt_fragmentAdapter = Comment_Viewpager_adapter(supportFragmentManager,url)
        comment_viewPager.adapter = commnt_fragmentAdapter
        comment_tablayout.setupWithViewPager(comment_viewPager)


        btn_back = findViewById(R.id.btn_back)
        // 1. 뒤로가기 버튼 이벤트
        btn_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        // 2. 유투브 영상으로 넘어가기
        thumbnail.setOnClickListener(View.OnClickListener {
            var Videourl = "https://www.youtube.com/watch?v=" + url
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(Videourl))
            startActivity(intent)
        })
    }


}