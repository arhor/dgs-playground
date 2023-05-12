package com.github.mburyshynets.dgs.web.graphql.fetcher

import com.github.mburyshynets.dgs.graphql.generated.DgsConstants
import com.github.mburyshynets.dgs.graphql.generated.types.CreateExtraDataRequest
import com.github.mburyshynets.dgs.graphql.generated.types.EntityType
import com.github.mburyshynets.dgs.graphql.generated.types.ExtraData
import com.github.mburyshynets.dgs.graphql.generated.types.Post
import com.github.mburyshynets.dgs.graphql.generated.types.User
import com.github.mburyshynets.dgs.service.DataExtensionLookupKey
import com.github.mburyshynets.dgs.service.DataExtensionService
import com.github.mburyshynets.dgs.web.graphql.loader.DataExtensionBatchLoader
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import java.util.concurrent.CompletableFuture

@DgsComponent
class DataExtensionFetcher(private val dataExtensionService: DataExtensionService) {

    @DgsMutation
    fun createDataExtension(@InputArgument request: CreateExtraDataRequest): ExtraData {
        return dataExtensionService.createDataExtension(request)
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME)
    @DgsData(parentType = DgsConstants.POST.TYPE_NAME)
    fun extraData(@InputArgument names: List<String>, dfe: DgsDataFetchingEnvironment): CompletableFuture<List<ExtraData>> {
        val loader = dfe.getDataLoader<DataExtensionLookupKey, List<ExtraData>>(DataExtensionBatchLoader::class.java)
        val source = dfe.getSource<Any>()

        val sourceTypeName = source.javaClass.simpleName

        return loader.load(
            DataExtensionLookupKey(
                id = when (source) {
                    is User -> source.id
                    is Post -> source.id
                    else -> throw IllegalStateException("Unsupported entity type: $sourceTypeName")
                },
                type = EntityType.valueOf(sourceTypeName),
                names = names,
            )
        )
    }
}
