package com.github.mburyshynets.dgs.service.impl

import com.github.mburyshynets.dgs.data.model.Post
import com.github.mburyshynets.dgs.data.repository.PostRepository
import com.github.mburyshynets.dgs.graphql.generated.types.PostDto
import com.github.mburyshynets.dgs.service.PostService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostServiceImpl(private val postRepository: PostRepository) : PostService {

    override fun createNewPost(userId: Long, content: String): PostDto {
        return postRepository.save(
            Post(
                userId = userId,
                content = content
            )
        ).toDto()
    }

    @Transactional(readOnly = true)
    override fun getPostsByUserIds(keys: Set<Long>): Map<Long, List<PostDto>> {
        return postRepository
            .findAllByUserIdIn(keys)
            .map { it.toDto() }
            .groupBy { it.userId!! }
    }
}
