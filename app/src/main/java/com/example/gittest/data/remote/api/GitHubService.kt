package com.example.gittest.data.remote.api

import com.example.gittest.data.remote.model.ReadMeDto
import com.example.gittest.data.remote.model.RepoDto
import com.example.gittest.data.remote.model.RepoDetailsDto
import com.example.gittest.data.remote.model.UserInfoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("user")
    suspend fun signIn(): UserInfoDto

    @GET("user/repos")
    suspend fun getRepositories(
        @Query("per_page") perPage: Int = 10
    ): List<RepoDto>

    @GET("repositories/{repo_id}")
    suspend fun getRepository(
        @Path("repo_id") repoId: String
    ): RepoDetailsDto

    @GET("/repos/{owner}/{repo}/readme")
    suspend fun getReadme(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("ref") ref: String
    ): ReadMeDto
}