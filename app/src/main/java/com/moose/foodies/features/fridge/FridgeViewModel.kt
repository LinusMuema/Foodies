package com.moose.foodies.features.fridge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moose.foodies.models.FridgeSearch
import com.moose.foodies.network.ApiRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FridgeViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    private val composite = CompositeDisposable()
    val recipes: MutableLiveData<FridgeSearch> = MutableLiveData()
    val exception: MutableLiveData<String> = MutableLiveData()

    fun getRecipes(ingredients: String) {
        composite.add(
            apiRepository.searchFridgeRecipes(ingredients)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { recipes.value = it },
                    { exception.value = it.message}))
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }
}