package com.github.mburyshynets.dgs.service.impl

import com.github.mburyshynets.dgs.data.model.Post
import com.github.mburyshynets.dgs.data.model.User
import com.github.mburyshynets.dgs.graphql.generated.types.PostDto
import com.github.mburyshynets.dgs.graphql.generated.types.UserDto

fun User.toDto() = UserDto(
    id = id!!,
    username = username,
    settings = settings?.items,
)

fun Post.toDto() = PostDto(
    id = id!!,
    userId = userId,
    content = content,
)
