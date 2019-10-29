@file:JvmName("Preferences")

package com.numeron.wandroid.other

import com.numeron.util.PreferencesDelegate


var userId: Long by PreferencesDelegate(preferences, 0)
var accessToken: String by PreferencesDelegate(preferences, "")