package com.github.mburyshynets.dgs.graphql.fetcher

import com.github.mburyshynets.dgs.config.CustomScalarsConfig
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.autoconfig.DgsExtendedScalarsAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(
    classes = [
        CustomScalarsConfig::class,
        DgsAutoConfiguration::class,
        DgsExtendedScalarsAutoConfiguration::class,
    ]
)
@Import
annotation class DgsTest
