package com.github.mburyshynets.dgs.common

import org.mapstruct.Mapping

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Mapping(target = "version", ignore = true)
@Mapping(target = "createdDateTime", ignore = true)
@Mapping(target = "updatedDateTime", ignore = true)
annotation class IgnoreAuditMappings
