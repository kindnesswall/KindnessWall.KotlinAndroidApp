package ir.kindnesswall.view.main.charity.Rating

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ir.kindnesswall.R
import ir.kindnesswall.data.model.RateModel
import ir.kindnesswall.databinding.ActivityRatingBinding

class RatingActivity : AppCompatActivity() , RateViewListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       var binding : ActivityRatingBinding= DataBindingUtil.setContentView(this, R.layout.activity_rating)

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
        binding.recyclerRate.adapter = ratAdapter
        binding.recyclerRate.setNestedScrollingEnabled(false)

    }

    override fun submitListener() {
        Log.i("5645646555555555","get List Data")
    }
}