package com.example.gittest.ui.auth

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.gittest.R
import com.example.gittest.databinding.FragmentAuthBinding
import com.example.gittest.ui.auth.AuthViewModel.Action
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {

    companion object {
        fun newInstance() = AuthFragment()
    }

    private lateinit var binding: FragmentAuthBinding

    private val viewModel: AuthViewModel by viewModels()

    private val idleColor by lazy { ContextCompat.getColor(requireContext(), R.color.grey) }
    private val errorColor by lazy { ContextCompat.getColor(requireContext(), R.color.error) }
    private val activeColor by lazy { ContextCompat.getColor(requireContext(), R.color.Secondary) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindToViewModel()
    }

    private fun bindToViewModel() {
        setupListeners()
        observeFlows()
    }

    private fun setupListeners() {
        setupBtnSignIn()
        setupTokenEditText()
        setupTokenFocusChanges()
    }

    private fun observeFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observeTokenState() }

                launch { observeAction() }

                launch { observeState() }
            }
        }
    }

    private suspend fun observeTokenState() {
        viewModel.token.collect { value ->
            if (binding.tokenEditText.text.toString() != value) {
                binding.tokenEditText.setText(value ?: "")
            }
        }
    }

    private fun setupTokenEditText() {
        binding.tokenEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateToken(s?.toString() ?: "")
                if (!s.isNullOrBlank()) viewModel.setStateActive()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupTokenFocusChanges() {
        binding.tokenEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) viewModel.setStateActive()
        }
    }

    private suspend fun observeState() {
        viewModel.state.collect { state ->
            when (state) {
                AuthViewModel.State.Idle -> changeEditLayout(idleColor)
                AuthViewModel.State.Active -> changeEditLayout(activeColor)
                AuthViewModel.State.Loading -> changeEditLayout(idleColor, isLoading = true)
                is AuthViewModel.State.InvalidInput -> changeEditLayout(
                    errorColor,
                    getString(R.string.invalid_token)
                )
            }
        }
    }

    private suspend fun observeAction() {
        viewModel.actions.collect { action ->
            when (action) {
                is Action.ShowError -> {
                    showErrorDialog(action.message)
                }

                Action.RouteToMain -> {
                    binding.root.findNavController()
                        .navigate(R.id.action_authFragment_to_repositoriesListFragment)
                }
            }
        }
    }

    private fun setupBtnSignIn() {
        binding.btnSignIn.setOnClickListener {
            viewModel.onSignButtonPressed()
        }
    }

    private fun changeEditLayout(color: Int, helperText: String = "", isLoading: Boolean = false) {
        with(binding) {
            tokenInputLayout.helperText = helperText
            tokenInputLayout.boxStrokeColor = color
            tokenInputLayout.defaultHintTextColor = ColorStateList.valueOf(color)
            btnSignIn.showLoading(isLoading)
        }
    }


    private fun showErrorDialog(message: Pair<Int, Int>) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(message.first))
            .setMessage(getString(message.second))
            .setPositiveButton("OK", null)
            .setCancelable(false)
            .show()
    }
}