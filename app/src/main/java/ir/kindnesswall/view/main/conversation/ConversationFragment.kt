package ir.kindnesswall.view.main.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.RequestChatModel
import ir.kindnesswall.databinding.FragmentConversationBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
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

    override fun configureViews() {
        binding.itemsListRecyclerView.apply {
            adapter = ConversationListAdapter(this@ConversationFragment)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            val animator = itemAnimator

            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
    }

    fun getConversations() {
        viewModel.getConversationsList().observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.SUCCESS -> {
                    showList(it.data)
                }

                CustomResult.Status.ERROR -> {
                    if (UserInfoPref.bearerToken.isEmpty()) {
                        binding.conversationEmptyPage.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun showList(data: List<ChatContactModel>?) {
        if (!data.isNullOrEmpty()) {
            (binding.itemsListRecyclerView.adapter as ConversationListAdapter).submitList(data)
        }

        if ((binding.itemsListRecyclerView.adapter as ConversationListAdapter).itemCount == 0) {
            binding.conversationEmptyPage.visibility = View.VISIBLE
        } else {
            binding.conversationEmptyPage.visibility = View.GONE
        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        context?.let {
            ChatActivity.start(it, RequestChatModel().apply {
                with(obj as ChatContactModel) {
                    chatId = this.chat?.chatId?.toLong() ?: 0
                    contactId = this.chat?.contactId?.toLong() ?: 0
                    userId = this.chat?.userId?.toLong() ?: 0
                }
            })
        }
    }
}