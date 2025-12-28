# Product Options Feature (Size & Color Selection)

## Overview
This feature allows users to select **size** and **color** options when adding products to their cart. The selected options are saved with each cart item and displayed in the cart view.

## Implementation Details

### 1. Database Structure
According to the Firebase database structure provided:
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

### 2. Data Model Changes

#### ItemsModel.kt
Added two new fields to store user selections:
```kotlin
data class ItemsModel(
    // ...existing fields...
    var selectedSize: String = "",      // Selected size for cart
    var selectedColor: String = ""      // Selected color for cart
)
```

### 3. Product Detail Page (DetailActivity.kt)

#### Size Selector
- Displays available sizes: **S, M, L, XL**
- Visual design:
  - Square boxes (50x50 dp)
  - Selected: Dark brown background with white text
  - Unselected: Light gray background with black text
  - Border highlight on selection

#### Color Selector
- Displays available colors: **Đỏ (Red), Trắng (White), Đen (Black), Nâu (Brown)**
- Visual design:
  - Circular color swatches (50x50 dp)
  - Each shows the actual color
  - Selected: Thick brown border with checkmark below
  - Unselected: Light gray border

#### Validation
When user clicks "Thêm Vào Giỏ" (Add to Cart):
- Checks if both size and color are selected
- Shows error toast if either is missing: **"Vui lòng chọn kích thước và màu sắc"**
- Only adds to cart when both options are selected

### 4. Cart Management (ManagmentCart.kt)

#### Updated Logic
The cart now treats items as **unique combinations** of:
- Product title
- Selected size
- Selected color

**Example:**
- "Adidas Áo phông" + Size "M" + Color "Đỏ" ≠ "Adidas Áo phông" + Size "L" + Color "Đen"
- These are treated as separate cart items

#### Cart Operations
```kotlin
fun insertItem(item: ItemsModel) {
    // Check if exact combination exists (title + size + color)
    val existAlready = listItem.any { 
        it.title == item.title && 
        it.selectedSize == item.selectedSize && 
        it.selectedColor == item.selectedColor 
    }
    
    if (existAlready) {
        // Increase quantity of existing item
        listItem[index].numberInCart += item.numberInCart
    } else {
        // Add as new item
        listItem.add(item)
    }
}
```

### 5. Cart Display (CartActivity.kt)

#### Cart Item View
Each cart item now displays:
1. Product image
2. Product title
3. **Selected options badge** (NEW):
   - Shows "Size: M | Màu: Đỏ"
   - Gray background chip
   - Appears below product title
   - Only shown if options are selected
4. Price
5. Quantity controls

#### Visual Example
```
┌─────────────────────────────────┐
│  [Image]  Adidas Áo phông       │
│           ┌──────────────────┐   │
│           │ Size: M | Màu: Đỏ │  │
│           └──────────────────┘   │
│           1,200,000 ₫           │
│           [−] 1 [+]             │
└─────────────────────────────────┘
```

## User Flow

### Adding to Cart
1. User browses products
2. Clicks on product → Opens Detail page
3. Swipes through product images
4. **Selects size** (e.g., "M")
5. **Selects color** (e.g., "Đỏ")
6. Clicks "Thêm Vào Giỏ"
7. Toast confirmation: "Đã thêm vào giỏ hàng"

### Viewing Cart
1. User opens cart
2. Sees all items with their selected options
3. Each variant is listed separately
4. Can adjust quantity for each specific variant

### Example Scenario
User can add:
- 2x Adidas Áo phông (Size M, Màu Đỏ)
- 1x Adidas Áo phông (Size L, Màu Đen)
- 3x Adidas Áo phông (Size M, Màu Trắng)

All three will appear as **separate cart items** with their own quantity controls.

## Files Modified

1. **ItemsModel.kt**
   - Added `selectedSize` and `selectedColor` fields

2. **ManagmentCart.kt**
   - Updated `insertItem()` to check size+color combination
   - Items are unique by (title + size + color)

3. **DetailActivity.kt**
   - Added `SizeSelector` composable
   - Added `ColorSelector` composable
   - Added validation before adding to cart
   - Updated add to cart logic to include size and color

4. **CartActivity.kt**
   - Updated `CartItem` composable to display size and color options
   - Added options badge UI below product title

## Available Options

### Sizes
- **S** - Small
- **M** - Medium  
- **L** - Large
- **XL** - Extra Large

### Colors
- **Đỏ** - Red (#E53935)
- **Trắng** - White (#FFFFFF)
- **Đen** - Black (#000000)
- **Nâu** - Brown (#795548)

## Storage
- Options are stored in **TinyDB (SharedPreferences)** as part of the cart data
- Persists across app sessions
- No Firebase upload (cart is local only)

## Future Enhancements (Optional)

1. **Dynamic options from Firebase**
   - Load available sizes/colors from database
   - Support product-specific options

2. **Stock management**
   - Check size/color availability
   - Show "Out of stock" for unavailable combinations

3. **Price variations**
   - Different prices for different sizes
   - Premium colors

4. **Product images by color**
   - Show color-specific product images
   - Update main image when color is selected

5. **Size guide**
   - Add size chart modal
   - Help users choose correct size

## Testing Checklist

- ✅ Size selector displays all options
- ✅ Color selector displays all colors correctly
- ✅ Validation prevents adding without selection
- ✅ Same product with different options creates separate cart items
- ✅ Same product with same options increases quantity
- ✅ Cart displays size and color for each item
- ✅ Quantity controls work for each variant
- ✅ Cart data persists after app restart

## Screenshots Locations (To be added)
- Detail page with size/color selectors
- Cart page showing options
- Validation error message

---
**Created:** December 27, 2025  
**Status:** ✅ Implemented and Working

