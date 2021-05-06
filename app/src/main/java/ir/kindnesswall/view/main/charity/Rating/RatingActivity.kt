package ir.kindnesswall.view.main.charity.Rating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import ir.kindnesswall.R
import ir.kindnesswall.data.model.RateModel
import ir.kindnesswall.databinding.ActivityRatingBinding
import ir.kindnesswall.view.Test
import ir.kindnesswall.view.main.charity.charitydetail.CharityViewModel
import kotlinx.android.synthetic.main.activity_rating.*
import org.koin.android.viewmodel.ext.android.viewModel

class RatingActivity : AppCompatActivity() , RateViewListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       var binding : ActivityRatingBinding= DataBindingUtil.setContentView(this, R.layout.activity_rating)
        val a = Test()
       binding.viewModelRating = a
        val list :ArrayList<RateModel> =ArrayList()
        list.add(RateModel("A","","",1.5f))
        list.add(RateModel("B","","",1.5f))
        list.add(RateModel("C","","",1.5f))
        list.add(RateModel("D","","",1.5f))
        list.add(RateModel("A","","",1.5f))
        list.add(RateModel("B","","",1.5f))
        list.add(RateModel("C","","",1.5f))
        list.add(RateModel("D","","",1.5f))
        list.add(RateModel("A","","",1.5f))
        list.add(RateModel("B","","",1.5f))
        list.add(RateModel("C","","",1.5f))
        list.add(RateModel("D","","",1.5f))
        var ratAdapter = RateAdapter(list!!)
        recycler_rate.adapter = ratAdapter
        recycler_rate.setNestedScrollingEnabled(false)

    }

    override fun submitListener() {
        Log.i("5645646555555555","test")
    }
}