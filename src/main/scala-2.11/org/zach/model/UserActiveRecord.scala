package org.zach.model

case class UserActiveRecord (
    user_id: String,
    user_created_date: Long,
    last_login: Long,
    last_create_group_date: Long,
    running_group_created_date: Long,
    last_mau: Long,
    login_duration: Double,
    create_group_duration: Double,
    running_group_duration: Double,
    running_group_num: Long,
    var mau_growth_rate: Double
)
