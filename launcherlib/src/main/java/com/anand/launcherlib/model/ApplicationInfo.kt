package com.anand.launcherlib.model

import android.graphics.drawable.Drawable

data class ApplicationInfo(
    var   label:String="",
    var   name:String="",
    var packageName:String="",
    var versionCode:Int=0,
    var versionName:String="",
    var icon:Drawable?=null
)
