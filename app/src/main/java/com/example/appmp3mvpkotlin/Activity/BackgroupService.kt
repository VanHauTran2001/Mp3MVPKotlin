package com.example.appmp3mvpkotlin.Activity

import Model.BaiHat
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.net.Uri
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.util.*

class BackgroupService : Service(),OnBufferingUpdateListener,
    OnCompletionListener, OnPreparedListener {
    private val myLocalBinder: MyLocalBinder = MyLocalBinder()
    private var baiHatList: List<BaiHat>? = ArrayList()
    var currentPosition = -1
    var isPrepared :Boolean = false
    var mediaPlayer: MediaPlayer? = MediaPlayer()
    var isPlaying : Boolean = false

    fun getBaiHatList(): List<BaiHat>? {
        return baiHatList
    }

    fun setBaiHatList(baiHatList: List<BaiHat>?) {
        this.baiHatList = baiHatList
    }
    @JvmName("isPrepared1")
    fun isPrepared() : Boolean{
        return isPrepared
    }
    @JvmName("getMediaPlayer1")
    fun getMediaPlayer() : MediaPlayer{
        return mediaPlayer!!
    }
    @JvmName("isPlaying1")
    fun isPlaying() : Boolean{
        return isPlaying
    }
    @JvmName("setPlaying1")
    fun setPlaying(playing : Boolean){
        this.isPlaying = playing
    }
    override fun onBind(intent: Intent): IBinder? {
        return myLocalBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("dl", "On create")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    fun onClickItem(position: Int) {
        isPrepared = false
        if (!baiHatList!![position].getLinkSong().equals("")) {
            currentPosition = position
            play(position)
            return
        }
        getLinkMp3(baiHatList!![position].getLinkSong()!!, position)
        currentPosition = position
    }

    override fun onBufferingUpdate(mediaPlayer: MediaPlayer, i: Int) {}
    override fun onCompletion(mediaPlayer: MediaPlayer) {
        next()
    }

    fun sizeItemMusicOnline(): Int {
        return if (baiHatList == null) {
            return 0
        } else
            return baiHatList!!.size
    }

    fun getData(position: Int): BaiHat {
        return baiHatList!![position]
    }

    override fun onPrepared(mp: MediaPlayer) {
        mediaPlayer!!.start()
        isPrepared = true
    }

    fun play(position: Int) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer!!.release()
            }
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.setDataSource(baiHatList?.get(position)?.getLinkSong())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mediaPlayer!!.setOnBufferingUpdateListener(this)
        mediaPlayer!!.setOnCompletionListener(this)
        mediaPlayer!!.setOnPreparedListener(this)
        mediaPlayer!!.prepareAsync()
        isPlaying = true
    }

    fun previous() {
        isPrepared = false
        if (currentPosition <= 0) {
            return
        }
        currentPosition -= 1
        play(currentPosition)
    }

    fun pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                isPlaying = false
                mediaPlayer!!.pause()
            } else {
                isPlaying = true
                mediaPlayer!!.start()
            }
        }
    }

    fun repeat() {
        isPrepared = false
        if (baiHatList!![currentPosition].getLinkSong() == null || baiHatList!![currentPosition].getLinkSong()
                .equals("")
        ) {
            getLinkMp3(baiHatList!![currentPosition].getLinkSong()!!, currentPosition)
        } else {
            play(currentPosition)
        }
    }

    fun randum() {
        isPrepared = false
        val random = Random()
        val index = random.nextInt(baiHatList!!.size)
        if (index == currentPosition) {
            currentPosition = index - 1
        }
        currentPosition = index
        if (baiHatList!![currentPosition].getLinkSong() == null || baiHatList!![currentPosition]
                .getLinkSong().equals("")
        ) {
            getLinkMp3(baiHatList!![currentPosition].getLinkSong()!!, currentPosition)
        } else {
            play(currentPosition)
        }
    }

    operator fun next() {
        isPrepared = false
        if (currentPosition == baiHatList!!.size - 1) {
            return
        }
        currentPosition = currentPosition + 1
        if (baiHatList!![currentPosition].getLinkSong() == null || baiHatList!![currentPosition]
                .getLinkSong().equals("")
        ) {
            getLinkMp3(baiHatList!![currentPosition].getLinkSong()!!, currentPosition)
        } else {
            play(currentPosition)
        }
    }

    fun currentItem(): BaiHat{
        return baiHatList!![currentPosition]
    }


    fun getLinkMp3(linkSong: String, position: Int) {
        val asyncTask: AsyncTask<String?, Void?, String?> =
            object : AsyncTask<String?, Void?, String?>() {
                override fun doInBackground(vararg p0: String?): String? {
                    var linkMusic = ""
                    try {
                        val c: Document = Jsoup.connect(strings[0]).get()
                        val els: Elements =
                            Objects.requireNonNull(c.select("div.tab-content").first())!!
                                .select("a.download_item")
                        linkMusic = if (els.size >= 2) {
                            els.get(1).attr("href")
                        } else {
                            els.get(0).attr("href")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                    return linkMusic
                }

                override fun onPostExecute(result: String?) {
                    baiHatList!![position].setLinkSong(result.toString())
                    if (result != null) {
                        play(position)
                    }
                    super.onPostExecute(result)
                }

            }
        asyncTask.execute(linkSong)
    }

    fun checkEmpty(): Boolean {
        return baiHatList == null || baiHatList!!.isEmpty()
    }
    inner class MyLocalBinder : Binder() {
        fun getService():BackgroupService{
            return this@BackgroupService
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}