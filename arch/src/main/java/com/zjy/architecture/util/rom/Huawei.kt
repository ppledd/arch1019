package com.zjy.architecture.util.rom

import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * @author zhengjy
 * @since 2021/07/21
 * Description:
 */
open class Huawei : Rom.DefaultRom() {

    override fun openPermissionSetting(context: Context) {
        val intent = Intent()
        intent.putExtra("packageName", context.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.component = ComponentName(
            "com.huawei.systemmanager",
            "com.huawei.permissionmanager.ui.MainActivity"
        )
        context.startActivity(intent)
    }

    override fun openNotificationSetting(context: Context, channelId: String) {
        openAppSettings(context)
    }
}