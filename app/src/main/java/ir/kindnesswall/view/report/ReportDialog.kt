package ir.kindnesswall.view.report

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.composethemeadapter.MdcTheme
import ir.kindnesswall.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Farhad Beigirad on 3/20/22.
 */
class ReportDialog : BottomSheetDialogFragment() {
    private val viewModel: ReportViewModel by viewModel()

    private val argType by lazy {
        requireNotNull(requireArguments().getParcelable<ReportType>(ARG_TYPE)) { "arg must not be null" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ComposeView(inflater.context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view as ComposeView
        view.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        view.setContent {
            MdcTheme {
                Column {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.report_dialog_title)) },
                        backgroundColor = Color.Transparent,
                        elevation = 0.dp
                    )

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {

                        val reportTextState = remember { mutableStateOf("") }
                        TextField(
                            value = reportTextState.value,
                            onValueChange = { reportTextState.value = it },
                            placeholder = { Text(text = stringResource(R.string.report_dialog_hint)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(200.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { attemptSubmit(reportTextState.value) }
                            ) {
                                Text(text = stringResource(R.string.submit_report))
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = { dismiss() }) {
                                Text(text = stringResource(R.string.cancel_report))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
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

    private fun attemptSubmit(reportText: String) {
        if (reportText.isNotBlank()) {
            when (val reportType = argType) {
                is ReportType.Charity ->
                    viewModel.sendCharityReport(reportType, reportText)
                is ReportType.Gift ->
                    viewModel.sendGiftReport(reportType, reportText)
            }
        }
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