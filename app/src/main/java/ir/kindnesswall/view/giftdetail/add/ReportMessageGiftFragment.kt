package ir.kindnesswall.view.giftdetail.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.core.os.bundleOf
import ir.kindnesswall.BR
import ir.kindnesswall.R
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.ReportGiftMessageModel
import ir.kindnesswall.databinding.ReportBottomSheetBinding
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.view.giftdetail.GiftDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ReportMessageGiftFragment : BottomSheetDialogFragment() {
    private var _binding: ReportBottomSheetBinding? = null
    val binding get() = _binding!!

    private val giftId: Long by lazy { requireArguments().getLong(ARG) }

    private val viewModel: GiftDetailViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReportBottomSheetBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            setVariable(BR.onClickSecondary, View.OnClickListener {
                dismiss()
            })

            setVariable(BR.onClickPrimary, View.OnClickListener {
                if (editDescribtion.text.toString().trim().isNotEmpty()) {
                    sendReport(editDescribtion.toString())
                }
            })
        }
    }

    private fun sendReport(message: String) {
        viewModel.sendReport(
            ReportGiftMessageModel(
                giftId = giftId,
                message = message
            )
        ).observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.report_success),
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            } else if (it.status == CustomResult.Status.ERROR) {
                if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                    NoInternetDialogFragment().display(childFragmentManager) {
                        sendReport(binding!!.editDescribtion.toString())
                        dismiss()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG = "ARG"
        fun newInstance(giftId: Long) = ReportMessageGiftFragment().apply {
            arguments = bundleOf(ARG to giftId)
        }
    }
}