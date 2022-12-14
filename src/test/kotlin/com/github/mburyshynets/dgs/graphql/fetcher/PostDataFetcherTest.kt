package com.github.mburyshynets.dgs.graphql.fetcher

import com.github.mburyshynets.dgs.graphql.generated.types.PostDto
import com.github.mburyshynets.dgs.graphql.generated.types.UserDto
import com.github.mburyshynets.dgs.graphql.loader.PostDataLoader
import com.github.mburyshynets.dgs.service.PostService
import com.github.mburyshynets.dgs.service.UserService
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@DgsTest
@ContextConfiguration(classes = [UserDataFetcher::class, PostDataFetcher::class, PostDataLoader::class])
internal class PostDataFetcherTest {

    @MockkBean
    private lateinit var postService: PostService

    @MockkBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var dgsQueryExecutor: DgsQueryExecutor

    @Test
    fun `should return successful result containing empty list of user posts`() {
        // given
        val user = UserDto(id = 1, username = "test-user")
        val post = PostDto(id = 2, userId = user.id, content = "test-post")

        every { userService.getUserByUsername(any()) } returns user
        every { postService.getPostsByUserIds(any()) } returns mapOf(user.id!! to listOf(post))

        // when
        val result = dgsQueryExecutor.execute(
            """
            query {
                user(username: "${user.username}") {
                    posts {
                        id
                        userId
                        content
                    }
                }
            }
            """
        )

        // then
        verify { userService.getUserByUsername(user.username!!) }
        verify { postService.getPostsByUserIds(setOf(user.id!!)) }

        assertThat(result.errors)
            .isEmpty()
        assertThat(result.isDataPresent)
            .isTrue
        assertThat(result.getData<Map<Any, Any>>())
            .containsEntry(
                "user", mapOf(
                    "posts" to listOf(
                        mapOf(
                            "id" to post.id,
                            "userId" to post.userId,
                            "content" to post.content
                        )
                    )
                )
            )
    }
}
