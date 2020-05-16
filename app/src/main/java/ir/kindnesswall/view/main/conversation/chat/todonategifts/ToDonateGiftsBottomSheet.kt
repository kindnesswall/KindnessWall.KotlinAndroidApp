package ir.kindnesswall.view.main.conversation.chat.todonategifts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.databinding.BottomSheetGiftsToDonateBinding

class ToDonateGiftsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var listener: (GiftModel) -> Unit

    private lateinit var binding: BottomSheetGiftsToDonateBinding

    private var giftsList: ArrayList<GiftModel>? = ArrayList()

    companion object {
        fun newInstance(giftsList: ArrayList<GiftModel>): ToDonateGiftsBottomSheet {
            return ToDonateGiftsBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("gifts", giftsList)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (GiftModel) -> Unit) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_gifts_to_donate,
            container,
            false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        giftsList = arguments?.getParcelableArrayList("gifts")

        if (giftsList.isNullOrEmpty()) {
            dismiss()
            return
        }

        val adapter = ToDonateListAdapter()
        adapter.setOnItemClickListener {
            listener.invoke(it)
            dismiss()
        }

        binding.toDonatesList.adapter = ToDonateListAdapter()

        adapter.setItems(giftsList!!)
    }
}