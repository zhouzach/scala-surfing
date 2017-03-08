package sdbc

import com.alibaba.druid.pool.DruidDataSource

object DruidDBPool {

  def getDataSource(url: String): DruidDataSource = {
    val dataSource = new DruidDataSource()
    dataSource.setDriverClassName("org.apache.hive.jdbc.HiveDriver")
    dataSource.setUrl(url)
    dataSource.setInitialSize(10)
    dataSource.setMinIdle(10)
    dataSource.setMaxActive(100)

    dataSource.setMaxWait(60000)
    dataSource.setTimeBetweenEvictionRunsMillis(60000)
    dataSource.setMinEvictableIdleTimeMillis(300000)

    dataSource.setValidationQuery("SELECT 'x'")
    dataSource.setTestWhileIdle(true)
    dataSource.setTestOnBorrow(false)
    dataSource.setTestOnReturn(false)

    dataSource.setPoolPreparedStatements(false)
    dataSource.setMaxPoolPreparedStatementPerConnectionSize(10)
    dataSource.setFilters("stat")

    dataSource
  }

}
