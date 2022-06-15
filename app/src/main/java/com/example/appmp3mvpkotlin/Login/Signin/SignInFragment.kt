package com.example.appmp3mvpkotlin.Login.Signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.appmp3mvpkotlin.Activity.HomeActivity
import com.example.appmp3mvpkotlin.R
import com.example.appmp3mvpkotlin.databinding.SigninFragmentBinding
import kotlinx.android.synthetic.main.signin_fragment.*

class SignInFragment : Fragment(),ISigninView{
    private var biding : SigninFragmentBinding? = null
    private var signinPresenter : ISigninPresenter? = null

    private var passWordnotVisible: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        biding = DataBindingUtil.inflate(inflater,R.layout.signin_fragment,container,false)
        signinPresenter = SigninPresenter(this)
        signinPresenter?.onUnit()
        return biding?.root
    }

    fun customLogin() {
        var email:String = biding?.edtEmail?.text.toString().trim()
        var password : String = biding?.edtPassword?.text.toString().trim()
        signinPresenter?.onLogin(email,password)
    }

    fun onChecked() {
        if(passWordnotVisible==1){
            biding?.imgCheckPass?.setImageResource(R.drawable.ic_baseline_visibility_off_24)
            biding?.edtPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passWordnotVisible = 0
        }else{
            biding?.imgCheckPass?.setImageResource(R.drawable.ic_check_on )
            biding?.edtPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
            passWordnotVisible = 1
        }
        biding?.edtPassword?.setSelection(biding!!.edtPassword.length())
    }

    override fun onClickListener() {
        biding?.btnLogin?.setOnClickListener {
            customLogin()
        }
        biding?.imgCheckPass?.setOnClickListener {
            onChecked()
        }
    }

    override fun onSuccessfull(mess: String) {
        Toast.makeText(activity,mess,Toast.LENGTH_LONG).show()
        val intent = Intent(activity, HomeActivity::class.java)
        activity?.startActivity(intent)
        activity?.finishAffinity()
    }

    override fun onError(mess: String) {
        Toast.makeText(activity,mess,Toast.LENGTH_LONG).show()
    }

    override fun onContext(): Context? {
        return activity
    }


}
