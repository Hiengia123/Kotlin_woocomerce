# Price Filter Feature - Brand Product Pages

## Date: December 27, 2024

## Overview
Added "Filter by Price" functionality to each brand/category page (Adidas, Nike, Puma, Gucci) - just like Shopee and Lazada. Users can now filter products by price range within each brand.

## User Experience

### Brand Page with Price Filter:
```
┌─────────────────────────────────────┐
│   ← Adidas                          │
│                                     │
│  🔍 [Tìm kiếm sản phẩm Adidas...]  │
│                                     │
│  [Dưới 500K] [500K-1TR] [1TR-2TR]  │ ← Price filter chips
│  [2TR-5TR] [Trên 5TR]              │
│                                     │
│  Tìm thấy 3 sản phẩm    [Xóa bộ lọc]│
│                                     │
│  [Filtered Products Grid]           │
│                                     │
└─────────────────────────────────────┘
```

## Price Ranges (Based on Your Database)

### Analyzed Product Prices:
- Cheapest: 550,000₫ (Adidas quần short)
- Most expensive: 11,200,000₫ (Gucci áo polo)
- Common range: 650,000₫ - 5,200,000₫

### Price Filter Ranges:
1. **Dưới 500K** (0₫ - 500,000₫)
   - Filters: Very budget items (none in current database)

2. **500K - 1TR** (500,000₫ - 1,000,000₫)
   - Filters: Adidas shorts, Puma áo, Nike shorts
   - Example: "Adidas Quần Short Trắng" (550,000₫)

3. **1TR - 2TR** (1,000,000₫ - 2,000,000₫)
   - Filters: Most Adidas/Nike products
   - Example: "Adidas Áo phông Phong Cách" (1,200,000₫)

4. **2TR - 5TR** (2,000,000₫ - 5,000,000₫)
   - Filters: Premium items
   - Example: "Adidas Ao khoác gió" (5,200,000₫)

5. **Trên 5TR** (5,000,000₫ - ∞)
   - Filters: Luxury items
   - Example: "Gucci Áo Polo Nâu" (11,200,000₫)

## Features Implemented

### 1. Filter Chips (Horizontal Scroll)
```kotlin
LazyRow {
    [Dưới 500K] [500K - 1TR] [1TR - 2TR] [2TR - 5TR] [Trên 5TR]
}
```

### 2. Chip States
**Unselected:**
- Background: Light gray (#F5F5F5)
- Text: Black
- Tap to select

**Selected:**
- Background: Dark brown (brand color)
- Text: White
- Tap to deselect

### 3. Combined Filtering
```
Search Query + Price Filter = Final Results
```

Example:
- Search: "áo"
- Price: "500K - 1TR"
- Result: Only Adidas shirts priced 500K-1TR

### 4. Clear All Filters Button
```
Tìm thấy 3 sản phẩm    [Xóa bộ lọc]
```
- Appears when any filter is active
- Clears both search and price filter
- One-click reset

## Code Implementation

### Data Class:
```kotlin
data class PriceRange(
    val label: String,      // "500K - 1TR"
    val min: Double,        // 500000.0
    val max: Double         // 1000000.0
)
```

### Price Ranges:
```kotlin
val priceRanges = listOf(
    PriceRange("Dưới 500K", 0.0, 500000.0),
    PriceRange("500K - 1TR", 500000.0, 1000000.0),
    PriceRange("1TR - 2TR", 1000000.0, 2000000.0),
    PriceRange("2TR - 5TR", 2000000.0, 5000000.0),
    PriceRange("Trên 5TR", 5000000.0, Double.MAX_VALUE)
)
```

### Filtering Logic:
```kotlin
// Step 1: Apply search filter
val searchFilteredItems = SearchHelper.searchProducts(items, searchQuery)

// Step 2: Apply price filter on search results
val filteredItems = if (selectedPriceFilter == null) {
    searchFilteredItems
} else {
    searchFilteredItems.filter { item ->
        item.price >= selectedPriceFilter.min && 
        item.price <= selectedPriceFilter.max
    }
}
```

### UI Component:
```kotlin
@Composable
fun PriceFilterChips(
    selectedPriceFilter: PriceRange?,
    onPriceFilterSelected: (PriceRange?) -> Unit
) {
    LazyRow(horizontalArrangement = spacedBy(8.dp)) {
        items(priceRanges.size) { index ->
            FilterChip(
                selected = selectedPriceFilter == range,
                onClick = { /* Toggle selection */ },
                label = { Text(range.label) },
                colors = FilterChipDefaults.filterChipColors(...)
            )
        }
    }
}
```

## User Flows

### Flow 1: Filter by Price Only
```
Adidas Page
   ↓
Tap "500K - 1TR" chip
   ↓
Shows: "Tìm thấy 2 sản phẩm"
   ↓
See only Adidas products 500K-1TR
```

### Flow 2: Search + Price Filter
```
Nike Page
   ↓
Type "áo" in search
   ↓
Shows: All Nike shirts
   ↓
Tap "1TR - 2TR" chip
   ↓
Shows: Only Nike shirts priced 1TR-2TR
```

### Flow 3: Clear Filters
```
Puma Page (filtered)
   ↓
"Tìm thấy 1 sản phẩm  [Xóa bộ lọc]"
   ↓
Tap "Xóa bộ lọc"
   ↓
All filters cleared, shows all Puma products
```

### Flow 4: Deselect Price Chip
```
Adidas Page
   ↓
"500K - 1TR" chip selected (dark brown)
   ↓
Tap same chip again
   ↓
Chip deselected, shows all products
```

## Examples by Brand

### Adidas Products:
| Product | Price | Matched Filters |
|---------|-------|----------------|
| Quần short trắng | 550,000₫ | 500K-1TR |
| Quần đen phong cách | 650,000₫ | 500K-1TR |
| Áo phông đen | 980,000₫ | 500K-1TR |
| Áo polo trắng | 1,150,000₫ | 1TR-2TR |
| Áo phông phong cách | 1,200,000₫ | 1TR-2TR |
| Ao khoác gió | 5,200,000₫ | Trên 5TR |

### Gucci Products:
| Product | Price | Matched Filters |
|---------|-------|----------------|
| Áo phông đen | 8,500,000₫ | Trên 5TR |
| Áo polo nâu | 11,200,000₫ | Trên 5TR |

## UI States

### State 1: No Filters
```
┌─────────────────────────────────┐
│  [Dưới 500K] [500K-1TR] ...    │ ← All chips gray
│                                 │
│  [All Products Grid]            │
└─────────────────────────────────┘
```

### State 2: Price Filter Active
```
┌─────────────────────────────────┐
│  [Dưới 500K] [500K-1TR] ...    │ ← One chip dark brown
│                                 │
│  Tìm thấy 3 sản phẩm  [Xóa bộ lọc]│
│  [Filtered Products]            │
└─────────────────────────────────┘
```

### State 3: Search + Price Filter
```
┌─────────────────────────────────┐
│  🔍 [áo]                     ✕  │
│  [Dưới 500K] [500K-1TR] ...    │ ← One chip selected
│                                 │
│  Tìm thấy 1 sản phẩm  [Xóa bộ lọc]│
│  [Highly Filtered Products]     │
└─────────────────────────────────┘
```

### State 4: No Results
```
┌─────────────────────────────────┐
│  🔍 [áo]                     ✕  │
│  [Dưới 500K] [500K-1TR] ...    │
│                                 │
│  Tìm thấy 0 sản phẩm  [Xóa bộ lọc]│
│                                 │
│         😕                      │
│  Không tìm thấy sản phẩm       │
│  Thử từ khóa khác trong Adidas │
└─────────────────────────────────┘
```

## Comparison with Shopee/Lazada

### Shopee Price Filters:
```
[Dưới 50.000] [50.000-200.000] [200.000-500.000]
[500.000-1TR] [Trên 1TR]
```

### Our Implementation:
```
[Dưới 500K] [500K-1TR] [1TR-2TR] [2TR-5TR] [Trên 5TR]
```

**Differences:**
- ✅ Our ranges match fashion product prices (higher)
- ✅ More granular for luxury items (Gucci products 5TR+)
- ✅ Optimized for Vietnamese dong formatting

**Similarities:**
- ✅ Horizontal scrollable chips
- ✅ Toggle selection (tap to select/deselect)
- ✅ Visual feedback (color change when selected)
- ✅ Clear all filters button
- ✅ Shows result count

## Technical Details

### Filter Priority:
1. **Brand Filter** (pre-applied from category selection)
2. **Search Filter** (applied first)
3. **Price Filter** (applied on search results)

### Performance:
- All filtering is client-side (instant)
- Uses `remember()` to cache results
- Only re-filters when dependencies change
- No network calls during filtering

### State Management:
```kotlin
var searchQuery by remember { mutableStateOf("") }
var selectedPriceFilter by remember { mutableStateOf<PriceRange?>(null) }
```

## Files Modified

1. **ListItemsActivity.kt**
   - Added `PriceRange` data class
   - Added `PriceFilterChips` composable
   - Added price filter state
   - Added dual filtering logic (search + price)
   - Added "Clear all filters" button
   - Updated result count and no-results conditions

## Vietnamese Formatting

### Price Labels:
- K = Nghìn (Thousand)
- TR = Triệu (Million)

### Examples:
- 500K = 500,000₫
- 1TR = 1,000,000₫
- 2TR = 2,000,000₫
- 5TR = 5,000,000₫

## Future Enhancements

### Possible Improvements:
1. **Custom Range** - Let users input min/max price
2. **Sort Options** - Price low-to-high, high-to-low
3. **Multi-select** - Select multiple price ranges
4. **Save Preferences** - Remember user's preferred range
5. **More Filters** - Size, color, rating filters

## Testing Scenarios

### Test Case 1: Basic Price Filter
1. Open Adidas page
2. Tap "500K - 1TR" chip
3. ✅ Should show only products in that range
4. ✅ Result count should update

### Test Case 2: Combined Filters
1. Open Nike page
2. Type "áo" in search
3. Tap "1TR - 2TR"
4. ✅ Should show only Nike shirts in 1TR-2TR range

### Test Case 3: Deselect Filter
1. Open Puma page
2. Select "500K - 1TR"
3. Tap same chip again
4. ✅ Filter should clear, show all products

### Test Case 4: Clear All Button
1. Apply search + price filter
2. Tap "Xóa bộ lọc"
3. ✅ Both filters should clear
4. ✅ Show all products

### Test Case 5: No Results
1. Open Gucci page
2. Select "Dưới 500K"
3. ✅ Should show no results (Gucci has no cheap items)
4. ✅ Show helpful message

## Benefits

### For Users:
1. ✅ **Find budget items** - Easy to filter by price
2. ✅ **Compare prices** - See products in same range
3. ✅ **Save time** - No need to scroll through all products
4. ✅ **Clear interface** - Visual chips are intuitive

### For Business:
1. ✅ **Better UX** - Matches competitor apps (Shopee/Lazada)
2. ✅ **Professional** - Modern e-commerce standard
3. ✅ **Conversion** - Users find products faster
4. ✅ **Competitive** - Feature parity with market leaders

## Summary

### ✅ Implemented:
1. 5 price range filters (Dưới 500K to Trên 5TR)
2. FilterChip UI with Material Design 3
3. Combined search + price filtering
4. Clear all filters button
5. Result count display
6. No-results state
7. Toggle selection (tap to select/deselect)

### 🎯 Result:
Each brand page now has price filtering just like Shopee and Lazada! Users can:
- Filter by price range with visual chips
- Combine with search for precise results
- Clear all filters with one tap
- See instant results with smooth UX

### 📱 User Impact:
"I can now easily find Adidas products in my budget (500K-1TR) without scrolling through expensive items!"

---

**Feature:** Price Filter on Brand Product Pages  
**Status:** ✅ Complete and Tested  
**Pattern:** Client-Side Filtering with Material3 FilterChips  
**Inspired by:** Shopee & Lazada  
**Date:** December 27, 2024

