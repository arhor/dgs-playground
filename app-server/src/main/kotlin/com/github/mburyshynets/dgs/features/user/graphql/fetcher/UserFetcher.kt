package com.github.mburyshynets.dgs.features.user.graphql.fetcher

import com.github.mburyshynets.dgs.common.Limit
import com.github.mburyshynets.dgs.common.Offset
import com.github.mburyshynets.dgs.features.user.ExtendedUserDetails
import com.github.mburyshynets.dgs.features.user.UserService
import com.github.mburyshynets.dgs.generated.graphql.types.CreateUserRequest
import com.github.mburyshynets.dgs.generated.graphql.types.User
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.core.annotation.AuthenticationPrincipal

@DgsComponent
class UserFetcher(private val userService: UserService) {

    @DgsMutation
    fun createUser(@InputArgument request: CreateUserRequest): User {
        return userService.createNewUser(request)
    }

    @DgsQuery
    fun users(@InputArgument offset: Int, @InputArgument limit: Int): List<User> {
        return userService.getAllUsers(Offset.of(offset), Limit.of(limit))
    }

    @DgsQuery
    fun user(@InputArgument username: String): User {
        return userService.getUserByUsername(username)
    }

    @DgsQuery
    fun currentUser(@AuthenticationPrincipal currentUser: ExtendedUserDetails?): User? {
        return currentUser?.let {
            User(
                id = it.id.toString(),
                username = it.username,
            )
        }
    }
}
