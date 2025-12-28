# Firebase Database Field Mapping Reference

## Quick Reference: Old vs New Database Structure

### 📊 Database Nodes

| Old Node Name | New Node Name | Status |
|---------------|---------------|--------|
| `Banner` | `banners` | ✅ Updated |
| `Category` | `categories` | ✅ Updated |
| `Items` | `items` | ✅ Updated |
| N/A | `attributes` | 🆕 New (not yet used) |

---

## 🏷️ CategoryModel Fields

| Old Field | New Field | Type | Notes |
|-----------|-----------|------|-------|
| `title` | `title` | String | ✅ Same |
| `id` | ❌ Removed | Int | Removed - use key instead |
| `picUrl` | `picUrl` | String | ✅ Same |

**Firebase Structure:**
```json
"categories": {
  "adidas": {
    "title": "Adidas",
    "picUrl": "https://..."
  }
}
```

---

## 🛍️ ItemsModel Fields

| Old Field | New Field | Type | Notes |
|-----------|-----------|------|-------|
| `title` | `title` | String | ✅ Same |
| `description` | `description` | String | ✅ Same |
| `price` | `price` | Double | ✅ Same |
| `picUrl` | `image` | String | ⚠️ Changed - now single image |
| ❌ N/A | `product_gallery` | Object | 🆕 New - contains img1, img2, img3 |
| `rating` | `rated` | Double | ⚠️ Renamed |
| `showRecommended` | `showRecommend` | Boolean | ⚠️ Renamed |
| `categoryId` | `categoryId` | String | ✅ Same |
| ❌ N/A | `categoryTitle` | String | 🆕 New |
| `model` | ❌ Removed | ArrayList<String> | Removed - not in new DB |
| `numberInCart` | `numberInCart` | Int | ✅ Same (local only) |

**Firebase Structure:**
```json
"items": {
  "adidas_tshirt_01": {
    "title": "Adidas Áo phông Phong Cách",
    "price": 1200000,
    "image": "https://...",
    "product_gallery": {
      "img1": "https://...",
      "img2": "https://...",
      "img3": ""
    },
    "description": "...",
    "categoryId": "adidas",
    "categoryTitle": "Adidas",
    "showRecommend": true,
    "rated": 4.7
  }
}
```

---

## 🎨 SliderModel (Banners)

| Old Field | New Field | Type | Notes |
|-----------|-----------|------|-------|
| `url` | `url` | String | ✅ Same |

**Firebase Structure:**
```json
"banners": {
  "adidas": {
    "url": "https://..."
  }
}
```

---

## 🔍 Query Changes

### Popular Items Query
```kotlin
// OLD
ref.orderByChild("showRecommended").equalTo(true)

// NEW
ref.orderByChild("showRecommend").equalTo(true)
```

### Category Filter Query
```kotlin
// OLD & NEW - Same
ref.orderByChild("categoryId").equalTo(id)
```

---

## 💾 Code Usage Examples

### Loading Product Image
```kotlin
// OLD
AsyncImage(model = item.picUrl.firstOrNull())

// NEW
AsyncImage(model = item.image)
```

### Displaying Product Gallery
```kotlin
// NEW - Build image list from gallery
val imageList = buildList {
    add(item.image)  // Main image
    if (item.product_gallery.img1.isNotEmpty()) add(item.product_gallery.img1)
    if (item.product_gallery.img2.isNotEmpty()) add(item.product_gallery.img2)
    if (item.product_gallery.img3.isNotEmpty()) add(item.product_gallery.img3)
}
```

### Displaying Rating
```kotlin
// OLD
Text(text = item.rating.toString())

// NEW
Text(text = item.rated.toString())
```

### Category Display
```kotlin
// OLD
AsyncImage(model = category.picUrl)  // Still the same

// NEW
AsyncImage(model = category.picUrl)  // No change needed
```

---

## 🗂️ Attributes Node (Future Use)

```json
"attributes": {
  "size": {
    "S": true,
    "M": true,
    "X": true,
    "XL": true
  },
  "color": {
    "red": true,
    "white": true,
    "black": true,
    "brown": true
  }
}
```

**Implementation Plan:**
- Use for product variant selection
- Add to ItemsModel when implementing size/color selection
- Create AttributesModel class

---

## ✅ Migration Checklist

- [x] Update database node references (Banner → banners, etc.)
- [x] Update ItemsModel fields (picUrl → image, rating → rated)
- [x] Update CategoryModel (remove id field)
- [x] Update Repository queries (showRecommended → showRecommend)
- [x] Update UI components to use new field names
- [x] Remove Model/Variant selector (not in new DB)
- [x] Fix all package imports
- [x] Create TinyDB helper class
- [ ] Implement attributes selection (future)
- [ ] Add order management (future)
- [ ] Add payment integration (future)

---

**Last Updated:** December 26, 2025

