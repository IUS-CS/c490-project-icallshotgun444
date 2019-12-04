package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity(), PlatformListFragment.Callbacks {


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         MobileAds.initialize(this)
         adView.loadAd(AdRequest.Builder().build())
         adView.adListener = object : AdListener(){
             override fun onAdFailedToLoad(p0: Int) {
                 Toast.makeText(applicationContext,"Failed to load",Toast.LENGTH_LONG).show()
             }

             override fun onAdLoaded() {
                 Toast.makeText(applicationContext,"Loaded ad",Toast.LENGTH_LONG).show()
             }
         }

         val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = PlatformListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }//on create


}
