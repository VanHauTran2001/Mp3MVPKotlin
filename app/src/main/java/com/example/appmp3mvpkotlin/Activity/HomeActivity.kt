package com.example.appmp3mvpkotlin.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.appmp3mvpkotlin.Fragment.FragmentListNhac
import com.example.appmp3mvpkotlin.R
import com.example.appmp3mvpkotlin.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private var binding : ActivityHomeBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        addFragment()
    }
    private fun addFragment() {
        supportFragmentManager.beginTransaction().add(R.id.frameLayout,FragmentListNhac(),FragmentListNhac::class.java.name)
            .addToBackStack(null).commit()
    }
}