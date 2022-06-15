package com.example.appmp3mvpkotlin.Fragment

import Model.BaiHat
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.appmp3mvpkotlin.Activity.BackgroupService
import com.example.appmp3mvpkotlin.R
import com.example.appmp3mvpkotlin.databinding.FragmentPlayNhacBinding
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

class FragmentPlayNhac : Fragment() {
    private var binding : FragmentPlayNhacBinding?= null
    private var isRunning: Boolean = false
    //Service
    private var myService: BackgroupService? = null
    private var serviceConnection: ServiceConnection? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_nhac,container,false)
        isRunning = true
        connectService()
        startAsyn()
        customToolbar()
        evenClick()
        return binding!!.root
    }
    companion object{
        val TAG = FragmentPlayNhac::class.java.name
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun connectService() {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                val binder: BackgroupService.MyLocalBinder = iBinder as BackgroupService.MyLocalBinder
                myService = binder.getService()
                myService!!.setPlaying(true)
                updateInfo()
            }

            override fun onServiceDisconnected(componentName: ComponentName) {}
        }
        val intent = Intent()
        intent.setClassName(activity!!, BackgroupService::class.java.name)
        activity?.bindService(intent, serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
    }
    private fun updateInfo() {
        if (myService == null) {
            return
        }
        val currentSong: BaiHat = myService!!.currentItem() ?: return
        binding!!.txtTenBaiHatPlay.text = currentSong.getNameSong()
        binding!!.txtCaSiPlay.text = currentSong.getNameSinger()
        if (myService!!.isPlaying()) {
            Glide.with(binding!!.imgPlay)
                .load(R.drawable.ic_pause)
                .into(binding!!.imgPlay)
        } else {
            Glide.with(binding!!.imgPlay)
                .load(R.drawable.iconplay)
                .into(binding!!.imgPlay)
        }
    }
    fun startAsyn() {
        val asyncTask: AsyncTask<Void?, Int?, Void?> = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Void?, Int?, Void?>() {
            override fun doInBackground(vararg voids: Void?): Void? {
                while (isRunning) {
                    SystemClock.sleep(300)
                    if (myService == null || !myService!!.isPrepared()) {
                        continue
                    }
                    val mp = myService!!.getMediaPlayer()
                    publishProgress(mp.duration, mp.currentPosition)
                }
                return null
            }
            @SuppressLint("SimpleDateFormat")
            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
                binding!!.seekBarPlaynhac.progress = values[1]!! * 100 / values[0]!!
                binding!!.txtTimeEnd.text = SimpleDateFormat("mm:ss").format(values[0])
                binding!!.txtTimeStart.text = SimpleDateFormat("mm:ss").format(values[1])
            }
        }
        asyncTask.executeOnExecutor(Executors.newFixedThreadPool(1))
    }
    fun evenClick(){
        binding!!.imgRepeat.setOnClickListener(View.OnClickListener {
            myService!!.repeat()
            updateInfo()
        })
        binding!!.imgSuffe.setOnClickListener(View.OnClickListener {
            myService!!.randum()
            updateInfo()
        })
        binding!!.seekBarPlaynhac.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    myService!!.getMediaPlayer()
                        .seekTo(myService!!.getMediaPlayer().duration / 100 * progress)
                    binding!!.seekBarPlaynhac.progress = progress
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding!!.imgPlay.setOnClickListener {
            myService!!.pause()
            updateInfo()
        }
        binding!!.imgPreview.setOnClickListener {
            Glide.with(binding!!.imgPlay).load(R.drawable.iconplay)
                .into(
                    binding!!.imgPlay
                )
            myService!!.previous()
            updateInfo()
        }
        binding!!.imgNext.setOnClickListener { v ->
            Glide.with(binding!!.imgPlay).load(R.drawable.iconplay)
                .into(
                    binding!!.imgPlay
                )
            myService!!.next()
            updateInfo()
        }
    }
    @SuppressLint("UseRequireInsteadOfGet")
    fun customToolbar(){
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding!!.toolbarPlayNhac)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding!!.toolbarPlayNhac.title = ""
        binding!!.toolbarPlayNhac.setNavigationOnClickListener { view ->
            myService!!.getMediaPlayer().stop()
            fragmentManager!!.popBackStack()
        }
    }

}