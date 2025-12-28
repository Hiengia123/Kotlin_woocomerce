# SVG to PNG/JPG Migration Guide

## Date: December 26, 2025

## Overview
This document outlines the changes made to remove SVG support from the application and migrate to PNG/JPG image formats for category logos.

---

## Changes Made

### 1. Removed SVG Dependency

**File:** `app/build.gradle.kts`

**Removed:**
```kotlin
implementation("io.coil-kt:coil-svg:2.2.2") // SVG support for category logos
```

**Reason:** Since all category logos have been updated to PNG/JPG format in Firebase, the SVG decoder library is no longer needed. This reduces app size and simplifies dependencies.

---

## Database Structure Update Required

### Old Structure (SVG URLs):
```json
"categories": {
    "adidas": {
        "title": "Adidas",
        "picUrl": "https://firebasestorage.googleapis.com/.../adidas_logo.svg"
    }
}
```

### New Structure (PNG/JPG URLs):
```json
"categories": {
    "adidas": {
        "title": "Adidas",
        "picUrl": "https://firebasestorage.googleapis.com/.../adidas_logo.png"
    },
    "puma": {
        "title": "Puma",
        "picUrl": "https://firebasestorage.googleapis.com/.../puma_logo.png"
    },
    "nike": {
        "title": "Nike",
        "picUrl": "https://firebasestorage.googleapis.com/.../nike_logo.png"
    },
    "gucci": {
        "title": "Gucci",
        "picUrl": "https://firebasestorage.googleapis.com/.../gucci_logo.jpg"
    }
}
```

---

## Action Required

### Update Firebase Realtime Database

You need to update your Firebase database `categories` node with the new PNG/JPG URLs:

1. Go to Firebase Console: https://console.firebase.google.com/
2. Select project: **minshop-2972a**
3. Navigate to **Realtime Database**
4. Update the `categories` node with new image URLs (PNG/JPG instead of SVG)

### Example Update:

**Before:**
```
categories/adidas/picUrl: "https://firebasestorage.googleapis.com/v0/b/minshop-2972a/o/adidas_logo.svg?alt=media&token=8c19f3d6-dbe5-4d5a-8c6e-b0af5ba9e290"
```

**After:**
```
categories/adidas/picUrl: "https://firebasestorage.googleapis.com/v0/b/minshop-2972a/o/adidas_logo.png?alt=media&token=YOUR_NEW_TOKEN"
```

---

## How Image Loading Works Now

The app uses **Coil** library (`coil-compose:2.2.2`) which natively supports:
- ✅ PNG
- ✅ JPG/JPEG
- ✅ WebP
- ✅ GIF
- ✅ BMP

**No additional decoder needed** for these common formats.

### Code Reference:
**File:** `MainActivity.kt` - `CategoryItem` Composable

```kotlin
AsyncImage(
    model = ImageRequest.Builder(context)
        .data(item.picUrl)  // Automatically handles PNG/JPG
        .crossfade(true)
        .build(),
    contentDescription = item.title,
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Fit,
    colorFilter = if (isSelected) {
        ColorFilter.tint(Color.White)
    } else {
        ColorFilter.tint(Color.Black)
    }
)
```

---

## Troubleshooting

### If Logos Still Don't Appear:

1. **Check Firebase Database URLs:**
   - Ensure all category `picUrl` fields are updated to PNG/JPG
   - Verify URLs are accessible (click them in browser)

2. **Clear App Cache:**
   ```bash
   # Uninstall and reinstall the app
   # Or use Android Studio: Build > Clean Project > Rebuild Project
   ```

3. **Check Firebase Storage:**
   - Verify PNG/JPG files are uploaded to Firebase Storage
   - Check storage rules allow public read access

4. **Verify Image Format:**
   - Ensure files are actual PNG/JPG (not renamed SVG)
   - Check file size is reasonable (< 500KB for logos)

5. **Check Internet Connection:**
   - Coil requires internet to load remote images
   - Test on real device/emulator with working connection

---

## Benefits of PNG/JPG Over SVG

### Advantages:
- ✅ **Smaller App Size:** No SVG decoder library needed
- ✅ **Better Compatibility:** Native support in all Android versions
- ✅ **Faster Loading:** No parsing overhead
- ✅ **Color Tinting:** Works perfectly with ColorFilter

### Considerations:
- 📏 **Image Size:** PNG/JPG files may be larger than SVG
  - **Solution:** Optimize images before upload (use TinyPNG, ImageOptim)
  - **Recommended Logo Size:** 128x128px or 256x256px

---

## Next Steps

1. ✅ **Completed:** Removed SVG dependency from `build.gradle.kts`
2. ⏳ **Your Task:** Update Firebase database category URLs to PNG/JPG
3. ⏳ **Your Task:** Upload PNG/JPG logo files to Firebase Storage (if not done)
4. ⏳ **Test:** Build and run the app to verify logos appear correctly

---

## Firebase Storage Upload Guide

### If you haven't uploaded PNG/JPG logos yet:

1. **Go to Firebase Console:**
   - https://console.firebase.google.com/
   - Select project: minshop-2972a
   - Navigate to **Storage**

2. **Upload Logo Files:**
   - Click "Upload File"
   - Upload: `adidas_logo.png`, `puma_logo.png`, `nike_logo.png`, `gucci_logo.jpg`

3. **Get Download URLs:**
   - Click each uploaded file
   - Copy "Download URL"
   - Update Realtime Database with these URLs

### Example URL Format:
```
https://firebasestorage.googleapis.com/v0/b/minshop-2972a/o/adidas_logo.png?alt=media&token=XXXXXX
```

---

## Testing Checklist

After updating database:

- [ ] Build app successfully (no errors)
- [ ] Category logos appear on home screen
- [ ] Logos display correct colors (black when unselected, white when selected)
- [ ] Logos are clear and not pixelated
- [ ] All 4 category logos work (Adidas, Puma, Nike, Gucci)
- [ ] Clicking categories navigates to product list

---

## Related Files

- **Build Config:** `app/build.gradle.kts` (SVG dependency removed)
- **UI Component:** `app/src/main/java/com/uilover/project2132/Activity/MainActivity.kt` (CategoryItem)
- **Data Model:** `app/src/main/java/com/uilover/project2132/Model/CategoryModel.kt`
- **Repository:** `app/src/main/java/com/uilover/project2132/Repository/MainRepository.kt`

---

## Summary

The app is now ready to use PNG/JPG logos. The SVG support library has been removed. Once you update the Firebase database with the new PNG/JPG URLs, the logos will display correctly.

**No code changes needed in Kotlin files** - the image loading logic automatically handles PNG/JPG formats.

---

*Last Updated: December 26, 2025*

