package com.github.mburyshynets.dgs.features.extradata.graphql.loader

import com.github.mburyshynets.dgs.features.extradata.ExtraDataService
import com.github.mburyshynets.dgs.generated.graphql.types.ExtendedEntityType
import com.github.mburyshynets.dgs.generated.graphql.types.ExtraData
import com.netflix.graphql.dgs.DgsDataLoader
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

sealed class ExtraDataBatchLoader(
    private val asyncExecutor: Executor,
    private val extraDataService: ExtraDataService,
) : MappedBatchLoader<String, ExtraData> {

    abstract val entityType: ExtendedEntityType

    override fun load(keys: Set<String>): CompletableFuture<Map<String, ExtraData>> {
        return CompletableFuture.supplyAsync(
            { extraDataService.getExtraDataInBatch(entityType, keys) },
            asyncExecutor
        )
    }

    @DgsDataLoader(name = "userExtraData", maxBatchSize = 250)
    class ForUser(executor: Executor, service: ExtraDataService) : ExtraDataBatchLoader(executor, service) {
        override val entityType = ExtendedEntityType.User
    }

    @DgsDataLoader(name = "postExtraData", maxBatchSize = 250)
    class ForPost(executor: Executor, service: ExtraDataService) : ExtraDataBatchLoader(executor, service) {
        override val entityType = ExtendedEntityType.Post
    }
}
