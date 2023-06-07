package com.github.crayonxiaoxin.abc.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity
import com.github.crayonxiaoxin.abc.base.BaseFragment
import com.github.crayonxiaoxin.abc.databinding.FragmentHomeBinding
import com.github.crayonxiaoxin.abc.navigate
import com.github.crayonxiaoxin.abc.ui.log.LogFragment
import com.github.crayonxiaoxin.abc.ui.rules.RulesFragment
import com.github.crayonxiaoxin.abc.utils._e
import com.github.crayonxiaoxin.abc.utils.getAppInfoList
import com.github.crayonxiaoxin.abc.utils.getAppVersionName
import com.github.crayonxiaoxin.abc.utils.isAccessibilitySettingsOn

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingVersion.setContent(context.getAppVersionName())

        binding.settingRules.setOnClickListener { navigate?.toPage(RulesFragment()) }
        binding.settingLog.setOnClickListener { navigate?.toPage(LogFragment()) }
        binding.settingVersion.setOnClickListener { }

        val accessibilityManager =
            context?.getSystemService(AppCompatActivity.ACCESSIBILITY_SERVICE) as? AccessibilityManager
        accessibilityManager?.let { am ->
            binding.settingEnable.setOnClickListener {
                openAccessibilitySetting()
            }
            binding.settingAutoStart.setOnClickListener {
                openAppDetailSetting()
            }
            binding.status.setOnCheckedChangeListener { _, _ ->
                binding.status.isChecked = am.isEnabled && context.isAccessibilitySettingsOn()
                openAccessibilitySetting()
            }
            accessibilityStatusChange(am.isEnabled && context.isAccessibilitySettingsOn())
            am.addAccessibilityStateChangeListener { isEnabled ->
                accessibilityStatusChange(isEnabled && context.isAccessibilitySettingsOn())
            }
        }

        context?.getAppInfoList()?.forEach {
            _e(msg = it.toString())
        }
    }

    // 打开应用信息页面 - 引导设置 "自启动"
    private fun openAppDetailSetting() {
        context?.let {
            val intent = Intent().apply {
                data = Uri.fromParts("package", it.packageName, null)
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
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
    }

}