package com.github.crayonxiaoxin.abc.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期格式化
 * @param fmt 格式
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:52 上午
 */
fun Date.fmt(fmt: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.CHINA): String {
    val simpleDateFormat = SimpleDateFormat(fmt, locale)
    return simpleDateFormat.format(this)
}

/**
 * 反格式化，已格式化日期转 Date 对象
 * @param fmt 格式
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:52 上午
 */
fun String?.dateStr2Date(
    fmt: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.CHINA
): Date? {
    if (this.isNullOrEmpty()) return null
    val simpleDateFormat = SimpleDateFormat(fmt, locale)
    return simpleDateFormat.parse(this)
}

/**
 * 格式化日期的重新格式化
 * @param reFmt 新的格式
 * @param fmt 原来的格式
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:53 上午
 */
fun String?.dateStrReformat(
    reFmt: String = "yyyy-MM-dd HH:mm",
    fmt: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.CHINA
): String {
    val dateStr2Date = this.dateStr2Date(fmt, locale) ?: return ""
    return dateStr2Date.fmt(reFmt, locale)
}

/**
 * 数字与星期转换（仅适用于 Hera）
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:56 上午
 */
fun Int.toWeekDay(prefix: String = ""): String {
    if (this > 7 || this < 0) return ""
    return prefix + when (this) {
        1 -> "一"
        2 -> "二"
        3 -> "三"
        4 -> "四"
        5 -> "五"
        6 -> "六"
        else -> "日"
    }
}

/**
 * 整型前置补0
 * @param digit 位数
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:57 上午
 */
fun Int.toFixed(digit: Int = 2): String {
    return String.format("%0${digit}d", this)
}

/**
 * 毫秒 转 时分秒字符串
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:58 上午
 */
fun Long.secondsFmt(): String {
    val hours = this / (60 * 60)
    val minutes = this % (60 * 60) / 60
    val seconds = this % 60
    return hours.toInt().toFixed() + ":" + minutes.toInt().toFixed() + ":" + seconds.toInt()
        .toFixed()
}

/**
 * 毫秒 转 秒
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:59 上午
 */
fun Long.ms2s(): Long = this / 1000

/**
 * 今日日期和原始时间组合，忽略原始日期
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:59 上午
 */
fun String?.todayFmt(): String {
    if (this.isNullOrEmpty()) return ""
    val time = this.dateStrReformat("HH:mm:ss")
    return if (time.isNotEmpty()) {
        val c = Calendar.getInstance(Locale.CHINA)
        c.time.fmt("yyyy-MM-dd $time")
    } else {
        ""
    }
}

/**
 * 明日日期和原始时间组合，忽略原始日期
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:59 上午
 */
fun String?.tomorrowFmt(): String {
    if (this.isNullOrEmpty()) return ""
    val time = this.dateStrReformat("HH:mm:ss")
    return if (time.isNotEmpty()) {
        val c = Calendar.getInstance(Locale.CHINA)
        c.add(Calendar.DAY_OF_MONTH, 1) // 当前时间增加一天
        c.time.fmt("yyyy-MM-dd $time")
    } else {
        ""
    }
}

/**
 * 昨日日期和原始时间组合，忽略原始日期
 *
 * @Author: Lau
 * @Date: 2022/7/13 11:59 上午
 */
fun String?.yesterdayFmt(): String {
    if (this.isNullOrEmpty()) return ""
    val time = this.dateStrReformat("HH:mm:ss")
    return if (time.isNotEmpty()) {
        val c = Calendar.getInstance(Locale.CHINA)
        c.add(Calendar.DAY_OF_MONTH, -1) // 当前时间减少一天
        c.time.fmt("yyyy-MM-dd $time")
    } else {
        ""
    }
}

/**
 * 日期 添加分鐘
 */
fun Date.addMinutes(minutes: Int): Date {
    val c = Calendar.getInstance(Locale.CHINA)
    c.time = this
    c.add(Calendar.MINUTE, minutes)
    return c.time
}

/**
 * 是否大于今天0时
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:00 下午
 */
fun Date?.biggerThanToday(): Boolean {
    val today = todayStart().dateStr2Date()
    if (this == null || today == null) return true
    return this.time > today.time
}

/**
 * 日期是否在现在之前 或 现在时间是否超过了对应日期时间
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:01 下午
 */
fun Date?.smallerThanNow(includeEqual: Boolean = false): Boolean {
    if (this == null) return true
    return if (includeEqual) {
        this.time <= System.currentTimeMillis()
    } else {
        this.time < System.currentTimeMillis()
    }
}

/**
 * 日期是否在现在之后 或 现在时间未超过了对应日期时间
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:01 下午
 */
fun Date?.biggerThanNow(includeEqual: Boolean = false): Boolean {
    if (this == null) return true
    return if (includeEqual) {
        this.time >= System.currentTimeMillis()
    } else {
        this.time > System.currentTimeMillis()
    }
}

/**
 * 今天日期格式化
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:02 下午
 */
fun todayNow(fmt: String = "yyyy-MM-dd HH:mm:ss"): String {
    val c = Calendar.getInstance(Locale.CHINA)
    return c.time.fmt(fmt)
}

/**
 * 今天是星期几（仅适用于 Hera）
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:03 下午
 */
fun todayWeekday(): Int {
    val c = Calendar.getInstance(Locale.CHINA)
    // sunday=0,monday=1  =>  api sunday=7,monday=1
    var i = c.get(Calendar.DAY_OF_WEEK) - 1
    if (i == 0) i = 7
    return i
}

/**
 * 今天是这个月的第几天
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:04 下午
 */
fun todayOfMonth(): Int {
    val c = Calendar.getInstance(Locale.CHINA)
    return c.get(Calendar.DAY_OF_MONTH)
}

/**
 * 今天0时
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:04 下午
 */
fun todayStart(): String {
    val c = Calendar.getInstance(Locale.CHINA)
    return c.time.fmt("yyyy-MM-dd 00:00:00")
}

/**
 * 今天结束
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:04 下午
 */
fun todayEnd(): String {
    val c = Calendar.getInstance(Locale.CHINA)
    return c.time.fmt("yyyy-MM-dd 23:59:59")
}