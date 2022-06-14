package com.example.appmp3mvpkotlin.Login.Signin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import com.example.appmp3mvpkotlin.Activity.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class SigninPresenter : ISigninPresenter {
    private var view: ISigninView ? = null

    constructor(view: ISigninView){
        this.view = view
    }


    override fun onUnit() {
        view?.onClickListener()
    }

    override fun onLogin(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            view!!.onError("Email can not be empty !")
        } else if (TextUtils.isEmpty(password)) {
            view!!.onError("Password can not be empty !")
        } else {
            val progressDialog = ProgressDialog(view!!.onContext())
            progressDialog.setTitle("Login")
            progressDialog.setMessage("Please wait.......")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful){
                        view!!.onSuccessfull("Login successfull")

                    }else{
                        view!!.onError("Login failed")
                    }

                }
        }
    }






}