package com.uilover.project2132.Helper

import android.content.Context
import android.widget.Toast
import com.uilover.project2132.Model.ItemsModel


class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertItem(item: ItemsModel) {
        var listItem = getListCart()
        // Check if item with same title, size, and color exists
        val existAlready = listItem.any {
            it.title == item.title &&
            it.selectedSize == item.selectedSize &&
            it.selectedColor == item.selectedColor
        }
        val index = listItem.indexOfFirst {
            it.title == item.title &&
            it.selectedSize == item.selectedSize &&
            it.selectedColor == item.selectedColor
        }

        if (existAlready) {
            listItem[index].numberInCart += item.numberInCart
        } else {
            listItem.add(item)
        }
        tinyDB.putListObject("CartList", listItem)
        Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listItem: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listItem[position].numberInCart == 1) {
            listItem.removeAt(position)
        } else {
            listItem[position].numberInCart--
        }
        tinyDB.putListObject("CartList", listItem)
        listener.onChanged()
    }

    fun plusItem(listItem: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listItem[position].numberInCart++
        tinyDB.putListObject("CartList", listItem)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listItem = getListCart()
        var fee = 0.0
        for (item in listItem) {
            fee += item.price * item.numberInCart
        }
        return fee
    }
}