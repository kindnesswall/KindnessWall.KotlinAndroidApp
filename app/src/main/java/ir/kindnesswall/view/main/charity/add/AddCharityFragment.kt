package ir.kindnesswall.view.main.charity.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.kindnesswall.BR
import ir.kindnesswall.databinding.FragmentAddCharityBinding
import ir.kindnesswall.utils.openSupportForm

class AddCharityFragment : BottomSheetDialogFragment() {

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
            openSupportForm(requireContext())
            dismiss()
        })
    }
}