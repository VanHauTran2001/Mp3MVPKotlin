package com.example.appmp3mvpkotlin.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.appmp3mvpkotlin.Fragment.FragmentListNhac
import com.example.appmp3mvpkotlin.R
import com.example.appmp3mvpkotlin.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private var binding : ActivityHomeBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        replaceFragment(FragmentListNhac())
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
}