# Search Feature - Troubleshooting Fix

## Problem: Search Icon Redirected to Welcome Page

### Issue:
When user clicks the search icon, instead of opening the SearchActivity, it redirects to the IntroActivity (welcome page).

### Root Cause:
**SearchActivity was not registered in AndroidManifest.xml**

When an Activity is not registered in the manifest, Android cannot find it and the app may crash or redirect to unexpected screens.

## Solution Applied ✅

### Added SearchActivity to AndroidManifest.xml:

```xml
<activity
    android:name=".Activity.SearchActivity"
    android:exported="false" />
```

### Location in File:
```xml
<application>
    <activity
        android:name=".Activity.CartActivity"
        android:exported="false" />
    <activity
        android:name=".Activity.SearchActivity"  <!-- ← ADDED THIS -->
        android:exported="false" />
    <activity
        android:name=".Activity.DetailActivity"
        android:exported="false" />
    <!-- ...other activities... -->
</application>
```

## Why This Matters

### In Android:
Every Activity MUST be declared in AndroidManifest.xml before it can be launched.

### exported="false"
- Means this Activity can only be launched from within the app
- It's not accessible from external apps or intents
- Correct for SearchActivity (internal screen)

### exported="true"
- Used for Activities that can be launched externally
- Example: IntroActivity (app launcher)
- Example: MainActivity (can be opened from notifications, etc.)

## Verification

### Test Steps:
1. Run the app
2. Click search icon (🔍) on homepage
3. Should open SearchActivity with search bar
4. Type "áo" or "adidas"
5. Should see product results

### Expected Behavior:
```
Homepage → [Click 🔍] → SearchActivity
                           ↓
                     [Search Results]
```

### Previous Incorrect Behavior:
```
Homepage → [Click 🔍] → IntroActivity (Welcome Page) ❌
```

## Files Modified

1. **AndroidManifest.xml**
   - Added `<activity android:name=".Activity.SearchActivity" />`

## Common Activity Registration Issues

### Symptom 1: App Crashes
```
Error: Unable to find explicit activity class
```
**Fix:** Add activity to AndroidManifest.xml

### Symptom 2: Redirects to Wrong Screen
**Fix:** Check activity is registered AND intent is correct

### Symptom 3: Nothing Happens When Clicked
**Fix:** Check activity is registered AND click listener is set

## Complete AndroidManifest.xml Structure

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.Project2132">
        
        <!-- Cart Page -->
        <activity
            android:name=".Activity.CartActivity"
            android:exported="false" />
        
        <!-- Search Page (NEW) -->
        <activity
            android:name=".Activity.SearchActivity"
            android:exported="false" />
        
        <!-- Product Detail Page -->
        <activity
            android:name=".Activity.DetailActivity"
            android:exported="false" />
        
        <!-- Category Products Page -->
        <activity
            android:name=".Activity.ListItemsActivity"
            android:exported="false" />
        
        <!-- Welcome/Intro Page (App Launcher) -->
        <activity
            android:name=".Activity.IntroActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <!-- Base Activity -->
        <activity
            android:name=".Activity.BaseActivity"
            android:exported="false" />
        
        <!-- Main Homepage -->
        <activity
            android:name=".Activity.MainActivity"
            android:exported="true"
            android:label="@string/app_name" />
    </application>

</manifest>
```

## Checklist for Adding New Activities

When creating a new Activity in your app:

- [ ] Create the Activity class file
- [ ] Add to AndroidManifest.xml
- [ ] Set `exported="false"` (unless needs external access)
- [ ] Create navigation intent from other screens
- [ ] Test navigation works
- [ ] Test back button works

## Navigation Pattern

### Correct Intent Usage:

```kotlin
// In MainActivity.kt
val intent = Intent(context, SearchActivity::class.java)
context.startActivity(intent)
```

### This requires SearchActivity to be registered in manifest!

## Testing After Fix

### Manual Test:
1. Clean and rebuild project
2. Install app on device/emulator
3. Open app → Homepage
4. Click search icon
5. ✅ Should open SearchActivity (not IntroActivity)

### Automated Test (Optional):
```kotlin
@Test
fun searchIconNavigatesToSearchActivity() {
    onView(withId(R.id.search_icon)).perform(click())
    intended(hasComponent(SearchActivity::class.java.name))
}
```

## Status

- ✅ **Fixed:** SearchActivity added to AndroidManifest.xml
- ✅ **Tested:** Navigation works correctly
- ✅ **Documented:** Quick start guide updated

---

**Issue:** Search redirected to welcome page  
**Root Cause:** Missing manifest registration  
**Fix:** Added SearchActivity to AndroidManifest.xml  
**Status:** ✅ Resolved  
**Date:** December 27, 2024

