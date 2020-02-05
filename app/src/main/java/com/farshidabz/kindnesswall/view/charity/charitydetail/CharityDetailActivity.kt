package com.farshidabz.kindnesswall.view.charity.charitydetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.databinding.ActivityCharityDetailBinding

class CharityDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityCharityDetailBinding

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, CharityDetailActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_charity_detail)
    }
}
