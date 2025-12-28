package com.uilover.project2132.Helper

import java.text.Normalizer

object SearchHelper {
    /**
     * Normalize Vietnamese text by removing accents
     * Example: "Áo khoác" -> "ao khoac"
     */
    fun removeVietnameseAccents(text: String): String {
        val normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
        val pattern = "\\p{InCombiningDiacriticalMarks}+".toRegex()
        return pattern.replace(normalized, "")
            .replace("Đ", "D")
            .replace("đ", "d")
            .lowercase()
    }

    /**
     * Search products by keyword
     * Searches in: title, categoryId, categoryTitle
     * Works with Vietnamese accents, case-insensitive
     */
    fun searchProducts(
        allProducts: List<com.uilover.project2132.Model.ItemsModel>,
        keyword: String
    ): List<com.uilover.project2132.Model.ItemsModel> {
        if (keyword.isEmpty()) return allProducts

        // Normalize search keyword
        val normalizedKeyword = removeVietnameseAccents(keyword)

        return allProducts.filter { product ->
            // Normalize product fields
            val normalizedTitle = removeVietnameseAccents(product.title)
            val normalizedCategoryId = removeVietnameseAccents(product.categoryId)
            val normalizedCategoryTitle = removeVietnameseAccents(product.categoryTitle)

            // Search in multiple fields
            normalizedTitle.contains(normalizedKeyword) ||
            normalizedCategoryId.contains(normalizedKeyword) ||
            normalizedCategoryTitle.contains(normalizedKeyword)
        }
    }
}

