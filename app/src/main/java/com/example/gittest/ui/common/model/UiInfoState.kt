package com.example.gittest.ui.common.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class UiInfoState(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    @StringRes val buttonText: Int
)

