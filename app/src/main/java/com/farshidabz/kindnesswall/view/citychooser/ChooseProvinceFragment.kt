package com.farshidabz.kindnesswall.view.citychooser


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.farshidabz.kindnesswall.R

/**
 * A simple [Fragment] subclass.
 */
class ChooseProvinceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_province, container, false)
    }


}
