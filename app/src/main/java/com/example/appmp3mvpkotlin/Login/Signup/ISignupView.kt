package com.example.appmp3mvpkotlin.Login.Signup

import android.content.Context

interface ISignupView {
    fun onClickListenner()
    fun onSuccessfully(mess:String)
    fun onError(mess : String)
    fun onContext(): Context?
}