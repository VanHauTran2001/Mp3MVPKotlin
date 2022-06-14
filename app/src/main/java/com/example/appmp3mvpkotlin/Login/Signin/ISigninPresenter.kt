package com.example.appmp3mvpkotlin.Login.Signin

interface ISigninPresenter {
        fun onUnit()
        fun onLogin(email:String,password:String)
}