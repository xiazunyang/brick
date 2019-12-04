@file:JvmName("Preferences")

package com.numeron.wandroid.other

import com.numeron.delegate.PreferencesDelegate


var userId: Long by PreferencesDelegate(preferences, 0)
var accessToken: String by PreferencesDelegate(preferences, "")