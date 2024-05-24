package com.my.composenews.data.model.response

import com.my.composenews.domain.vo.MessageVo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushDTO (
    @SerialName("uid")
    val uid: String?=null,
    @SerialName("age")
    val age: Int?=null,
    @SerialName("name")
    val name: String?=null
){
    fun toVo(): MessageVo{
        return MessageVo(
            uid = this.uid ?: "",
            age = this.age ?: 0,
            name = this.name ?: ""
        )
    }
}
