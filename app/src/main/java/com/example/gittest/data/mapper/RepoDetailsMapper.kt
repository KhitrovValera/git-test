package com.example.gittest.data.mapper

import com.example.gittest.data.remote.model.RepoDetailsDto
import com.example.gittest.domain.model.RepoDetails

fun RepoDetailsDto.toDomain(): RepoDetails {
    return RepoDetails(
        id = id.toString(),
        name = name,
        fullName = full_name,
        ownerLogin = owner.login,
        description = owner.id.toString(),
        htmlUrl = html_url,
        stargazersCount = stargazers_count,
        watchersCount = watchers_count,
        forksCount = forks_count,
        openIssuesCount = open_issues_count,
        language = language,
        private = private,
        defaultBranch = default_branch,
        licenseName = license?.name
    )
}