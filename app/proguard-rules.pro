# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\android-sdk/tools/proguard/proguard-android.txt
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
-dontshrink
-keepattributes Signature
-keepattributes *Annotation*

-keep class * implements android.app.Fragment
-keep class android.support.** {*;}
-keepnames class * extends android.support.v7.widget.RecyclerView.ViewHolder {
    <init>(android.view.View);
}

# for Serializable.
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# af volley
-keep class * implements cn.ieclipse.af.volley.IBaseResponse {*;}

# aorm
-keep class cn.ieclipse.aorm.annotation.** {*;}
-keep @cn.ieclipse.aorm.annotation.Table class * {
    @cn.ieclipse.aorm.annotation.* <fields>;
}