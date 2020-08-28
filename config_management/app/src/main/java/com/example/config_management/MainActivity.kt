package com.example.config_management
import com.example.config_management.PagerAdapter
import com.example.config_management.R
import kotlinx.android.synthetic.main.activity_main.*



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val pagerAdapter by lazy {
        PagerAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerAdapter.title.add("Domains")
        pagerAdapter.title.add("Features")
        viewpager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewpager)

        val pos :Int = viewpager.currentItem
//        Search view
        
    }
}