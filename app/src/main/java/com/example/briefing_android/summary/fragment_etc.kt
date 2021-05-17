package com.example.briefing_android.summary

import android.app.LauncherActivity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.briefing_android.R
import com.example.briefing_android.api.CommentURLRequest
import com.example.briefing_android.api.UserServiceImpl
import com.example.briefing_android.api.safeEnqueue
import com.example.briefing_android.summary.recyclerview_comment.CommentItem
import com.example.briefing_android.summary.recyclerview_comment.rv_Adapter

class fragment_etc(url:String) : Fragment(){
    private lateinit var  FErecyclerview : RecyclerView
    private var mpadapter3: rv_Adapter = rv_Adapter(R.layout.comment_item)

    var url = url

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var etc_listview = inflater.inflate(R.layout.etc_list, container, false)
        var thiscontext = container!!.getContext()
        FErecyclerview = etc_listview.findViewById(R.id.etc_recyclerview)

        var mp_datalist = ArrayList<ArrayList<CommentItem>>()
        Log.v("1111111","111111111")
        Log.v("url",url)
        //-------server--------------
        val callect_comment_List=UserServiceImpl.CommentService.requestURL(CommentURLRequest("V1WHgI2xM2k"))
        callect_comment_List.safeEnqueue {
            Log.v("22222222222","22222222222222")
            if(it.isSuccessful){
                Log.v("3333333333","33333333333")
                var ect_List = arrayListOf<CommentItem>()
                val ect_Comment = it.body()!!.etc_data
                for(i in 0 until ect_Comment.size){
                    if(ect_Comment[i].sort.equals("그외")){
                        Log.v("4444444444","4444444444444")
                        ect_List.add(
                                CommentItem(
                                        it_username=ect_Comment[i].nickname,
                                        it_date=ect_Comment[i].writetime.substring(0,10),
                                        it_comment=ect_Comment[i].comment,
                                        it_likecount=ect_Comment[i].likecount
                                )
                        )
                    }
                }
                Log.v("555555555","555555555555")
                //리사이클러뷰의 어댑터 세팅
                FErecyclerview.adapter=mpadapter3

                //리사이클러뷰 배치
                val lm = LinearLayoutManager(thiscontext)
                FErecyclerview.layoutManager=lm

                mpadapter3.data = ect_List
                mpadapter3.notifyDataSetChanged()
                mp_datalist.add(mpadapter3.data)
            }
        }
        mpadapter3.notifyDataSetChanged()

        return etc_listview
    }

}