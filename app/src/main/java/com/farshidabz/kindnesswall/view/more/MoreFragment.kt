package com.farshidabz.kindnesswall.view.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.farshidabz.kindnesswall.BaseFragment
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.UserInfoPref
import com.farshidabz.kindnesswall.databinding.FragmentMoreBinding
import com.farshidabz.kindnesswall.view.profile.MyProfileActivity


/**
 * Created by farshid.abazari since 2019-11-07
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class MoreFragment : BaseFragment() {
    lateinit var binding: FragmentMoreBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
        return binding.root
    }

    override fun configureViews() {
        binding.profileTextView.setOnClickListener { context?.let { MyProfileActivity.start(it) } }
    }

    override fun onResume() {
        super.onResume()
        binding.userInfo = UserInfoPref
    }
}