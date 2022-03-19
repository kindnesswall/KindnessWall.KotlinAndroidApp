package ir.kindnesswall.view.main.charity.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.kindnesswall.BR
import ir.kindnesswall.R
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.ReportCharityMessageModel
import ir.kindnesswall.databinding.FragmentAddCharityBinding
import ir.kindnesswall.databinding.ReportBottomSheetBinding
import ir.kindnesswall.utils.openSupportForm
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.utils.widgets.RoundBottomSheetDialogFragment
import ir.kindnesswall.view.main.charity.charitydetail.CharityViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ReportMessageCharityFragment(private val charityId: Long) : RoundBottomSheetDialogFragment() {
    private lateinit var binding: ReportBottomSheetBinding
    private val viewModel: CharityViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReportBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            setVariable(BR.onClickSecondary, View.OnClickListener {
                dismiss()
            })

            setVariable(BR.onClickPrimary, View.OnClickListener {
                if (editDescribtion.text.toString().trim().isNullOrEmpty()) {
                    editDescribtion.hint = getString(R.string.please_enter_repost)
                } else {
                    sendReport(editDescribtion.toString())
                }
            })
        }
    }


    private fun sendReport(message: String) {
        viewModel.sendReport(
            ReportCharityMessageModel(
                charityId = charityId,
                message = message
            )
        ).observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                Toast.makeText(requireContext(), "${it.data}", Toast.LENGTH_SHORT).show()
            } else if (it.status == CustomResult.Status.ERROR) {
                if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                    NoInternetDialogFragment().display(childFragmentManager) {
                        sendReport(binding.editDescribtion.toString())
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_try_again),
                        Toast.LENGTH_LONG
                    ).show()
                    dismiss()
                }
            }
        }
    }


}