package com.example.gittest.ui.repos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gittest.R
import com.example.gittest.databinding.FragmentRepositoriesListBinding
import com.example.gittest.domain.model.Repo
import com.example.gittest.ui.common.model.UiInfoState
import com.example.gittest.ui.repos.adapter.RepositoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    companion object {
        fun newInstance() = RepositoriesListFragment()
    }

    private lateinit var binding: FragmentRepositoriesListBinding

    private lateinit var repositoriesAdapter: RepositoriesAdapter

    private val viewModel: RepositoriesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)
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
        setupBtnUpdate()
        initRecyclerView()
        setupToolBar()
    }

    private fun observeFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observeState() }

                launch { observeAction() }
            }
        }
    }

    private suspend fun observeAction() {
        viewModel.actions.collect { action ->
            when (action) {
                RepositoriesListViewModel.Action.ExitAccount -> {
                    navigateToAuthFragment()
                }

                is RepositoriesListViewModel.Action.RouteToRepo -> {
                    navigateToDetailFragment(action.repoId)
                }
            }
        }
    }

    private fun navigateToAuthFragment() {
        binding.root.findNavController()
            .navigate(R.id.action_repositoriesListFragment_to_authFragment)
    }

    private fun navigateToDetailFragment(repoId: String) {
        val args = Bundle().apply {
            putString("repoId", repoId)
        }

        binding.root.findNavController()
            .navigate(R.id.action_repositoriesListFragment_to_detailInfoFragment, args)
    }

    private suspend fun observeState() {
        viewModel.state.collect { state ->
            when (state) {
                is RepositoriesListViewModel.State.Content -> {
                    getRepos(state.repos)
                    showRepos()
                }

                RepositoriesListViewModel.State.Empty -> showEmpty()
                is RepositoriesListViewModel.State.Error -> showError(state.message)
                RepositoriesListViewModel.State.Loading -> showLoading()
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            pbRepos.isVisible = true
            rsRepos.isVisible = false
            inRepos.groupInfoState.isVisible = false
        }
    }

    private fun showRepos() {
        with(binding) {
            pbRepos.isVisible = false
            rsRepos.isVisible = true
            inRepos.groupInfoState.isVisible = false
        }
    }

    private fun showError(uiInfoState: UiInfoState) {
        with(binding) {
            pbRepos.isVisible = false
            rsRepos.isVisible = false

            with(inRepos) {
                groupInfoState.isVisible = true
                ivState.setImageResource(uiInfoState.icon)
                tvReason.text = getString(uiInfoState.title)
                tvDescriptionReason.text = getString(uiInfoState.subtitle)
                btnUpdate.text = getString(uiInfoState.buttonText)
            }
        }
    }

    private fun showEmpty() {
        with(binding) {
            pbRepos.isVisible = false
            rsRepos.isVisible = false

            with(inRepos) {
                groupInfoState.isVisible = true
                ivState.setImageResource(R.drawable.ic_empty)
                tvReason.text = getString(R.string.empty)
                tvDescriptionReason.text = getString(R.string.empty_description)
                btnUpdate.text = getString(R.string.refresh)
            }
        }
    }

    private fun setupBtnUpdate() {
        binding.inRepos.btnUpdate.setOnClickListener {
            viewModel.loadRepo()
        }
    }

    private fun setupToolBar() {
        binding.materialToolbar.setOnMenuItemClickListener {
            viewModel.onExitButtonPressed()
            true
        }
    }

    private fun initRecyclerView() {

        repositoriesAdapter = RepositoriesAdapter(emptyList()) { repoId ->
            viewModel.onRepoPressed(repoId)
        }

        binding.rsRepos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = repositoriesAdapter
        }
    }

    private fun getRepos(repos: List<Repo>) {
        repositoriesAdapter.updateRepositories(repos)
    }
}