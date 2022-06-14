package com.example.appmp3mvpkotlin.Login.Signup

interface ISignupPresenter {
    fun onUnit()
    fun onSignup(email : String,user : String ,password : String,confirmPassword : String,phone : String)
}