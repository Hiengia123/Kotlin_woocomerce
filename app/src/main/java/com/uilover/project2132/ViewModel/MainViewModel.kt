package com.uilover.project2132.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.uilover.project2132.Model.CategoryModel
import com.uilover.project2132.Model.ItemsModel
import com.uilover.project2132.Model.SliderModel
import com.uilover.project2132.Repository.MainRepository

class MainViewModel() : ViewModel() {
    private val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        return repository.loadBanner()
    }
    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    fun loadFiltered(id: String): LiveData<MutableList<ItemsModel>> {
        return repository.loadFiltered(id)
    }

    fun loadAllItems(): LiveData<MutableList<ItemsModel>> {
        return repository.loadAllItems()
    }
}