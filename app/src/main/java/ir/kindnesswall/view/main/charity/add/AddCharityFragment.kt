package ir.kindnesswall.view.main.charity.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.kindnesswall.BR
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.databinding.FragmentAddCharityBinding
import ir.kindnesswall.utils.isAppAvailable
import ir.kindnesswall.utils.widgets.RoundBottomSheetDialogFragment

class AddCharityFragment : RoundBottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddCharityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCharityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.onClickPrimary, View.OnClickListener {
            val packageName = "org.telegram.messenger"
            if (isAppAvailable(requireContext(), packageName)) {
                val telegramIntent = Intent(Intent.ACTION_VIEW)
                telegramIntent.data = Uri.parse("http://telegram.me/Kindness_Wall_Admin")
                startActivity(telegramIntent)
            } else {
                (activity as BaseActivity).showToastMessage(getString(R.string.install_telegram))
            }
            dismiss()
        })

    }
}