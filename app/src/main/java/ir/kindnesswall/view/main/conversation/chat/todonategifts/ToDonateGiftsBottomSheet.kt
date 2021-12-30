package ir.kindnesswall.view.main.conversation.chat.todonategifts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.BottomSheetGiftsToDonateBinding
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel

class ToDonateGiftsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var listener: (Boolean, GiftModel) -> Unit

    private lateinit var binding: BottomSheetGiftsToDonateBinding

    val viewModel: DonateGiftViewModel by viewModel()

    companion object {
        fun newInstance(
            giftsList: ArrayList<GiftModel>,
            contactId: Long
        ): ToDonateGiftsBottomSheet {
            return ToDonateGiftsBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("gifts", giftsList)
                    putLong("contactId", contactId)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Boolean, GiftModel) -> Unit) {
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

        viewModel.giftsList = arguments?.getParcelableArrayList("gifts")
        viewModel.contactId = arguments?.getLong("contactId", 0) ?: 0

        if (viewModel.giftsList.isNullOrEmpty()) {
            dismiss()
            return
        }

        binding.closeImageView.setOnClickListener { dismiss() }

        val adapter = ToDonateListAdapter()
        adapter.setOnItemClickListener {
            donateGift(it)
        }

        binding.toDonatesList.adapter = adapter

        adapter.setItems(viewModel.giftsList!!)
    }

    private fun donateGift(it: GiftModel) {
        viewModel.donateGift(it).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                CustomResult.Status.LOADING -> {
                    // todo show loading
                }

                CustomResult.Status.ERROR -> {
                    if (result.errorMessage?.message!!.contains("Unable to resolve host")) {
                        NoInternetDialogFragment().display(childFragmentManager) {
                            donateGift(it)
                        }
                    } else {
                        showToastMessage(getString(R.string.error_in_donate))
                        listener.invoke(false, it)
                    }
                }

                CustomResult.Status.SUCCESS -> {
                    listener.invoke(true, it)
                    dismiss()
                }
            }
        }
    }

    fun showToastMessage(message: String) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}