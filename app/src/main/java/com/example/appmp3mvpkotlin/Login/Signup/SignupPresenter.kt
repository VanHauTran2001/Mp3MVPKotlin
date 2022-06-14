package com.example.appmp3mvpkotlin.Login.Signup

import android.app.Activity
import android.app.ProgressDialog
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth

class SignupPresenter : ISignupPresenter {
    private var view : ISignupView?=null

    constructor(view : ISignupView){
        this.view = view
    }
    override fun onUnit() {
        view!!.onClickListenner()
    }

    override fun onSignup(
        email: String,
        user: String,
        password: String,
        confirmPassword: String,
        phone: String
    ) {
        if (TextUtils.isEmpty(email)) {
            view!!.onError("Email can not be empty !")
        } else if (TextUtils.isEmpty(user)) {
            view!!.onError("User can not be empty !")
        } else if (TextUtils.isEmpty(password)) {
            view!!.onError("Password can not be empty !")
        } else if (TextUtils.isEmpty(confirmPassword)) {
            view!!.onError("Confirm Password can not be empty !")
        } else if (TextUtils.isEmpty(phone)) {
            view!!.onError("Phone number can not be empty !")
        } else {
            val firebaseAuth = FirebaseAuth.getInstance()
            val progressDialog = ProgressDialog(view!!.onContext())
            progressDialog.setTitle("Login")
            progressDialog.setMessage("Please wait.......")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    (view!!.onContext() as Activity?)!!
                ) { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        view!!.onSuccessfully("Register user successfully !")
                    } else {
                        view!!.onError("Register user failed !")
                    }
                }
        }
    }
}