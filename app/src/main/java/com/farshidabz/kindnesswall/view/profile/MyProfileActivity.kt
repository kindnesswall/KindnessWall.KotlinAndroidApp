package com.farshidabz.kindnesswall.view.profile

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.databinding.ActivityMyProfileBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MyProfileActivity : BaseActivity() {

    lateinit var binding: ActivityMyProfileBinding

    private val viewModel: MyProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.backImageView.setOnClickListener { onBackPressed() }
        binding.editImageView.setOnClickListener {
            binding.editProfileContainer.visibility = View.VISIBLE
        }

        binding.cancelChangesTextView.setOnClickListener { revertAllChanges() }
        binding.saveChangesTextView.setOnClickListener { saveChanges() }
    }

    private fun saveChanges() {

    }

    private fun revertAllChanges() {

    }
}
