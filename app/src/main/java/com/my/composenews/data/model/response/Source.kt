package com.my.composenews.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String?=null,
    val name: String = ""
)