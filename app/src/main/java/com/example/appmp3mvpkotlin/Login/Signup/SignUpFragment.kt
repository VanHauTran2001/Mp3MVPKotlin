package com.example.appmp3mvpkotlin.Login.Signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.appmp3mvpkotlin.R
import com.example.appmp3mvpkotlin.databinding.SignupFragmentBinding

class SignUpFragment : Fragment(),ISignupView {
    private var biding : SignupFragmentBinding? = null
    private var signupPresenter : ISignupPresenter?= null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        biding = DataBindingUtil.inflate(inflater,R.layout.signup_fragment,container,false)
        signupPresenter = SignupPresenter(this)
        signupPresenter?.onUnit()
        return biding?.root
    }

    override fun onClickListenner() {
        biding?.btnSignup?.setOnClickListener {
            val email: String = biding?.edtEmail?.text.toString().trim()
            val user: String = biding?.edtUser?.text.toString().trim()
            val password: String = biding?.edtPassword?.text.toString().trim()
            val confirmPassword: String = biding?.edtConfirmPassword?.text.toString().trim()
            val phone: String = biding?.edtPhone?.text.toString().trim()
            signupPresenter?.onSignup(email, user, password, confirmPassword, phone)
        }
    }

    override fun onSuccessfully(mess: String) {
        Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show()
    }

    override fun onError(mess: String) {
        Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show()
    }

    override fun onContext(): Context? {
        return activity
    }
}