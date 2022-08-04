package com.github.mburyshynets.dgs.data.repository

import com.github.mburyshynets.dgs.WithDatabaseContainer
import com.github.mburyshynets.dgs.config.DatabaseConfig
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.ContextConfiguration

@DataJdbcTest
@ContextConfiguration(classes = [DatabaseConfig::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal interface RepositoryTestBase : WithDatabaseContainer
