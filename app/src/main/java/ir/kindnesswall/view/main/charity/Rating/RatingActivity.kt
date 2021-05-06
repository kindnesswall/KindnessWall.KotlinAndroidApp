package ir.kindnesswall.view.main.charity.Rating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import ir.kindnesswall.R
import ir.kindnesswall.databinding.ActivityRatingBinding
import ir.kindnesswall.view.Test
import ir.kindnesswall.view.main.charity.charitydetail.CharityViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RatingActivity : AppCompatActivity() , RateViewListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       var binding : ActivityRatingBinding= DataBindingUtil.setContentView(this, R.layout.activity_rating)
        val a = Test(this)
       binding.viewModelRating = a
    }

    override fun submitListener() {
        Log.i("5645646555555555","test")
    }
}