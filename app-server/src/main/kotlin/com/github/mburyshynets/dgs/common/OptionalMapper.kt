package com.github.mburyshynets.dgs.common;

import org.mapstruct.Mapper
import java.util.Optional

@Mapper(config = MapstructCommonConfig::class)
abstract class OptionalMapper {

    fun <T : Any> wrap(value: T): Optional<T> = Optional.ofNullable(value)

    fun <T> unwrap(value: Optional<T>): T? = value.orElse(null)
}
