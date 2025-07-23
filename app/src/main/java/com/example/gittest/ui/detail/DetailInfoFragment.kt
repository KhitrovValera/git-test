package com.example.gittest.ui.detail

import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.gittest.R
import com.example.gittest.databinding.FragmentDetailInfoBinding
import com.example.gittest.domain.model.RepoDetails
import com.example.gittest.ui.common.model.UiInfoState
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.ImagesPlugin
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {

    private lateinit var binding: FragmentDetailInfoBinding

    private val viewModel: DetailInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailInfoBinding.inflate(inflater, container, false)
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

    private fun observeFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observeState() }
            }
        }
    }

    private suspend fun observeState() {
        viewModel.state.collect { state ->
            when (state) {
                is DetailInfoViewModel.State.Error -> showError(state.error)
                is DetailInfoViewModel.State.Loaded -> {
                    showDetails(state.githubRepo)
                    val readmeState = state.readmeState

                    when (readmeState)  {
                        DetailInfoViewModel.ReadmeState.Empty -> {
                            showEmptyReadme()
                        }
                        is DetailInfoViewModel.ReadmeState.Error -> showReadMeError(readmeState.error)
                        is DetailInfoViewModel.ReadmeState.Loaded -> {
                            showReadme(readmeState.markdown)
                        }
                        DetailInfoViewModel.ReadmeState.Loading -> {
                            showLoadingReadMe()
                        }
                    }
                }
                DetailInfoViewModel.State.Loading -> showDetailsLoading()
            }
        }
    }

    private fun setupListeners() {
        setupBtnUpdate()
        setupToolBar()
    }

    private fun setupBtnUpdate() {
        binding.inDetail.btnUpdate.setOnClickListener {
            viewModel.retryLoadData()
        }
    }

    private fun setupToolBar() {
        binding.materialToolbar.setOnMenuItemClickListener {
            navBack()
            true
        }
    }

    private fun navBack() {
        binding.root.findNavController()
            .navigate(R.id.action_detailInfoFragment_to_repositoriesListFragment)
    }

    private fun showLoadingReadMe() {
        binding.pbReadMe.isVisible = true
    }

    private fun showEmptyReadme() {
        binding.pbReadMe.isVisible = false
        binding.tvReadmeContent.isVisible = true
        binding.tvReadmeContent.text = R.string.empty_readme.toString()
    }

    private fun showReadme(content: String) {
        binding.pbReadMe.isVisible = false
        binding.tvReadmeContent.isVisible = true

        val markwon = Markwon.builder(requireContext())
            .usePlugin(ImagesPlugin.create())
            .usePlugin(TablePlugin.create(requireContext()))
            .build()

        markwon.setMarkdown(binding.tvReadmeContent, content)
    }

    private fun showReadMeError(uiInfoState: UiInfoState) {
        showError(uiInfoState)
        binding.scrollViewContent.isVisible = true
    }

    private fun showError(uiInfoState: UiInfoState) {
        with(binding) {
            scrollViewContent.isVisible = false
            inDetail.groupInfoState.isVisible = true
            pbRepo.isVisible = false

            with(inDetail) {
                ivState.setImageResource(uiInfoState.icon)
                tvReason.text = uiInfoState.title.toString()
                tvDescriptionReason.text = uiInfoState.subtitle.toString()
                btnUpdate.text = uiInfoState.buttonText.toString()
            }
        }
    }

    private fun setHtmlText(textView: TextView, htmlText: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            textView.text = Html.fromHtml(htmlText)
        }
    }

    private fun showDetails(repoDetails: RepoDetails) {
        val cleanUrl = repoDetails.htmlUrl
            .removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("www.")

        with(binding) {
            scrollViewContent.isVisible = true
            inDetail.groupInfoState.isVisible = false
            pbRepo.isVisible = false
            flReadMe.isVisible = true

            materialToolbar.title = repoDetails.name
            tvLicenseValue.text = repoDetails.licenseName ?: getString(R.string.no_license)

            tvRepoLink.text = cleanUrl
            tvRepoLink.movementMethod = LinkMovementMethod.getInstance()

            val starsFormattedString = getString(R.string.stats_stars, repoDetails.stargazersCount)
            setHtmlText(tvStars, starsFormattedString)

            val forksFormattedString = getString(R.string.stats_forks, repoDetails.forksCount)
            setHtmlText(tvForks, forksFormattedString)

            val watchersFormattedString = getString(R.string.stats_watchers, repoDetails.watchersCount)
            setHtmlText(tvWatchers, watchersFormattedString)
        }
    }

    private fun showDetailsLoading() {
        with(binding) {
            scrollViewContent.isVisible = false
            inDetail.groupInfoState.isVisible = false
            pbRepo.isVisible = true
        }
    }
}