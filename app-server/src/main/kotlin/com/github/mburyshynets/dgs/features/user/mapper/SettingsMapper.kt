package com.github.mburyshynets.dgs.features.user.mapper;

import com.github.mburyshynets.dgs.features.user.entity.Setting
import com.github.mburyshynets.dgs.features.user.entity.Settings
import com.github.mburyshynets.dgs.common.MapstructCommonConfig
import org.mapstruct.Mapper
import java.util.EnumSet

@Mapper(config = MapstructCommonConfig::class)
abstract class SettingsMapper {

    fun wrap(value: EnumSet<Setting>?): Settings = Settings(value.emptySetIfNull())

    fun unwrap(value: Settings?): EnumSet<Setting> = value?.items.emptySetIfNull()

    private fun EnumSet<Setting>?.emptySetIfNull() = this ?: EnumSet.noneOf(Setting::class.java)
}
