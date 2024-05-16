package com.my.composenews.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    val message: String
)
