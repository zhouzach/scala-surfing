package org.zach.utils

import java.sql.{Connection, ResultSet, SQLException}

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object C3P0Tools {
  val logger = LoggerFactory.getLogger(getClass)

  val config = ConfigFactory.load()
  val driverClass = config.getString("ctx.dataSourceClassName")
  val jdbcUrl = config.getString("ctx.dataSource.url")
  val user = config.getString("ctx.dataSource.user")
  val password = config.getString("ctx.dataSource.password")

//  val initialPoolSize = config.getInt("ctx.dataSource.initialPoolSize")
//  val maxPoolSize = config.getInt("ctx.dataSource.maxPoolSize")
//  val minPoolSize = config.getInt("ctx.dataSource.minPoolSize")
//  val maxStatements = config.getInt("ctx.dataSource.maxStatements")

  val dataSource = new ComboPooledDataSource()
  dataSource.setDriverClass(driverClass)
  dataSource.setJdbcUrl(jdbcUrl)
  dataSource.setUser(user)
  dataSource.setPassword(password)

//  dataSource.setInitialPoolSize(initialPoolSize)
//  dataSource.setMaxPoolSize(maxPoolSize)
//  dataSource.setMinPoolSize(minPoolSize)
//  dataSource.setMaxStatements(maxStatements)
  println(dataSource)
  println(dataSource.getConnection)
  println(getConnection())

  def getDataSource = {
    dataSource
  }
  def getConnection(): Option[Connection] = {
    try {
      Some(dataSource.getConnection)
    } catch {
      case sqlExp: SQLException =>
        sqlExp.printStackTrace()
        logger.info(sqlExp.toString)
        None
      case exp: Exception =>
        exp.printStackTrace()
        logger.info(exp.toString)
        None
    }
  }

  def close(connection: Connection) = {
    if(connection != null) {
      try {
        connection.close()
      }catch {
        case sqlExp: SQLException =>
          sqlExp.printStackTrace()
          logger.info(sqlExp.toString)
        case exp: Exception =>
          exp.printStackTrace()
          logger.info(exp.toString)
      }
    }
  }

  def execQuery[T](connection: Option[Connection], query: String)(function: Option[ResultSet] => T) = {
    try {
      if(connection.nonEmpty) {
        val statement = connection.get.createStatement()
        val rs = statement.executeQuery(query)
        function(Some(rs))
      } else {
        function(None)
      }
    }catch {
      case sqlExp: SQLException =>
        sqlExp.printStackTrace()
        logger.info(sqlExp.toString)
        function(None)
      case exp: Exception =>
        exp.printStackTrace()
        logger.info(exp.toString)
        function(None)
    }finally {
      connection match {
        case Some(conn) => close(connection.get)
        case None =>
      }
    }
  }

  def execUpdate[T](connection: Option[Connection], updateStr: String) = {
    try {
      if(connection.nonEmpty) {
        val statement = connection.get.createStatement()
        statement.executeUpdate(updateStr)
      }
    }catch {
      case sqlExp: SQLException =>
        sqlExp.printStackTrace()
        logger.info(sqlExp.toString)
      case exp: Exception =>
        exp.printStackTrace()
        logger.info(exp.toString)
    }finally {
      connection match {
        case Some(conn) => close(connection.get)
        case None =>
      }
    }
  }
}
