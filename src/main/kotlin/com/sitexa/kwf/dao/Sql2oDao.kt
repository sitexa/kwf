package com.sitexa.kwf.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.sql2o.Sql2o

/**
 * Created by open on 5/27/16.
 * Base model configuration a datasource, and maintain a connection pool.
 */
abstract class Sql2oDao {

    companion object {
        val config = HikariConfig()

        init {
            config.dataSourceClassName = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
            config.catalog = "sitexa"
            config.username = "root"
            config.password = "pop007"
            config.addDataSourceProperty("prepStmtCacheSize", "250")
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        }

        val dataSource = HikariDataSource(config)
        val sql2o = Sql2o(dataSource)

    }
}