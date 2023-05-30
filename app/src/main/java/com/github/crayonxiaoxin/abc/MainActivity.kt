package com.github.crayonxiaoxin.abc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity
import com.github.crayonxiaoxin.abc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = this.javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val skipList = arrayListOf<String>(" 跳过", "跳过 1", "跳过 ", "1 跳过", "跳过广告", "跳 过")
        skipList.forEach {
            if ("(.*)跳过(.*)".toRegex().matches(it.replace(" ", ""))) {
                Log.e(TAG, "onCreate: 符合要求 - [$it]")
            }
        }

        val accessibilityManager = getSystemService(ACCESSIBILITY_SERVICE) as? AccessibilityManager
        accessibilityManager?.let { am ->
            binding.helloWorld.setOnClickListener {
                openAccessibilitySetting()
            }
            binding.autoStart.setOnClickListener {
                openAppDetailSetting()
            }
            binding.status.setOnCheckedChangeListener { _, _ ->
                binding.status.isChecked = am.isEnabled
                openAccessibilitySetting()
            }
            accessibilityStatusChange(am.isEnabled)
            am.addAccessibilityStateChangeListener { isEnabled ->
                accessibilityStatusChange(isEnabled)
            }
        }
    }

    // 打开应用信息页面 - 引导设置 "自启动"
    private fun openAppDetailSetting() {
        val intent = Intent().apply {
            data = Uri.fromParts("package", packageName, null)
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    // 打开无障碍设置页面 - 引导打开无障碍
    private fun openAccessibilitySetting() {
        val intent = Intent().apply {
            action = Settings.ACTION_ACCESSIBILITY_SETTINGS
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun accessibilityStatusChange(isEnabled: Boolean) {
        binding.status.isChecked = isEnabled
        if (isEnabled) {
            binding.helloWorld.text = "已开启"
            binding.autoStart.visibility = View.VISIBLE
        } else {
            binding.helloWorld.text = "已关闭"
            binding.autoStart.visibility = View.GONE
        }
    }
}