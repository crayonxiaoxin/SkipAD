package com.github.crayonxiaoxin.abc

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.crayonxiaoxin.abc.base.BaseActivity
import com.github.crayonxiaoxin.abc.base.BaseFragment
import com.github.crayonxiaoxin.abc.databinding.ActivityMainBinding
import com.github.crayonxiaoxin.abc.ui.home.HomeFragment
import com.github.crayonxiaoxin.abc.utils.fitSystemBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity(), IMain {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fitSystemBar(darkIcons = true)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toPage(HomeFragment())
    }

    override fun toPage(f: Fragment, tag: String?) {
        addFragment(R.id.frame_layout, f, tag)
    }

    override fun back() {
        this.onBackPressed()
    }

    private var isExit = false

    override fun onBackPressed() {
        val fm = supportFragmentManager
        val count = fm.backStackEntryCount
        if (count > 1) {
            with(fm.findFragmentById(R.id.frame_layout)) {
                if (this is BaseFragment) {
                    if (this.onBackPressed()) {
                        fm.popBackStack()
                    }
                } else {
                    super.onBackPressed()
                }
            }
        } else if (count == 1) {
            if (isExit) {
                this.finish()
            } else {
                isExit = true
                Toast.makeText(this, getString(R.string.exit_press_one_more), Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    delay(1500)
                    isExit = false
                }
            }
        } else {
            super.onBackPressed()
        }
    }
}