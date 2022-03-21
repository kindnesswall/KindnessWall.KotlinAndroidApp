package ir.kindnesswall.view.report

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.kindnesswall.R
import ir.kindnesswall.databinding.ReportBottomSheetBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Farhad Beigirad on 3/20/22.
 */
class ReportDialog : BottomSheetDialogFragment() {
    private var _binding: ReportBottomSheetBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: ReportViewModel by viewModel()

    private val argType by lazy {
        requireNotNull(requireArguments().getParcelable<ReportType>(ARG_TYPE)) { "arg must not be null" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ReportBottomSheetBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCancelRepost.setOnClickListener { dismiss() }
        binding.btnSendRepost.setOnClickListener {
            val reportText = binding.editDescribtion.text?.toString().orEmpty()
            if (reportText.isNotBlank()) {
                when (val reportType = argType) {
                    is ReportType.Charity -> viewModel.sendCharityReport(reportType, reportText)
                    is ReportType.Gift -> viewModel.sendGiftReport(reportType, reportText)
                }
            }
        }

        viewModel.errorFlow.onEach { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isSuccessfullyFlow.onEach {
            Toast.makeText(context, R.string.report_success, Toast.LENGTH_SHORT).show()
            dismiss()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TYPE = "ARG_TYPE"
        fun newInstance(model: ReportType) = ReportDialog().apply {
            arguments = bundleOf(ARG_TYPE to model)
        }
    }

    sealed interface ReportType : Parcelable {
        @Parcelize
        data class Gift(val id: Long) : ReportType

        @Parcelize
        data class Charity(val id: Long) : ReportType
    }
}