package com.example.ktorclientsample.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ktorclientsample.R
import com.example.ktorclientsample.data.ApiService
import com.example.ktorclientsample.data.RepositoryImpl
import com.example.ktorclientsample.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        val client = ApiService.build()
        val repo = RepositoryImpl(client)
        MainViewModelFactory(repo).create(MainViewModel::class.java)
    }

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private lateinit var mAdapter: UserAdapter
    private lateinit var itemDecoration: DividerItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = UserAdapter()

        binding.customToolbar.toolBar.title = getString(R.string.contact_header)

        itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        binding.contactList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(itemDecoration)
        }

        observer()
    }

    private fun observer() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.getUserState.collect { state ->
                when (state) {
                    is MainViewModel.UIState.Loading -> {
                        binding.progressBar.root.show(true)
                    }
                    is MainViewModel.UIState.Success -> {
                        binding.progressBar.root.show(false)
                        mAdapter.submitUsers(state.data)
                    }
                    is MainViewModel.UIState.Failure -> {
                        binding.progressBar.root.show(false)
                        snackbar(state.message)
                    }
                }
            }
        }
    }

    private fun View.show(show: Boolean) {
        visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun Activity.snackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun Activity.snackbar(message: String, action: () -> Unit) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                action()
            }
            .show()
    }
}