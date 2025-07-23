package com.example.gittest.data.mapper

import com.example.gittest.data.remote.model.UserInfoDto
import com.example.gittest.domain.model.UserInfo

fun UserInfoDto.toDomain(): UserInfo {
    return UserInfo(
        id = id,
        login = login,
        name = name,
        avatarUrl = avatar_url,
        profileUrl = html_url
    )
}