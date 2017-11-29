package org.zach.inheriting

import java.io.File
import java.util.Map.Entry
import java.util.concurrent.ConcurrentHashMap

import com.typesafe.config.{Config, ConfigFactory, ConfigValue}

object MyApp2 extends App {
  val serverConf = ServerConf2.apply()
  val port = serverConf.port().get
  println(port)

}

trait ClientConf2[C <: ClientConf2[C]] {
  val config = new ConcurrentHashMap[String, Any]()

  def get[V](key: String): Option[V] = {
    config.get(key) match {
      case x: V => Some(x)
      case _ => None
    }
  }

  @SuppressWarnings(Array("unchecked"))
  def set(key: String, value: Any): this.type = {
    config.put(key, value)
    this
  }

  def loadFromResource(name: String): this.type = {
    Some(name).map(ConfigFactory.load).map(loadProperties)
    this
  }

  def loadFromFile(name: String): this.type = {
    getConfigFile(name).map(loadConfig).map(loadProperties)
    this
  }

  private def getConfigFile(name: String): Option[File] = {
    Some(name).map(new File(".", _)).filter(_.exists())
  }

  private def loadConfig(file: File): Config = {
    ConfigFactory.parseFile(file)
  }

  def loadProperties(config: Config)
}

class ServerConf2(loadFromDefaults: Boolean) extends ClientConf2[ServerConf2] {
  if (loadFromDefaults) {
    loadFromResource(ServerConf2.SERVICE_CONF)
  }

  override def loadProperties(config: Config) = {
    val agentConfig = config.getConfig("server")
    for (entry <- agentConfig.entrySet().toArray) {
      val obj = entry.asInstanceOf[Entry[String, ConfigValue]]
      set(obj.getKey, obj.getValue.unwrapped())
    }
  }

  def ip(): Option[String] = get("ip")

  def port(): Option[Int] = get[Int]("port")

  def urlSecret(): Option[String] = get[String]("urlSecret")

  def sessionTimeout(): Int = get[Int]("sessionTimeout").getOrElse(20)

  def groupId(): String = get[String]("groupId").getOrElse("itoa")
}

object ServerConf2 {
  private val SERVICE_CONF = "application.conf"

  def apply() = {
    new ServerConf2(true).loadFromFile(SERVICE_CONF)
  }
}
