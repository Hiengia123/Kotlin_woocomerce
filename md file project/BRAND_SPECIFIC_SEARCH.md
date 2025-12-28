# Brand-Specific Search Feature - Category Pages

## Date: December 27, 2024

## Overview
Added search functionality to each brand/category page (Adidas, Nike, Puma, Gucci) that filters products ONLY within that specific brand.

## User Requirement

### Example:
On the **Adidas page**:
- Search "áo" → Shows only **Adidas** shirts (not Puma, Nike, or Gucci)
- Search "quần" → Shows only **Adidas** pants
- Search "khoác" → Shows only **Adidas** jackets

On the **Nike page**:
- Search "áo" → Shows only **Nike** shirts (not Adidas, Puma, or Gucci)

## Implementation

### Before:
```
┌─────────────────────────────┐
│   ← Adidas                  │
│                             │
│  [All Adidas Products]      │
│  [No search capability]     │
│                             │
└─────────────────────────────┘
```

### After:
```
┌─────────────────────────────┐
│   ← Adidas                  │
│                             │
│  🔍 [Tìm kiếm Adidas...]  ✕ │
│  Tìm thấy 3 sản phẩm        │
│                             │
│  [Filtered Adidas Products] │
│                             │
└─────────────────────────────┘
```

## Features Implemented

### 1. Search Bar in Category Page
- Located below the page title
- Placeholder: "Tìm kiếm sản phẩm {Brand}..."
- Real-time filtering as user types
- Clear button (✕) appears when typing

### 2. Brand-Specific Filtering
```kotlin
// Only searches within items already filtered by categoryId
val items by viewModel.loadFiltered(id).observeAsState(emptyList())

// Then applies search filter on top
val filteredItems = SearchHelper.searchProducts(items, searchQuery)
```

### 3. Vietnamese Text Support
Uses the same `SearchHelper` as global search:
- Works with/without accents
- Case-insensitive
- Searches in: title, categoryId, categoryTitle

### 4. UI States

#### Empty Search (Default):
- Shows all products for that brand
- No result count shown

#### Active Search with Results:
```
Tìm thấy 3 sản phẩm
[Product Grid]
```

#### No Results:
```
😕
Không tìm thấy sản phẩm
Thử từ khóa khác trong Adidas
```

## Code Changes

### ListItemsActivity.kt

#### Added Imports:
```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.uilover.project2132.Helper.SearchHelper
```

#### Added Search State:
```kotlin
var searchQuery by remember { mutableStateOf("") }
```

#### Added Search Filtering:
```kotlin
val filteredItems = remember(searchQuery, items.size) {
    SearchHelper.searchProducts(items, searchQuery)
}
```

#### Added Search Bar UI:
```kotlin
Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp)
) {
    // Search icon
    Image(painterResource(R.drawable.search_icon))
    
    // Search input field
    BasicTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        decorationBox = { /* Placeholder */ }
    )
    
    // Clear button (✕)
    if (searchQuery.isNotEmpty()) {
        Box(clickable { searchQuery = "" }) {
            Text("✕")
        }
    }
}
```

#### Added Result Count:
```kotlin
if (searchQuery.isNotEmpty()) {
    Text("Tìm thấy ${filteredItems.size} sản phẩm")
}
```

#### Added No Results State:
```kotlin
if (filteredItems.isEmpty() && searchQuery.isNotEmpty()) {
    Column {
        Text("😕")
        Text("Không tìm thấy sản phẩm")
        Text("Thử từ khóa khác trong $title")
    }
}
```

## Search Flow

### Data Flow:
```
Firebase
   ↓
loadFiltered(categoryId)  → [All Adidas Products]
   ↓
User types "áo"
   ↓
SearchHelper.searchProducts([Adidas items], "áo")
   ↓
[Filtered Adidas Products with "áo"]
   ↓
Display in Grid
```

### Example Scenario:

**User on Adidas Page:**
```
All Adidas Products: [
  "Adidas Áo phông",
  "Adidas Quần đen", 
  "Adidas Áo khoác",
  "Adidas Quần short"
]

User types: "áo"
↓
Filtered Results: [
  "Adidas Áo phông",
  "Adidas Áo khoác"
]

Shows: "Tìm thấy 2 sản phẩm"
```

**User on Nike Page:**
```
All Nike Products: [
  "Nike Áo thể thao",
  "Nike Quần dài",
  "Nike Áo polo"
]

User types: "áo"
↓
Filtered Results: [
  "Nike Áo thể thao",
  "Nike Áo polo"
]

Shows: "Tìm thấy 2 sản phẩm"
```

**Key Point:** Searching "áo" on Adidas page NEVER shows Nike products!

## Search Capabilities

### Searches In:
1. Product title
2. Category ID (but already filtered by brand)
3. Category title (but already filtered by brand)

### Vietnamese Support Examples:

| User Types | Brand Page | Finds |
|------------|------------|-------|
| áo | Adidas | Adidas áo phông, Adidas áo khoác |
| ao | Adidas | Adidas áo phông, Adidas áo khoác (same!) |
| quần | Nike | Nike quần dài, Nike quần short |
| quan | Nike | Nike quần dài, Nike quần short (same!) |
| đen | Puma | Puma quần đen, Puma áo đen |
| den | Puma | Puma quần đen, Puma áo đen (same!) |

## UI Components

### Search Bar:
```
┌─────────────────────────────────────┐
│ 🔍  [Tìm kiếm sản phẩm Adidas...]  ✕│
└─────────────────────────────────────┘
```

#### Elements:
- **Search Icon (🔍):** Visual indicator
- **Input Field:** Light gray background (#F5F5F5)
- **Placeholder:** "Tìm kiếm sản phẩm {Brand}..."
- **Clear Button (✕):** Circular button with brand color

### Clear Button Design:
- Background: lightBrown color
- Shape: Circle (RoundedCornerShape(50))
- Icon: ✕ symbol
- Color: darkBrown
- Action: Clears search query

## Comparison: Global vs Brand Search

### Global Search (SearchActivity):
```
All Products from all brands
   ↓
Search "áo"
   ↓
Shows: Adidas áo, Nike áo, Puma áo, Gucci áo
```

### Brand Search (ListItemsActivity):
```
Only Adidas products
   ↓
Search "áo"
   ↓
Shows: ONLY Adidas áo (no other brands)
```

## User Experience

### Journey 1: Category Search
```
Homepage
   ↓
Tap "Adidas" category
   ↓
Adidas Page opens (shows all Adidas items)
   ↓
Type "áo" in search bar
   ↓
See only Adidas shirts
   ↓
Tap product → Detail page
```

### Journey 2: Clear Search
```
Adidas Page
   ↓
Type "khoác" → See jackets
   ↓
Tap ✕ button
   ↓
Search cleared → See all Adidas products again
```

### Journey 3: No Results
```
Nike Page
   ↓
Type "xyz123"
   ↓
See "Không tìm thấy sản phẩm"
   ↓
Type different keyword
```

## Performance

### Efficient Filtering:
1. **Pre-filtered by brand** - Only loads products for that category
2. **Client-side search** - Instant filtering (no network calls)
3. **Cached results** - Uses `remember()` to avoid re-filtering
4. **Real-time** - Updates as user types

### Memory:
- Adidas has ~6 products → Very lightweight
- Nike has ~4 products → Very lightweight
- No performance concerns

## Benefits

### For Users:
1. ✅ **Focused search** - Only see relevant brand products
2. ✅ **Faster results** - Smaller dataset to search
3. ✅ **Clear context** - Know they're searching within a brand
4. ✅ **Vietnamese support** - Works with/without accents

### For App:
1. ✅ **Better UX** - Users find what they want faster
2. ✅ **Consistent** - Same search experience everywhere
3. ✅ **Reusable** - Uses existing SearchHelper utility
4. ✅ **Maintainable** - Clean, simple code

## Testing Scenarios

### Test Case 1: Basic Search
1. Open Adidas page
2. Type "áo"
3. ✅ Should show only Adidas shirts
4. ✅ Should NOT show Nike/Puma/Gucci shirts

### Test Case 2: Vietnamese Accents
1. Open Nike page
2. Type "quần" → See results
3. Clear search
4. Type "quan" → See SAME results
5. ✅ Both should work identically

### Test Case 3: Clear Button
1. Open Puma page
2. Type "đen"
3. See filtered results
4. Tap ✕ button
5. ✅ Search clears, shows all Puma products

### Test Case 4: No Results
1. Open Gucci page
2. Type "xyzabc"
3. ✅ Should show "Không tìm thấy sản phẩm"
4. ✅ Should show brand name in message

### Test Case 5: Case Insensitive
1. Open any brand page
2. Type "ÁO" (uppercase)
3. ✅ Should find products with "áo" or "Áo"

## Files Modified

1. **ListItemsActivity.kt**
   - Added search bar UI
   - Added search state management
   - Added filtering logic
   - Added no-results state
   - Added result count display

## Future Enhancements

### Possible Improvements:
1. **Search history** - Remember recent searches per brand
2. **Popular searches** - Show trending keywords for each brand
3. **Filters** - Add price/rating filters within brand
4. **Sort options** - Price low-to-high, rating, etc.
5. **Voice search** - Voice input for search
6. **Suggestions** - Auto-complete as user types

## Summary

### ✅ Implemented:
1. Search bar on each category/brand page
2. Filters ONLY within that specific brand
3. Vietnamese text support (with/without accents)
4. Real-time filtering
5. Clear button to reset search
6. Result count display
7. No-results state with helpful message

### 🎯 Result:
Each brand page (Adidas, Nike, Puma, Gucci) now has its own search functionality that:
- Only searches within that brand's products
- Works with Vietnamese accents
- Provides instant results
- Shows helpful feedback

### 📱 User Impact:
Users can now easily find specific products within their favorite brand without seeing results from other brands!

---

**Feature:** Brand-Specific Search on Category Pages  
**Status:** ✅ Complete and Tested  
**Pattern:** Client-Side Filtering with Brand Scope  
**Date:** December 27, 2024

