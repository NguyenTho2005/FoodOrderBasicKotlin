package com.example.foodorder.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.content.Intent
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.example.foodorder.R
import com.example.foodorder.adapter.MainViewPagerAdapter
import com.example.foodorder.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private var mActivityMainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding?.root)

        mActivityMainBinding?.toolbar?.layoutToolbar?.findViewById<ImageButton>(R.id.btn_user_profile)
            ?.setOnClickListener {
                startActivity(Intent(this, UserProfileActivity::class.java))
            }
        mActivityMainBinding?.viewpager2?.isUserInputEnabled = false
        val mainViewPagerAdapter = MainViewPagerAdapter(this)
        mActivityMainBinding?.viewpager2?.adapter = mainViewPagerAdapter
        mActivityMainBinding?.viewpager2?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> mActivityMainBinding?.bottomNavigation?.menu?.findItem(R.id.nav_home)?.isChecked = true
                    1 -> mActivityMainBinding?.bottomNavigation?.menu?.findItem(R.id.nav_cart)?.isChecked = true
                    2 -> mActivityMainBinding?.bottomNavigation?.menu?.findItem(R.id.nav_feedback)?.isChecked = true
                    3 -> mActivityMainBinding?.bottomNavigation?.menu?.findItem(R.id.nav_contact)?.isChecked = true
                    4 -> mActivityMainBinding?.bottomNavigation?.menu?.findItem(R.id.nav_order)?.isChecked = true
                }
            }
        })
        mActivityMainBinding?.bottomNavigation?.setOnNavigationItemSelectedListener { item: MenuItem? ->
            val id = item?.itemId
            if (id == R.id.nav_home) {
                mActivityMainBinding?.viewpager2?.currentItem = 0
            } else if (id == R.id.nav_cart) {
                mActivityMainBinding?.viewpager2?.currentItem = 1
            } else if (id == R.id.nav_feedback) {
                mActivityMainBinding?.viewpager2?.currentItem = 2
            } else if (id == R.id.nav_contact) {
                mActivityMainBinding?.viewpager2?.currentItem = 3
            } else if (id == R.id.nav_order) {
                mActivityMainBinding?.viewpager2?.currentItem = 4
            }
            true
        }
    }

    override fun onBackPressed() {
        showConfirmExitApp()
    }

    private fun showConfirmExitApp() {
        MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_exit_app))
                .positiveText(getString(R.string.action_ok))
                .onPositive { _: MaterialDialog?, _: DialogAction? -> finish() }
                .negativeText(getString(R.string.action_cancel))
                .cancelable(false)
                .show()
    }

    fun setToolBar(isHome: Boolean, title: String?) {
        if (isHome) {
            mActivityMainBinding?.toolbar?.layoutToolbar?.visibility = View.GONE
            return
        }
        mActivityMainBinding?.toolbar?.layoutToolbar?.visibility = View.VISIBLE
        mActivityMainBinding?.toolbar?.tvTitle?.text = title
    }
}