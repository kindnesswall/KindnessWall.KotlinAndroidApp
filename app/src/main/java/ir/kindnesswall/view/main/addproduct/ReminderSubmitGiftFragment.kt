package ir.kindnesswall.view.main.addproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.kindnesswall.BR
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.databinding.FragmentReminderSubmitGiftBinding
import ir.kindnesswall.utils.extentions.runOrStartAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReminderSubmitGiftFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentReminderSubmitGiftBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderSubmitGiftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.onClickPrimary, View.OnClickListener {
            requireContext().runOrStartAuth {
                SubmitGiftActivity.start(requireContext())
            }
            dismiss()
        })

        binding.setVariable(BR.onClickSecondary, View.OnClickListener {
            dismiss()
        })

    }

    companion object {
        fun start(fragmentManager: FragmentManager) {
            if (isThirdDay()) {
                val reminderSubmitGiftFragment = ReminderSubmitGiftFragment()
                reminderSubmitGiftFragment.show(fragmentManager, reminderSubmitGiftFragment.tag)
            }
        }

        private fun isThirdDay(): Boolean {
            val sdf = SimpleDateFormat("yyMMdd", Locale.ENGLISH)
            val currentDayInfo: Long = sdf.format(Date()).toLong()
            if (AppPref.latestDayVisitedApp < currentDayInfo) {
                AppPref.latestDayVisitedApp = currentDayInfo
                if (AppPref.countDaysVisitedApp >= 2) {
                    AppPref.countDaysVisitedApp = 0
                    return true
                } else {
                    AppPref.countDaysVisitedApp += 1
                }
            }
            return false
        }
    }

}