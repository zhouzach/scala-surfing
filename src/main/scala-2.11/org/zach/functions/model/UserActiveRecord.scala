package org.zach.functions.model

import org.joda.time.DateTime

case class UserActiveRecord (
    user_id: String,
    user_created_date: Long = DateTime.now().getMillis,
    last_login: Long = DateTime.now().getMillis,
    last_create_group_date: Long = DateTime.now().getMillis,
    running_group_created_date: Long = DateTime.now().getMillis,
    last_mau: Long = 0L,
    login_duration: Double = 0.0,
    create_group_duration: Double = 0.0,
    running_group_duration: Double =0.0,
    running_group_num: Long = 100,
    var mau_growth_rate: Double = 0.3
)
