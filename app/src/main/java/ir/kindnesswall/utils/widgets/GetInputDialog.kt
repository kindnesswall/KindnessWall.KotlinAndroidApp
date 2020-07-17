package ir.kindnesswall.utils.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import ir.kindnesswall.R
import ir.kindnesswall.databinding.DialogGetInputBinding

class GetInputDialog : DialogFragment() {
    var onApproveListener: ((String) -> Unit)? = null
    var onRefuseListener: (() -> Unit)? = null

    private lateinit var binding: DialogGetInputBinding
    private var minCharacter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_get_input, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        if (arguments != null) {
            val title = arguments?.getString("title", "")
            val hint = arguments?.getString("hint", "")
            val message = arguments?.getString("message", "")
            val acceptBtn = arguments?.getString("accept_btn", "")

            minCharacter = arguments?.getInt("min_char", 0) ?: 0

            binding.titleTextView.text = title
            binding.inputEditText.hint = hint
            binding.acceptButton.text = acceptBtn

            if (!message.isNullOrEmpty()) {
                binding.inputEditText.setText(message)
            }
        }

        binding.inputEditText.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.acceptButton.isEnabled = false
            } else {
                if (text.length >= minCharacter) {
                    binding.acceptButton.isEnabled = true
                    context?.let {
                        binding.acceptButton.setTextColor(
                            ContextCompat.getColor(
                                it,
                                R.color.white
                            )
                        )
                    }
                } else {
                    binding.acceptButton.isEnabled = false
                    context?.let {
                        binding.acceptButton.setTextColor(
                            ContextCompat.getColor(
                                it,
                                R.color.disableButtonColor
                            )
                        )
                    }
                }
            }
        }

        binding.acceptButton.setOnClickListener {
            dismiss()
            onApproveListener?.invoke(binding.inputEditText.text.toString())
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
            onRefuseListener?.invoke()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}