# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.sumit.flurryanalytics.FlurryAnalytics {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/sumit/flurryanalytics/repack'
-flattenpackagehierarchy
-dontpreverify

# Flurry
-dontwarn com.flurry.**

-keep class com.flurry.android.agent.FlurryContentProvider