package com.example.gittest.ui.repos.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gittest.databinding.ItemRepositoriesBinding
import com.example.gittest.domain.model.Repo

class RepositoriesAdapter(
    private var repos: List<Repo>,
    private val onItemClick: (String) -> Unit
): RecyclerView.Adapter<RepositoriesAdapter.RepositoriesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepositoriesViewHolder {
        val inflater = android.view.LayoutInflater.from(parent.context)
        val binding = ItemRepositoriesBinding.inflate(inflater, parent, false)
        return RepositoriesViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(
        holder: RepositoriesViewHolder,
        position: Int
    ) {
        holder.bind(repos[position])
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    fun updateRepositories(newRepos: List<Repo>) {
        repos = newRepos
        notifyDataSetChanged()
    }

    class RepositoriesViewHolder(
        private val binding: ItemRepositoriesBinding,
        private val onItemClick: (String) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            with(binding) {
                tvRepoName.text = repo.name
                tvRepoLanguage.text = repo.language
                tvDescription.text = repo.description
                root.setOnClickListener { onItemClick(repo.id) }
            }
        }
    }
}