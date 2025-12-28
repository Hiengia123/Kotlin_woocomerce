# Quick Reference: Size & Color Options Feature

## 🎯 What This Feature Does

Users must select **SIZE** and **COLOR** before adding products to cart.

---

## 🔍 How It Works

### On Product Detail Page:
1. User sees size options: **S, M, L, XL**
2. User sees color options: **Đỏ, Trắng, Đen, Nâu**
3. User must select both before clicking "Thêm Vào Giỏ"
4. If either is missing → Error toast appears

### In Cart:
- Each unique combination (Product + Size + Color) is a separate item
- Cart shows: **"Size: M | Màu: Đỏ"** below product name
- Each variant has independent quantity controls

---

## 💡 Examples

### Scenario 1: Different Sizes
```
Cart Item 1: Adidas Áo phông | Size: M | Màu: Đỏ (Qty: 1)
Cart Item 2: Adidas Áo phông | Size: L | Màu: Đỏ (Qty: 1)
→ Two separate items
```

### Scenario 2: Same Size, Different Color
```
Cart Item 1: Nike Áo Polo | Size: M | Màu: Trắng (Qty: 2)
Cart Item 2: Nike Áo Polo | Size: M | Màu: Đen (Qty: 1)
→ Two separate items
```

### Scenario 3: Exact Match
```
User adds: Puma Áo thun | Size: L | Màu: Đỏ
Cart already has: Puma Áo thun | Size: L | Màu: Đỏ (Qty: 2)
→ Quantity becomes 3 (not a new item)
```

---

## 🎨 UI Components Added

### 1. SizeSelector
- Location: Product detail page, after rating
- Style: Square buttons, brown when selected
- Options: S, M, L, XL

### 2. ColorSelector  
- Location: Below size selector
- Style: Circular color swatches with border
- Options: Red, White, Black, Brown
- Selected: Thick border + checkmark

### 3. Cart Badge
- Location: Cart item, below product title
- Format: "Size: M | Màu: Đỏ"
- Style: Gray rounded background

---

## 🔧 Technical Details

### Modified Files:
1. `ItemsModel.kt` - Added selectedSize, selectedColor fields
2. `ManagmentCart.kt` - Updated cart logic for variants
3. `DetailActivity.kt` - Added size/color selectors + validation
4. `CartActivity.kt` - Added option display in cart

### Data Storage:
- Stored in TinyDB (local SharedPreferences)
- No Firebase sync needed
- Persists across app restarts

---

## ✅ Testing Tips

1. **Test validation:**
   - Try adding without selecting size → Should fail ✓
   - Try adding without selecting color → Should fail ✓
   - Select both → Should work ✓

2. **Test cart uniqueness:**
   - Add same product with different sizes → 2 items ✓
   - Add same product with different colors → 2 items ✓
   - Add same product with same options → qty increases ✓

3. **Test cart display:**
   - Check if size/color badge appears ✓
   - Check if format is correct: "Size: X | Màu: Y" ✓
   - Test +/- buttons work independently ✓

---

## 🐛 Troubleshooting

**Q: Error toast not showing?**
- Check if context is available in DetailScreen
- Verify Toast import is present

**Q: Cart not separating variants?**
- Check ManagmentCart.insertItem() logic
- Verify it checks title + size + color

**Q: Options not displayed in cart?**
- Check if selectedSize and selectedColor are saved
- Verify CartItem composable has the display code

**Q: Size/Color selectors not appearing?**
- Check if SizeSelector and ColorSelector are called
- Verify they're placed in correct order

---

## 📱 User Instructions (For App Users)

**Cách chọn size và màu:**
1. Nhấn vào sản phẩm để xem chi tiết
2. Cuộn xuống xem các lựa chọn size (S, M, L, XL)
3. Nhấn vào size bạn muốn (sẽ có viền nâu khi chọn)
4. Nhấn vào màu bạn muốn (sẽ có viền đậm khi chọn)
5. Nhấn "Thêm Vào Giỏ"

**Lưu ý:**
- Phải chọn cả size VÀ màu mới thêm được vào giỏ
- Nếu thiếu, sẽ có thông báo yêu cầu chọn đầy đủ
- Mỗi tổ hợp size + màu là một sản phẩm riêng trong giỏ hàng

---

## 🚀 Future Enhancements (Optional)

- [ ] Load sizes/colors dynamically from Firebase
- [ ] Add size guide modal
- [ ] Show stock availability per variant
- [ ] Add product images per color
- [ ] Different prices for different sizes
- [ ] Add "favorite size" saving
- [ ] Size recommendation based on user history

---

**Implementation Date:** December 27, 2025  
**Status:** ✅ Complete and Working  
**Build Status:** ✅ No Errors (only minor warnings)

