package org.zach.functions.model

case class User(
   id: String,
   email: String,
   password: String,
   salt: String,
   created_at: Long,
   invite_code: Option[String] = None,
   features: Seq[String] = Seq(),
   actived: Boolean = false, // 激活否
   operation_info: Map[String, Any] = Map(), // tags, phone, from
   business_info: Map[String, Any] = Map(),
   last_login: Option[Long] = None, // 可能从未登录
   blocked: Boolean = false, // 黑名单否
   notify_emails: Seq[String] = Seq(), // 该用户的运营跟踪者邮箱, 可能有多个运营,
   third_party_from: Option[String] = None,
   third_party_id: Option[String] = None,
   role: String = "Owner",
   owner_id: Option[String] = None,
   owner_email: Option[String] = None,
   owner_comment: Option[String] = None,
   product: String // abtest | asotest
)

