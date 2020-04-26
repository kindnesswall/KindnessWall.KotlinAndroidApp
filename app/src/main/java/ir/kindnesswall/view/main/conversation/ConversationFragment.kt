package ir.kindnesswall.view.main.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.model.ConversationModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.FragmentConversationBinding
import ir.kindnesswall.utils.OnItemClickListener
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * Created by farshid.abazari since 2019-11-07
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class ConversationFragment : BaseFragment(), OnItemClickListener {
    lateinit var binding: FragmentConversationBinding
    val viewModel: ConversationsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_conversation, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getConversations()
    }

    override fun configureViews() {
        binding.itemsListRecyclerView.apply {
            adapter = ConversationListAdapter(this@ConversationFragment)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun getConversations() {
        viewModel.conversationsList.observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.SUCCESS -> {
                    showList(it.data)
                }

                CustomResult.Status.ERROR -> {
                    showToastMessage(it.message.toString())
                }
            }
        }
    }

    private fun showList(data: List<ConversationModel>?) {
        if (!data.isNullOrEmpty()) {
            (binding.itemsListRecyclerView.adapter as ConversationListAdapter).submitList(data)
        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {

    }
}