package org.rabbit.inheriting

import java.io.File
import java.util.Map.Entry
import java.util.concurrent.ConcurrentHashMap

import com.typesafe.config.{Config, ConfigFactory, ConfigValue}
import org.slf4j.LoggerFactory

object MyApp extends App {
  val serverConf = ServerConfig.apply()
  val port = serverConf.port().get
  println(port)

}

trait ClientConfig [C <: ClientConfig[C]] {

    val configs: ConcurrentHashMap[String, Any]
//  val configs = new ConcurrentHashMap[String, Any]()

  def getConfigs(key: String): ConcurrentHashMap[String, Any] = {
        val conf = new ConcurrentHashMap[String, Any]()
    val agentConfig = loadFromResource(key)
    for (entry <- agentConfig.entrySet().toArray) {
      val obj = entry.asInstanceOf[Entry[String, ConfigValue]]
      conf.put(obj.getKey, obj.getValue.unwrapped())
    }
    conf
  }

  val logger = LoggerFactory.getLogger(this.getClass)

  def get[V](key: String): Option[V] = {
    configs.get(key) match {
      case x: V => Some(x)
      case _ => None
    }
  }

  private def loadFromResource(key: String): Config = {
    val SERVICE_CONF = "application.conf"
    val file = new File(".", SERVICE_CONF)
    if (file.exists()) {
      loadConfig(file).getConfig(key)
    } else {
      ConfigFactory.load(SERVICE_CONF).getConfig(key)
    }
  }

  private def loadConfig(file: File): Config = {
    ConfigFactory.parseFile(file)
  }
}

class ServerConfig() extends ClientConfig[ServerConfig] {

//  override val configs = getConfigs("server")
  val configs = getConfigs("server")

  def port(): Option[Int] = get[Int]("port")

}

object ServerConfig {
  def apply() = new ServerConfig()
}

