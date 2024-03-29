package com.github.mburyshynets.dgs.features.forum.graphql.fetcher

import com.github.mburyshynets.dgs.generated.graphql.types.CreateTopicRequest
import com.github.mburyshynets.dgs.generated.graphql.types.Topic
import com.github.mburyshynets.dgs.features.user.ExtendedUserDetails
import com.github.mburyshynets.dgs.features.forum.TopicService
import com.github.mburyshynets.dgs.common.Limit
import com.github.mburyshynets.dgs.common.Offset
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal

@DgsComponent
class TopicFetcher(private val topicService: TopicService) {

    @DgsMutation
    @PreAuthorize("isAuthenticated()")
    fun createTopic(
        @InputArgument request: CreateTopicRequest,
        @AuthenticationPrincipal currentUser: ExtendedUserDetails,
    ): Topic {
        return topicService.createNewTopic(request, currentUser)
    }

    @DgsQuery
    fun topics(@InputArgument offset: Int, @InputArgument limit: Int): List<Topic> {
        return topicService.getAllTopics(Offset(offset), Limit(limit))
    }
}
