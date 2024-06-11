package com.my.composenews.domain.form

data class ChannelError(
    val isError: Boolean = false,
    val errMsg: String = ""
)