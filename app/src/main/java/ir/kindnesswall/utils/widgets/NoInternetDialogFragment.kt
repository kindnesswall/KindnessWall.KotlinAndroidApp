package ir.kindnesswall.utils.widgets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ir.kindnesswall.R
import ir.kindnesswall.databinding.DialogNoInternetBinding


class NoInternetDialogFragment : DialogFragment() {
    lateinit var binding: DialogNoInternetBinding
    private lateinit var tryAgainClickListener: () -> Unit

    fun display(
        fragmentManager: FragmentManager,
        tryAgainClickListener: () -> Unit
    ): NoInternetDialogFragment? {
        val dialog = NoInternetDialogFragment()
        dialog.tryAgainClickListener = tryAgainClickListener
        dialog.show(fragmentManager, NoInternetDialogFragment::class.java.simpleName)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_no_internet, container, false)

        binding.tryAgain.setOnClickListener {
            tryAgainClickListener.invoke()
            dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)

            dialog.setCancelable(true)
        }
    }
}