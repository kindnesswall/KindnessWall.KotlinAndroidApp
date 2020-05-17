package ir.kindnesswall.view.main.conversation

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.KindnessApplication
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

    private lateinit var updateContactListBroadcastReceiver: ConversationBroadcastReceiver
    private lateinit var newContactListBroadcastReceiver: ConversationBroadcastReceiver

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

        initBroadcastReceiver()
    }

    private fun initBroadcastReceiver() {
        newContactListBroadcastReceiver = ConversationBroadcastReceiver {
            getConversations()
        }

        updateContactListBroadcastReceiver = ConversationBroadcastReceiver {
            refreshList()
        }

        val newListFilter = IntentFilter("NEW_CONTACT_LIST")
        val updateListFilter = IntentFilter("UPDATE_CONTACT_LIST")

        context?.let {
            it.registerReceiver(newContactListBroadcastReceiver, newListFilter)
            it.registerReceiver(updateContactListBroadcastReceiver, updateListFilter)
        }
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

    fun loadData() {
        val staticContacts = KindnessApplication.instance.getContactList()

        if (viewModel.conversationsList.isNullOrEmpty() && staticContacts.isNullOrEmpty()) {
            getConversations()
        } else {
            refreshList()
        }
    }

    private fun refreshList() {
        showList(KindnessApplication.instance.getContactList())
    }

    private fun getConversations() {
        viewModel.getConversationsList().observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.SUCCESS -> {
                    it.data?.let { contactList ->
                        viewModel.conversationsList.clear()
                        viewModel.conversationsList.addAll(contactList)
                    }

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
            KindnessApplication.instance.setContactList(data)
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