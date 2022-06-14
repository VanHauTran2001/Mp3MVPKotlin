package com.example.appmp3mvpkotlin.Login.Signin

import android.content.Context

interface ISigninView {
    fun onClickListener()
    fun onSuccessfull(mess : String)
    fun onError(mess : String)
    fun onContext(): Context?
}