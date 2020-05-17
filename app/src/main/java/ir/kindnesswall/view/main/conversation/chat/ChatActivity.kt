package ir.kindnesswall.view.main.conversation.chat

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.model.*
import ir.kindnesswall.databinding.ActivityChatBinding
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import ir.kindnesswall.utils.imageloader.circleCropTransform
import ir.kindnesswall.utils.imageloader.loadImage
import ir.kindnesswall.view.main.conversation.chat.todonategifts.ToDonateGiftsBottomSheet
import org.koin.android.viewmodel.ext.android.viewModel


class ChatActivity : BaseActivity() {

    lateinit var binding: ActivityChatBinding
    val viewModel: ChatViewModel by viewModel()

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    private lateinit var adapter: ChatAdapter

    private lateinit var chatBroadcastReceiver: ChatBroadcastReceiver

    companion object {
        fun start(
            context: Context,
            requestChatModel: RequestChatModel,
            isCharity: Boolean = false
        ) {
            context.startActivity(
                Intent(context, ChatActivity::class.java)
                    .putExtra("requestGiftModel", requestChatModel)
                    .putExtra("isCharity", isCharity)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        configureViews(savedInstanceState)

        viewModel.requestChatModel =
            intent.getSerializableExtra("requestGiftModel") as? RequestChatModel

        viewModel.isCharity = intent.getBooleanExtra("isCharity", false)

        if (viewModel.requestChatModel == null) {
            finish()
            return
        }

        AppPref.currentChatSessionId = viewModel.requestChatModel?.chatId ?: -1

        initBroadcastReceiver()

        getUserProfile()
        getChats()
        getToDonateGifts()
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.backImageView.setOnClickListener { onBackPressed() }
        binding.userImageView.setOnClickListener { }
        binding.userNameTextView.setOnClickListener { }

        binding.sendImageView.setOnClickListener {
            if (!viewModel.messageTextLiveData.value.isNullOrEmpty()) {
                viewModel.sendMessage().observe(this) { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            result.data?.let { data ->
                                binding.messageEditText.setText("")
                                viewModel.chatList?.add(0, data)
                                viewModel.chatList?.let { adapter.setData(it) }
                                checkEmptyPage()
                            }
                        }

                        CustomResult.Status.LOADING -> {

                        }

                        CustomResult.Status.ERROR -> {
                            showToastMessage(getString(R.string.error_sending_message))
                        }
                    }
                }
            }
        }

        binding.giftImageView.setOnClickListener { showGiftOptionsMenu() }
        binding.blockUserImageView.setOnClickListener { blockUser() }
        binding.unblockButton.setOnClickListener { unblockUser() }

        initRecyclerView()
    }

    private fun getToDonateGifts() {
        viewModel.getToDonateGifts().observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.ERROR -> {
                }
                CustomResult.Status.SUCCESS -> {
                    if (it.data.isNullOrEmpty()) {
                        return@observe
                    }

                    viewModel.toDonateList.addAll(it.data)
                }
            }
        }
    }

    private fun getUserProfile() {
        if (viewModel.isCharity) {
            viewModel.getCharityProfile().observe(this) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        it.data?.let { user ->
                            loadImage(
                                user.imageUrl,
                                binding.userImageView,
                                placeHolderId = R.drawable.ic_profile_place_holder_white,
                                options = circleCropTransform()
                            )

                            binding.userNameTextView.text = user.name
                            viewModel.receiverUserId = user.userId
                        }
                    }
                    CustomResult.Status.ERROR -> {
                        finish()
                    }
                }
            }
        } else {
            viewModel.getUserProfile().observe(this) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        it.data?.let { user ->
                            loadImage(
                                user.image,
                                binding.userImageView,
                                placeHolderId = R.drawable.ic_profile_place_holder_white,
                                options = circleCropTransform()
                            )

                            binding.userNameTextView.text = user.name
                            viewModel.receiverUserId = user.id
                        }
                    }
                    CustomResult.Status.ERROR -> {
                        finish()
                    }
                }
            }
        }
    }

    private fun initBroadcastReceiver() {
        chatBroadcastReceiver = ChatBroadcastReceiver { message ->
            if (message.chatId.toLong() != viewModel.requestChatModel?.chatId) {
                return@ChatBroadcastReceiver
            }

            viewModel.chatList?.add(0, message)
            viewModel.chatList?.let { adapter.setData(it) }
            viewModel.sendAckMessage(message.id, message.chatId.toLong())

            checkEmptyPage()
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

    private fun unblockUser() {
        viewModel.unblockUser().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                binding.blockUserImageView.visibility = View.VISIBLE
                binding.unblockButton.visibility = View.GONE
            }
        }
    }

    private fun blockUser() {
        viewModel.blockUser().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                binding.blockUserImageView.visibility = View.GONE
                binding.unblockButton.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecyclerView() {
        binding.itemsListRecyclerView.adapter = getAdapter()

        val animator = binding.itemsListRecyclerView.itemAnimator

        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

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

        adapter.setHasStableIds(true)

        return adapter
    }

    private fun showGiftOptionsMenu() {
        if (viewModel.toDonateList.isNullOrEmpty()) {
            showToastMessage(getString(R.string.no_gift_to_donate))
            return
        }

        ToDonateGiftsBottomSheet.newInstance(
            viewModel.toDonateList,
            viewModel.requestChatModel?.contactId ?: 0
        ).apply {
            setOnItemClickListener {

            }
        }.show(supportFragmentManager, "donate")
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

        viewModel.chatList?.addAll(chatMessageModel.textMessages!!)
        viewModel.chatList?.let { adapter.setData(it) }

        checkEmptyPage()
    }

    private fun checkEmptyPage() {
        if (viewModel.chatList.isNullOrEmpty()) {
            binding.noChatTextView.visibility = View.VISIBLE
        } else {
            binding.noChatTextView.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        AppPref.isInChatPage = true

        AppPref.currentChatSessionId = viewModel.requestChatModel?.chatId ?: -1
    }

    override fun onStop() {
        super.onStop()
        AppPref.currentChatSessionId = -1
        AppPref.isInChatPage = false
    }

    override fun onDestroy() {
        super.onDestroy()
        AppPref.isInChatPage = false
        AppPref.currentChatSessionId = -1
        unregisterReceiver(chatBroadcastReceiver)
    }
}
