package com.example.appmp3mvpkotlin.Fragment

import Model.BaiHat
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmp3mvpkotlin.Activity.BackgroupService
import com.example.appmp3mvpkotlin.Activity.MainActivity
import com.example.appmp3mvpkotlin.Adapter.AdapterBaiHat
import com.example.appmp3mvpkotlin.R
import com.example.appmp3mvpkotlin.databinding.FragmentListNhacBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class FragmentListNhac : Fragment() ,AdapterBaiHat.IListen,TextWatcher{
    private var binding : FragmentListNhacBinding?=null
    private var mService : BackgroupService?=null
    private var executorService : ExecutorService?=null
    private var connection :ServiceConnection?= null
    private var adapterBaiHat : AdapterBaiHat?= null
    private var isBound : Boolean = false
    private var baiHatArrayList : ArrayList<BaiHat> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_nhac,container,false)
        viewLoading()
        binding!!.imgSearch.setOnClickListener(View.OnClickListener {
            val txtSearch = binding!!.edtSearch.text.toString().trim()
            searchSong(txtSearch.trim().replace(" ","+"))
        })
        adapterBaiHat = AdapterBaiHat(this)
        mService = BackgroupService()
        configRecylerView()
        connectService()
        binding!!.imgBackHome.setOnClickListener {
            activity?.startActivity(Intent(activity,MainActivity::class.java))
        }
        return binding!!.root
    }
    private fun searchSong(keySearch: String) {
        if (executorService != null && executorService!!.isShutdown) {
            executorService!!.shutdown()
        }
        executorService = Executors.newFixedThreadPool(1)
        val urlSearch = "https://chiasenhac.vn/tim-kiem?q=$keySearch"
        configRecylerView()
//        DowloadTask().execute(urlSearch)

    }
//    class DowloadTask : AsyncTask<String?, Void?,ArrayList<BaiHat>?>() {
//        override fun doInBackground(vararg p0: String?):ArrayList<BaiHat>? {
//            val document: Document
//            try {
//                document = Jsoup.connect(strings[0]).get()
//                val elements = Objects.requireNonNull(document.select("div.tab-content").first())!!
//                    .select("ul.list_music")
//                for (e in elements) {
//                    val elements1 = e.select("li.media")
//                    for (e1 in elements1) {
//                        val name = Objects.requireNonNull(e1.select("a.search_title").first())!!
//                            .attr("title")
//                        val singer = e1.select("div.media-body").select("div.author").text()
//                        val link = Objects.requireNonNull(e1.select("a.search_title").first())!!
//                            .attr("href")
//                        val document1 = Jsoup.connect(link).get()
//                        val elementLink = document1.select("div.tab-content").first()
//                        val linkMP3 = elementLink!!.select("a.download_item").attr("href")
//                        baiHat.add(BaiHat(name, singer, linkMP3))
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return baiHatArrayList
//        }
//
//        @SuppressLint("NotifyDataSetChanged")
//        override fun onPostExecute(baiHats:ArrayList<BaiHat>?) {
//            super.onPostExecute(baiHats)
//            executorService = null
//            if (baiHats != null) {
//                mService.setBaiHatList(baiHats)
//            }
//            binding.recylerBaiHat.getAdapter().notifyDataSetChanged()
//            hideLoading()
//        }
//    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun connectService() {
        connection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                val binder: BackgroupService.MyLocalBinder = iBinder as BackgroupService.MyLocalBinder
                mService = binder.getService()
                isBound = true
                if (mService!!.checkEmpty()) {
                    searchSong("ai+chung+tinh+duoc+mai")
                } else {
                    binding!!.recylerBaiHat.adapter!!.notifyDataSetChanged()
                    if (mService!!.getBaiHatList() != null) {
                        hideLoading()
                    }
                }
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                isBound = false
            }
        }
        val intent = Intent()
        intent.setClassName(activity!!, BackgroupService::class.java.getName())
        activity?.bindService(intent, connection as ServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun configRecylerView() {
        adapterBaiHat = AdapterBaiHat(this)
        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding!!.recylerBaiHat.layoutManager = linearLayoutManager
        binding!!.recylerBaiHat.adapter = adapterBaiHat
    }

    private fun viewLoading() {
        binding!!.recylerBaiHat.visibility = View.GONE
        binding!!.swiperefresh.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding!!.recylerBaiHat.visibility = View.VISIBLE
        binding!!.swiperefresh.visibility = View.GONE
    }
    override fun getCount(): Int {
        return if (mService == null) {
            return 0
        } else
            return mService!!.sizeItemMusicOnline()
    }

    override fun getData(position: Int): BaiHat? {
        return mService!!.getData(position)
    }

    override fun onClickBaiHat(position: Int) {
        mService!!.onClickItem(position)
        replaceFragment(FragmentPlayNhac())
    }
    fun replaceFragment(fragment: Fragment?) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction!!.replace(R.id.frameLayout, fragment!!)
//        transaction!!.addToBackStack(FragmentPlayNhac.TAG)
        transaction!!.commit()
    }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        searchSong(p0.toString().replace(" "," ").replace(" ","+"))
    }


    override fun onDestroy() {
        mService!!.unbindService(connection!!)
        super.onDestroy()
    }
}