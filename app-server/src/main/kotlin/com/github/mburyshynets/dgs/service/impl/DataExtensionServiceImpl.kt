package com.github.mburyshynets.dgs.service.impl

import com.github.mburyshynets.dgs.data.model.DataExtensionEntity
import com.github.mburyshynets.dgs.data.repository.DataExtensionRepository
import com.github.mburyshynets.dgs.graphql.generated.types.CreateDataExtensionRequest
import com.github.mburyshynets.dgs.graphql.generated.types.DataExtension
import com.github.mburyshynets.dgs.service.DataExtensionLookupKey
import com.github.mburyshynets.dgs.service.DataExtensionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicInteger

@Service
class DataExtensionServiceImpl @Autowired constructor(
    private val dataExtensionRepository: DataExtensionRepository,
) : DataExtensionService {

    @Transactional
    override fun createDataExtension(request: CreateDataExtensionRequest): DataExtension {
        val dataExtensionEntity = DataExtensionEntity(
            entityId = request.entityId,
            entityType = request.entityType.name,
            propertyName = request.propertyName,
            propertyValue = request.propertyValue,
        )
        return dataExtensionRepository.save(dataExtensionEntity).let {
            DataExtension(
                id = it.id!!.toString(),
                entityId = it.entityId,
                entityType = it.entityType,
                propertyName = it.propertyName,
                propertyValue = it.propertyValue
            )
        }
    }


    @Transactional(readOnly = true)
    override fun getDataExtensions(keys: Set<DataExtensionLookupKey>): Map<DataExtensionLookupKey, List<DataExtension>> {


        val target = HashMap<DataExtensionLookupKey, List<DataExtension>>()

        for (key in keys) {
            target[key] =
                dataExtensionRepository.findAllEntityTypeAndByEntityId(key.type.name, key.id).map {
                    DataExtension(
                        it.id!!.toString(),
                        it.entityId,
                        it.entityType,
                        it.propertyName,
                        it.propertyValue,
                    )
                }
        }
        return target
    }
}
