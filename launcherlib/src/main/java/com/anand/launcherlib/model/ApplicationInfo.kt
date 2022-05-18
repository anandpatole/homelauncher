package com.anand.launcherlib.model

import android.graphics.drawable.Drawable

data class ApplicationInfo(
    var   label:String="",
    var   name:String="",
    var packageName:String="",
    var icon:Drawable?=null
)
