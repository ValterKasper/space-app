# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/valter/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Dagger
-dontwarn com.google.errorprone.annotations.**

# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# Okio
-dontwarn okio.**

# OkHttp
-dontwarn javax.annotation.**

# PrettyTime
-keep class org.ocpsoft.prettytime.i18n.**

# Data classes
-keep class sk.kasper.remote.entity.** { *; }
-keep class sk.kasper.space.database.entity.** { *; }

# Crashlytics
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

-dontnote com.google.android.gms.**
-dontnote com.google.firebase.crash.FirebaseCrash
-dontnote org.robolectric.Robolectric
-dontnote android.os.SystemProperties
-dontnote com.google.protobuf.**
-dontnote sun.misc.Unsafe
-dontnote libcore.io.Memory
-dontnote kotlin.internal.**
-dontnote kotlin.reflect.jvm.internal.**
-dontnote kotlin.jvm.internal.**
-dontnote com.android.org.conscrypt.SSLParametersImpl
-dontnote org.apache.harmony.xnet.provider.jsse.SSLParametersImpl
-dontnote sun.security.ssl.SSLContextImpl
-dontnote dalvik.system.CloseGuard
-dontnote com.google.android.material.**
-dontnote com.google.firebase.analytics.connector.internal.AnalyticsConnectorRegistrar
-dontnote sk.kasper.space.view.OrbitView
-dontwarn okhttp3.internal.platform.*



