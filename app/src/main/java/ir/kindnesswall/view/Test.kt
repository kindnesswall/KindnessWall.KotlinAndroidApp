package ir.kindnesswall.view

import ir.kindnesswall.view.main.charity.Rating.RateViewListener

class Test(val rateViewListener: RateViewListener) {

    fun submit(){
        rateViewListener.submitListener()
    }

}