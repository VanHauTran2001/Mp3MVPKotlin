package com.example.appmp3mvpkotlin.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appmp3mvpkotlin.Login.Signin.SignInFragment
import com.example.appmp3mvpkotlin.Login.Signup.SignUpFragment
import com.example.appmp3mvpkotlin.R
import com.example.appmp3mvpkotlin.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var biding : ActivityMainBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        biding?.tabLayout?.setupWithViewPager(biding?.viewPager)
        viewPagerAdapter = ViewPagerAdapter(getSupportFragmentManager())
        viewPagerAdapter!!.addFragment(SignInFragment(), "Login")
        viewPagerAdapter!!.addFragment(SignUpFragment(), "Signup")
        biding?.viewPager?.setAdapter(viewPagerAdapter)
    }
    class ViewPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragments: ArrayList<Fragment>
        private val titles: ArrayList<String>

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getCount(): Int {
            return fragments.size;
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

        init {
            fragments = ArrayList()
            titles = ArrayList()
        }
    }
}