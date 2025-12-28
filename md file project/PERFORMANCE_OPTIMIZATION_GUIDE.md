# Performance Optimization Guide

## Emulator & App Performance Issues - Solutions

**Last Updated:** December 26, 2025  
**Project:** Kotlin E-Commerce Application

---

## Table of Contents
1. [Gradle Build Optimizations](#gradle-build-optimizations)
2. [Emulator Configuration](#emulator-configuration)
3. [App Startup Optimizations](#app-startup-optimizations)
4. [Android Studio Settings](#android-studio-settings)
5. [Hardware Recommendations](#hardware-recommendations)
6. [Troubleshooting](#troubleshooting)

---

## Gradle Build Optimizations

### ✅ Already Applied (Automatic)

The following optimizations have been automatically applied to your project:

#### **gradle.properties**
```properties
# Increased memory allocation
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8 -XX:+UseParallelGC

# Enable parallel builds
org.gradle.parallel=true

# Enable Gradle caching
org.gradle.caching=true

# Enable configuration on demand
org.gradle.configureondemand=true

# Kotlin incremental compilation
kotlin.incremental=true

# Kotlin daemon optimization
kotlin.daemon.jvmargs=-Xmx2048m

# R8 full mode
android.enableR8.fullMode=true
```

#### **app/build.gradle.kts**
- Added debug build type optimizations
- Added packaging options
- Added DEX optimizations

### 📋 Manual Steps Required

After these automatic changes, you should:

1. **Clean and Rebuild Project**
   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

2. **Invalidate Caches**
   ```
   File → Invalidate Caches → Invalidate and Restart
   ```

---

## Emulator Configuration

### 🚀 Optimal Emulator Settings

#### **Creating a Faster Emulator**

1. **Open AVD Manager** (Tools → Device Manager)

2. **Create New Virtual Device** with these settings:
   - **Device:** Pixel 5 or Pixel 6 (smaller screen = faster)
   - **System Image:** 
     - ✅ **RECOMMENDED:** Android 13 (API 33) x86_64 with Google APIs
     - ⚠️ **AVOID:** ARM images (slower on x86 systems)
   
3. **Advanced Settings:**
   ```
   RAM: 2048 MB (2GB) - Don't allocate more than 50% of your laptop RAM
   VM Heap: 512 MB
   Internal Storage: 2048 MB
   SD Card: Studio-managed, 512 MB
   
   Graphics: Automatic (or Hardware - GLES 2.0)
   Boot option: Cold boot
   
   Multi-Core CPU: 4 cores (if your laptop has 8+ cores)
                   2 cores (if your laptop has 4-6 cores)
   ```

4. **Enable Hardware Acceleration:**
   - Make sure **Intel HAXM** (Intel) or **AMD Hypervisor** (AMD) is installed
   - Check: SDK Manager → SDK Tools → Intel x86 Emulator Accelerator (HAXM installer)

### ⚡ Speed Up Existing Emulator

Edit your existing emulator:
1. AVD Manager → Click ✏️ (Edit) on your emulator
2. Show Advanced Settings
3. Apply settings above

### 💡 Emulator Quick Tips

```
✅ DO:
- Use x86_64 images (not ARM)
- Keep emulator running (don't close it)
- Use "Quick Boot" after first launch
- Disable animations in emulator (see below)

❌ DON'T:
- Create multiple emulators with high RAM
- Use ARM images on Intel/AMD CPUs
- Run multiple emulators simultaneously
- Use very high resolution devices
```

### 🎬 Disable Animations in Emulator

Open emulator → Settings → About Phone → tap "Build number" 7 times → Back → Developer Options:
```
Window animation scale: Animation off
Transition animation scale: Animation off
Animator duration scale: Animation off
```

---

## App Startup Optimizations

### 🔧 Code-Level Optimizations

#### **1. Optimize Firebase Initialization**

Check your `Application` class or `MainActivity`:

```kotlin
// ❌ BAD: Synchronous loading
override fun onCreate() {
    super.onCreate()
    Firebase.initialize(this) // Blocks UI thread
}

// ✅ GOOD: Async loading
override fun onCreate() {
    super.onCreate()
    lifecycleScope.launch(Dispatchers.IO) {
        Firebase.initialize(this@MainActivity)
    }
}
```

#### **2. Lazy Load ViewModels**

```kotlin
// ✅ Use lazy initialization
private val viewModel: MainViewModel by viewModels()
```

#### **3. Optimize Image Loading**

In your Compose code:
```kotlin
// Add placeholder and error handling
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .memoryCachePolicy(CachePolicy.ENABLED) // Cache in memory
        .diskCachePolicy(CachePolicy.ENABLED)   // Cache on disk
        .build(),
    contentDescription = null,
    placeholder = painterResource(R.drawable.placeholder),
    error = painterResource(R.drawable.error_image)
)
```

#### **4. Enable R class Optimization**

Already configured in `gradle.properties`:
```properties
android.nonTransitiveRClass=true
```

---

## Android Studio Settings

### ⚙️ Optimize Android Studio Performance

#### **1. Increase Android Studio Memory**

**Windows:**
1. Help → Edit Custom VM Options
2. Add/modify these lines:
```
-Xms2048m
-Xmx4096m
-XX:ReservedCodeCacheSize=1024m
-XX:+UseG1GC
-XX:SoftRefLRUPolicyMSPerMB=50
-XX:CICompilerCount=2
-XX:+HeapDumpOnOutOfMemoryError
-Dsun.io.useCanonPrefixCache=false
-Djava.net.preferIPv4Stack=true
-Dfile.encoding=UTF-8
```

#### **2. Disable Unnecessary Plugins**

File → Settings → Plugins → Disable:
- Android Games
- App Links Assistant
- CVS, Subversion (if not using)
- Copyright
- Markdown (if not using)

#### **3. Optimize Inspections**

File → Settings → Editor → Inspections:
- Uncheck inspections you don't need
- Consider disabling some Kotlin inspections for faster editing

#### **4. Power Save Mode (When Not Coding)**

File → Power Save Mode (Enable when just running app, disable when coding)

---

## Hardware Recommendations

### 💻 Minimum Requirements for Smooth Development

```
CPU: Intel Core i5 (6th gen+) or AMD Ryzen 5
RAM: 8GB (12GB+ recommended)
Storage: SSD (MANDATORY for Android development)
Graphics: Integrated GPU is fine

⚠️ If you have less than this, consider:
   - Using a physical Android device instead of emulator
   - Using lightweight emulator (Genymotion)
   - Remote development on a cloud service
```

### 📱 Use Physical Device (FASTEST OPTION)

Instead of emulator:

1. **Enable Developer Options** on your Android phone:
   - Settings → About Phone → tap Build number 7 times

2. **Enable USB Debugging:**
   - Settings → Developer Options → USB Debugging (ON)

3. **Connect to laptop:**
   - USB cable connection
   - Or use Wireless Debugging (Android 11+)

4. **Run app:**
   - Device will appear in Android Studio
   - Click Run (much faster than emulator!)

---

## Troubleshooting

### 🐛 Common Issues & Solutions

#### **Issue 1: "Emulator is very slow"**

**Solutions:**
1. Check if HAXM/Hypervisor is installed and running
2. Reduce emulator RAM to 2GB
3. Use x86_64 system image (not ARM)
4. Close other applications
5. Disable antivirus real-time scanning on emulator folder

#### **Issue 2: "App takes long to build"**

**Solutions:**
1. Clean project: `Build → Clean Project`
2. Invalidate caches: `File → Invalidate Caches → Restart`
3. Delete `.gradle` folder in project root
4. Delete `build` folders: `gradlew clean`
5. Update Android Gradle Plugin to latest version

#### **Issue 3: "App crashes on startup"**

**Check Logcat:**
```
View → Tool Windows → Logcat
Filter: Show only selected application
Look for red error lines
```

**Common fixes:**
1. Check `google-services.json` is present
2. Verify Firebase configuration
3. Check internet permission in AndroidManifest.xml
4. Clear app data: Settings → Apps → Your App → Clear Data

#### **Issue 4: "Gradle sync fails"**

**Solutions:**
1. Check internet connection
2. Try VPN if in restricted region
3. File → Sync Project with Gradle Files
4. Delete `.gradle` folder and sync again

#### **Issue 5: "Out of memory error"**

**Solutions:**
1. Increase memory in `gradle.properties` (already done)
2. Close other applications
3. Restart Android Studio
4. Upgrade laptop RAM if possible

---

## Quick Performance Checklist

### ✅ Before Every Development Session

```
□ Close unnecessary applications
□ Ensure emulator is already running (don't restart it)
□ Use "Quick Boot" for emulator
□ Check available disk space (minimum 10GB free on SSD)
□ Ensure laptop is plugged in (not on battery)
□ Close browser tabs (especially Chrome with many tabs)
```

### ✅ Weekly Maintenance

```
□ Clean project: Build → Clean Project
□ Invalidate caches: File → Invalidate Caches
□ Clear Gradle cache: Delete .gradle folder in project root
□ Update dependencies (check for updates)
□ Clear emulator data if it's slow: AVD Manager → Wipe Data
```

---

## Performance Testing Commands

### Check Current Build Time

Run in terminal:
```powershell
# Clean build
gradlew clean

# Measure build time
gradlew assembleDebug --profile
```

Check the HTML report in: `build/reports/profile/`

### Check Emulator Performance

In emulator, run:
```
Settings → Developer Options → Profile GPU Rendering → On screen as bars
```

Green bars = good performance
Red bars = UI lag

---

## Expected Performance After Optimizations

### 🎯 Realistic Expectations

| Task | Before | After Optimization |
|------|--------|-------------------|
| Gradle Sync | 2-5 min | 30-90 sec |
| Clean Build | 3-8 min | 1-3 min |
| Incremental Build | 30-120 sec | 10-30 sec |
| Emulator Boot | 60-120 sec | 20-40 sec |
| App Install | 20-40 sec | 10-20 sec |
| App Start | 5-10 sec | 2-4 sec |

**Note:** Times vary based on laptop hardware

---

## Alternative Development Options

### If Emulator is Still Too Slow

1. **Genymotion Emulator** (Faster alternative)
   - Download: https://www.genymotion.com/
   - Free for personal use
   - Better performance than AVD

2. **Physical Device** (Best option)
   - Use your own Android phone
   - USB or Wireless debugging
   - Real-world performance testing

3. **Cloud-based Android Emulators**
   - Firebase Test Lab
   - BrowserStack
   - Sauce Labs

4. **Lightweight Emulators**
   - Bluestacks (for basic testing)
   - LDPlayer (gaming-focused but works)

---

## System Resource Monitoring

### Monitor Performance While Developing

**Windows Task Manager:**
- Press `Ctrl + Shift + Esc`
- Check:
  - CPU usage (should be < 80% when idle)
  - RAM usage (should have 2GB+ free)
  - Disk usage (should be < 10% when idle)

**If resources are maxed out:**
- Close unnecessary apps
- Restart Android Studio
- Restart emulator
- Restart laptop if needed

---

## Summary of Changes Made

### ✅ Files Modified

1. **gradle.properties**
   - Increased JVM memory to 4GB
   - Enabled parallel builds
   - Enabled Gradle caching
   - Added Kotlin optimizations

2. **app/build.gradle.kts**
   - Added debug build configuration
   - Added packaging options
   - Added DEX optimizations

### 📋 Next Steps for You

1. **Sync Gradle** (should happen automatically)
2. **Invalidate Caches**: File → Invalidate Caches → Restart
3. **Clean Project**: Build → Clean Project
4. **Rebuild Project**: Build → Rebuild Project
5. **Configure Emulator** (follow guide above)
6. **Test the app**

---

## Additional Resources

- [Android Developer Guide - Performance](https://developer.android.com/topic/performance)
- [Gradle Build Optimization](https://docs.gradle.org/current/userguide/performance.html)
- [Configure the Android Emulator](https://developer.android.com/studio/run/emulator-acceleration)
- [Improve Your Build Speed](https://developer.android.com/studio/build/optimize-your-build)

---

## Support

If you continue experiencing performance issues after applying these optimizations, consider:

1. Checking your laptop specifications
2. Upgrading to SSD if using HDD
3. Adding more RAM (16GB recommended)
4. Using a physical Android device
5. Seeking help in Android Developer communities

---

*Generated: December 26, 2025*
*Project: com.uilover.project2132*

