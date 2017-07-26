# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/liugaoxin/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses


-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Application
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.app.Service
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep class android.support.v7.widget.** { *;}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable { *; }

-keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

-keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

-keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

-keepclassmembers class **.R$* {*;}

-dontwarn android.support.**

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.

-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

##--------------- SDPLog ---------------
-keep class com.shengpay.logger.** { *; }
##--------------- cat ---------------
-keep class com.analysis.** { *; }
#--------------- okhttp ---------------
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
##--------------- baidu ---------------
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
##--------------- okio ---------------
-dontwarn okio.**
-keep class okio.**{*;}
##--------------- 语音播报 ---------------
-keep class com.iflytek.**{*;}
##--------------- bugly ---------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
##--------------- zxing ---------------
-dontwarn com.google.zxing.**
-keep  class com.google.zxing.**{*;}
##--------------- recylerView ---------------
-dontwarn android.support.v7.widget.RecyclerView.**
-keep  class android.support.v7.widget.RecyclerView.**{*;}
##--------------- 阿里推送 ---------------
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class com.ut.** {*;}
-keep class com.ta.** {*;}
-keep class anet.**{*;}
-keep class anetwork.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-keep class android.os.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn anetwork.**
-dontwarn com.ut.**
-dontwarn com.ta.**