package ir.kindnesswall.view.main.conversation.chat

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.model.ChatMessageModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.TextMessageBaseModel
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.databinding.ActivityChatBinding
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import org.koin.android.viewmodel.ext.android.viewModel


class ChatActivity : BaseActivity() {

    lateinit var binding: ActivityChatBinding
    val viewModel: ChatViewModel by viewModel()

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    private lateinit var adapter: ChatAdapter

    private lateinit var chatBroadcastReceiver: ChatBroadcastReceiver

    companion object {
        fun start(context: Context, chatId: Long) {
            context.startActivity(
                Intent(
                    context,
                    ChatActivity::class.java
                ).apply { putExtra("chatId", chatId) })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        configureViews(savedInstanceState)

        viewModel.chatId = intent.getLongExtra("chatId", 0)

        initBroadcastReceiver()

        getChats()
    }

    private fun initBroadcastReceiver() {
        chatBroadcastReceiver = ChatBroadcastReceiver {

        }

        val filter = IntentFilter("CHAT")
        registerReceiver(chatBroadcastReceiver, filter)
    }

    private fun getChats() {
        viewModel.getChats().observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }

                CustomResult.Status.SUCCESS -> {
                    endlessRecyclerViewScrollListener.isLoading = false
                    showList(it.data)
                }

                CustomResult.Status.ERROR -> {
                    endlessRecyclerViewScrollListener.isLoading = false
                }
            }
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.backImageView.setOnClickListener { onBackPressed() }
        binding.userImageView.setOnClickListener { }
        binding.userNameTextView.setOnClickListener { }

        binding.sendImageView.setOnClickListener {
            if (!viewModel.messageTextLiveData.value.isNullOrEmpty()) viewModel.sendMessage()
        }

        binding.giftImageView.setOnClickListener { showGiftOptionsMenu() }
        binding.blockUserImageView.setOnClickListener { blockUser() }
        binding.unblockButton.setOnClickListener { unblockUser() }

        initRecyclerView()
    }

    private fun unblockUser() {
        viewModel.unblockUser().observe(this) {
            binding.blockUserImageView.visibility = View.VISIBLE
            binding.unblockButton.visibility = View.GONE
        }
    }

    private fun blockUser() {
        viewModel.blockUser().observe(this) {
            binding.blockUserImageView.visibility = View.GONE
            binding.unblockButton.visibility = View.VISIBLE
        }
    }

    private fun initRecyclerView() {
        binding.itemsListRecyclerView.adapter = getAdapter()

        (binding.itemsListRecyclerView.layoutManager as? LinearLayoutManager)?.reverseLayout = true
        (binding.itemsListRecyclerView.layoutManager as? LinearLayoutManager)?.stackFromEnd = false

        setRecyclerViewPagination(binding.itemsListRecyclerView.layoutManager as LinearLayoutManager)
    }

    private fun setRecyclerViewPagination(layoutManager: LinearLayoutManager) {
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore() {
                    endlessRecyclerViewScrollListener.isLoading = true
                    gatherLastId()
                    getChats()
                }

                override fun onScrolled(position: Int) {
                }
            }

        endlessRecyclerViewScrollListener.PAGE_SIZE = 10
        endlessRecyclerViewScrollListener.VISIBLE_THRESHOLD = 5

        binding.itemsListRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun gatherLastId() {
        if (viewModel.chatList == null) {
            viewModel.lastId = 0
            return
        }

        val lastItem = viewModel.chatList!!.last()
        if (lastItem is TextMessageModel) {
            viewModel.lastId = lastItem.id
        } else {
            for (i in viewModel.chatList!!.lastIndex downTo 0) {
                val item = viewModel.chatList!![i]
                if (item is TextMessageModel) {
                    viewModel.lastId = item.id
                }
            }
        }
    }

    private fun getAdapter(): ChatAdapter {
        if (!::adapter.isInitialized) {
            adapter = ChatAdapter(viewModel)
        }

        return adapter
    }

    private fun showGiftOptionsMenu() {

    }

    private fun showList(chatMessageModel: ChatMessageModel?) {
        if (chatMessageModel == null) {
            return
        }

        if (chatMessageModel.textMessages.isNullOrEmpty()) {
            return
        }

        val items = arrayListOf<TextMessageBaseModel>()
        val textMessages: List<TextMessageModel>? = chatMessageModel.textMessages

        /** todo add header here
        //                for (textMessage in chatMessageModel.textMessages!!) {
        //                    textMessage.createdAt = textMessage.createdAt?.getJustDate()
        //                }
        //                try {
        //                    val groups = LinkedHashMap<Date, List<TextMessageModel>>()
        //                    groups.putAll(transactions.groupBy { item -> item.createdAt ?: Date() })
        //
        //                    for (transactionModel in groups) {
        //                        if (shouldAddHeader(transactionModel)) {
        //                            items.add(TransactionHeaderModel(transactionModel.key.toDayofMonth_MonthName_year()))
        //                        }
        //
        //                        for (transaction in transactionModel.value) {
        //                            items.add(TransactionItemModel(transaction))
        //                        }
        //                    }
        //                } catch (exception: Exception) {
        //                }
         */

        viewModel.chatList?.addAll(chatMessageModel.textMessages!!)
        viewModel.chatList?.let { adapter.setData(it) }

        if (viewModel.chatList.isNullOrEmpty()) {
            binding.noChatTextView.visibility = View.VISIBLE
        } else {
            binding.noChatTextView.visibility = View.GONE
        }
    }
//
//    private fun shouldAddHeader(transactionModel: MutableMap.MutableEntry<Date, List<TextMessageModel>>): Boolean {
//        if (adapter.itemCount == 0) {
//            return true
//        }
//
//        val currentDayCalendar = Calendar.getInstance().apply {
//            timeInMillis = transactionModel.key.time
//        }
//
//        val prvDayCalendar = Calendar.getInstance().apply {
//            timeInMillis =
//                viewModel.transactionList[viewModel.transactionList.lastIndex].createdAt?.time ?: 0
//        }
//
//        val day = currentDayCalendar[Calendar.DAY_OF_YEAR]
//        val targetDay = prvDayCalendar[Calendar.DAY_OF_YEAR]
//
//        if (day == targetDay) {
//            return false
//        }
//
//        return true
//    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(chatBroadcastReceiver)
    }
}
