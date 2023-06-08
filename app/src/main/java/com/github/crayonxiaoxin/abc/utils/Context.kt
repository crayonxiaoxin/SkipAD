package com.github.crayonxiaoxin.abc.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.github.crayonxiaoxin.abc.R
import com.github.crayonxiaoxin.abc.model.AppInfo
import com.github.crayonxiaoxin.abc.service.SkipAdService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 检查服务是否开启
fun Context?.isAccessibilitySettingsOn(): Boolean {
    if (this == null) return false
    var accessibilityEnabled = 0
    val service = packageName + "/" + SkipAdService::class.java.canonicalName
    try {
        accessibilityEnabled = Settings.Secure.getInt(
            this.applicationContext.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED
        )
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
    }
    val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
    if (accessibilityEnabled == 1) {
        val settingValue = Settings.Secure.getString(
            this.applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService = mStringColonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
    }
    return false
}

fun Context.getAppInfoList(): List<AppInfo> {
    val installedApplications =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))
        } else {
            packageManager.getInstalledApplications(0)
        }
    return installedApplications.map {
        AppInfo(
            pkg = it.packageName.toString(),
            name = packageManager.getApplicationLabel(it).toString(),
            logo = packageManager.getApplicationIcon(it),
        )
    }
}

fun Context.getAppInfo(pkg: String): AppInfo? {
    return try {
        val a = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(pkg, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(pkg, 0)
        }
        AppInfo(
            pkg = a.packageName.toString(),
            name = packageManager.getApplicationLabel(a.applicationInfo).toString(),
            logo = packageManager.getApplicationIcon(a.applicationInfo),
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context?.getAppVersionName(): String {
    val packageInfo =
        this?.packageManager?.getPackageInfo(this.packageName, 0)
    return (packageInfo?.versionName) ?: ""
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(this@toast, message, duration).show()
    }
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    this.context?.toast(message, duration)
}