package com.zjy.architecture.util.rom

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.zjy.architecture.R
import com.zjy.architecture.ext.toast

/**
 * @author zhengjy
 * @since 2021/07/21
 * Description:
 */
interface Rom {

    /**
     * 是否允许锁屏显示界面
     */
    fun canShowViewOnLockScreen(context: Context): Boolean

    /**
     * 是否允许后台弹出界面
     */
    fun isBackgroundStartAllowed(context: Context): Boolean

    /**
     * 跳转到app权限设置页面
     */
    fun openPermissionSetting(context: Context)

    /**
     * 跳转到app通知设置页面
     */
    fun openNotificationSetting(context: Context, channelId: String)

    /**
     * 是否需要跳转安装应用许可页面
     *
     * 部分Rom不需要跳转即可安装
     */
    fun needAuthInstallPermission(context: Context): Boolean

    open class DefaultRom : Rom {
        override fun canShowViewOnLockScreen(context: Context): Boolean {
            return true
        }

        override fun isBackgroundStartAllowed(context: Context): Boolean {
            return true
        }

        override fun openPermissionSetting(context: Context) {
            openAppSettings(context)
        }

        override fun openNotificationSetting(context: Context, channelId: String) {
            val intent = Intent().apply {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                        // android 8.0引导
                        action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                        // android 5.0-7.0
                        action = "android.settings.APP_NOTIFICATION_SETTINGS"
                        putExtra("app_package", context.packageName)
                        putExtra("app_uid", context.applicationInfo.uid)
                    }
                    else -> {
                        // 其他
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                }
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                val i = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", context.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                try {
                    context.startActivity(i)
                } catch (e: Exception) {
                    context.toast(R.string.arch_error_unable_open_notification_setting)
                }
            }
        }

        override fun needAuthInstallPermission(context: Context): Boolean {
            return false
        }

        /**
         * 打开App设置页面
         */
        protected fun openAppSettings(context: Context) {
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", context.packageName, null)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                context.toast(R.string.arch_error_unable_open_setting)
            }
        }
    }
}