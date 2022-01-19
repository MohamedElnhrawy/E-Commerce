-dontwarn androidx.databinding.**
-keep class androidx.databinding.** { *; }
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

-keep class com.gtera.data.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn rx.**

-keepattributes *Annotation*,EnclosingMethod,Signature
-dontwarn com.squareup.okhttp.**

-keepattributes EnclosingMethod
-keepattributes InnerClasses

-optimizationpasses 5

-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
# for firebase auth
-keepattributes *Annotation*
#Keep classes that are referenced on the AndroidManifest
-keep public class * extends androidx.app.Activity
-keep public class * extends androidx.app.Application
-keep public class * extends androidx.app.Service
-keep public class * extends androidx.content.BroadcastReceiver
-keep public class * extends androidx.content.ContentProvider
-keep public class com.androidx.vending.licensing.ILicensingService

#To maintain custom components names that are used on layouts XML:
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#Keep the R
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep class .R
-keep class **.R$* {
    <fields>;
}

#Uncomment if using Serializable
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


-keep class **$$ViewBinder { *; }

-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep class androidx.appcompat.widget.** { *; }

# Retrofit
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#Gson & hawk
-keep class com.google.gson.** { *; }
#-keep class org.sqlite.** { *; }
#-keep class org.sqlite.database.** { *; }


## GSON 2.2.4 specific rules ##

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

-keepattributes EnclosingMethod

# Gson specific classes
-keep class com.google.gson.stream.** { *; }

#proguard
-dontwarn com.squareup.okhttp.**


# Crashlytics 2.+
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile, LineNumberTable, *Annotation*

# If you are using custom exceptions, add this line so that custom exception types are skipped during obfuscation:
-keep public class * extends java.lang.Exception

#Material library
-keep class com.rey.material.** { *; }
-dontwarn com.rey.material.**

-keep class com.jafir.kotpref.encrypt.support.** { *; }

## Android architecture components: Lifecycle
-keepclassmembers,allowshrinking,allowobfuscation class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}
# LifecycleObserver's empty constructor is considered to be unused by proguard
-keepclassmembers class * implements androidx.lifecycle.LifecycleObserver {
    <init>(...);
}
# ViewModel's empty constructor is considered to be unused by proguard
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

-keep class android.app.Application{ *; }