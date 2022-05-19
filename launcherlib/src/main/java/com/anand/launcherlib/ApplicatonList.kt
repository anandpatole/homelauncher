package com.anand.launcherlib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import com.anand.launcherlib.interfaces.NewAppListener
import com.anand.launcherlib.interfaces.UninstalledAppListener
import com.anand.launcherlib.model.ApplicationInfo
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList


class ApplicatonList : BroadcastReceiver()
{





    companion object {

        var intentFilter = IntentFilter()
        var newAppListener: WeakReference<NewAppListener>? = null
        var uninstalledAppListener: WeakReference<UninstalledAppListener>? = null
        fun getApplicationsInfo(context: Context): List<ApplicationInfo>? {
            val apps: MutableList<ApplicationInfo> = ArrayList()
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val pm: PackageManager = context.getPackageManager()
            val activities = pm.queryIntentActivities(intent, 0)
            Collections.sort(activities, ResolveInfo.DisplayNameComparator(pm))
            for (ri in activities) {
                val info = ApplicationInfo()
                info.packageName = ri.activityInfo.packageName
                info.name = ri.activityInfo.name
                info.label = ri.loadLabel(pm) as String
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    info.versionCode= pm.getPackageInfo(ri.activityInfo.packageName,0).longVersionCode.toInt()
                }
                info.versionName=pm.getPackageInfo(ri.activityInfo.packageName,0).versionName
                info.icon = ri.activityInfo.loadIcon(pm)
                apps.add(info)
            }

            return apps
        }

        fun registerListners(
                newAppListener: NewAppListener,
                uninstalledAppListener: UninstalledAppListener
        )
        {
            ApplicatonList.newAppListener=WeakReference(newAppListener)
            ApplicatonList.uninstalledAppListener= WeakReference(uninstalledAppListener)
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            intentFilter.addDataScheme("package")

        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        var action = intent?.getAction()
        Log.e("action",action.toString())
        val packageName = intent?.data?.encodedSchemeSpecificPart
        if (action!=null && context!=null) {
            if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {

                newAppListener?.get()?.newAppListener(packageName?:"")
            }
            else if(action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                uninstalledAppListener?.get()?.uninstallAppListener(packageName?:"")
            }
            else if(action.equals(Intent.ACTION_PACKAGE_FULLY_REMOVED))
            {
                uninstalledAppListener?.get()?.uninstallAppListener(packageName?:"")
            }
        }
    }
}