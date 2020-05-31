package ir.kindnesswall.view.profile.blocklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.ActivityBlockListBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import org.koin.android.viewmodel.ext.android.viewModel

class BlockListActivity : BaseActivity(), OnItemClickListener {

    private val viewModel: BlockListViewModel by viewModel()

    lateinit var binding: ActivityBlockListBinding

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, BlockListActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_block_list)

        configureViews(savedInstanceState)
        getBlockedUsers()
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.backImageView.setOnClickListener { onBackPressed() }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val animator = binding.blockedUsersRecyclerView.itemAnimator

        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

        val adapter = BlockListAdapter(this)
        adapter.setHasStableIds(true)
        binding.blockedUsersRecyclerView.adapter = adapter
        binding.blockedUsersRecyclerView.setHasFixedSize(true)
        binding.blockedUsersRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getBlockedUsers() {
        viewModel.getBlockList().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                showList(it.data)
                checkEmptyPage()
            }
        }
    }

    private fun checkEmptyPage() {
        if (viewModel.blockedUsers.isNullOrEmpty()) {
            binding.blockUsersCount.visibility = View.GONE
            binding.blockedUsersRecyclerView.visibility = View.GONE
            binding.emptyPagePlaceHolder.visibility = View.VISIBLE
        } else {
            binding.blockUsersCount.visibility = View.VISIBLE
            binding.blockedUsersRecyclerView.visibility = View.VISIBLE
            binding.emptyPagePlaceHolder.visibility = View.GONE
        }
    }

    private fun showList(data: List<ChatContactModel>?) {
        if (data.isNullOrEmpty()) {
            viewModel.blockedUsers.clear()
            return
        }

        viewModel.blockedUsers.addAll(data)

        binding.blockedUsersRecyclerView.adapter = BlockListAdapter(object : OnItemClickListener {
            override fun onItemClicked(position: Int, obj: Any?) {
                showPromptDialog(
                    getString(R.string.unblock),
                    getString(
                        R.string.unblock_user_question,
                        (obj as ChatContactModel).contactProfile?.name
                    ), onPositiveClickCallback = {
                        viewModel.unblockUser(obj.chat?.chatId ?: 0)
                            .observe(this@BlockListActivity) {
                                if (it.status == CustomResult.Status.SUCCESS) {
                                    KindnessApplication.instance.getContact(obj.chat?.chatId!!)?.blockStatus?.let { it ->
                                        it.contactIsBlocked = false
                                        it.userIsBlocked = false
                                    }

                                    viewModel.blockedUsers.removeAt(position)
                                    (binding.blockedUsersRecyclerView.adapter as BlockListAdapter)
                                        .submitList(viewModel.blockedUsers)

                                    setBlockedUsersCount()
                                    checkEmptyPage()
                                }
                            }
                    }
                )
            }
        })

        (binding.blockedUsersRecyclerView.adapter as BlockListAdapter).submitList(data)
        setBlockedUsersCount()
    }

    fun setBlockedUsersCount() {
        if (viewModel.blockedUsers.isNullOrEmpty()) {
            checkEmptyPage()
            return
        }

        if (viewModel.blockedUsers.size == 1) {
            binding.blockUsersCount.text =
                getString(R.string.one_user_blocked_count, viewModel.blockedUsers.size.toString())
        } else {
            binding.blockUsersCount.text =
                getString(R.string.blocked_count, viewModel.blockedUsers.size.toString())
        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        GiftDetailActivity.start(this, obj as GiftModel)
    }
}
