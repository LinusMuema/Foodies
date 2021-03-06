package com.moose.foodies.features.feature_home.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moose.foodies.features.feature_home.data.HomeRepository
import com.moose.foodies.features.feature_home.domain.HomeData
import com.moose.foodies.util.Result
import com.moose.foodies.util.parse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {
    private val composite = CompositeDisposable()

    private val _data = MutableLiveData<Result<HomeData>>()
    val data: LiveData<Result<HomeData>> = _data

    private val _updated = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> = _updated

    fun getRemoteData(){
        composite.add(
            repository.getRemoteData()
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        updateLocalData(it)
                        _updated.postValue(true)
                    },
                    { _data.postValue(Result.Error(it.parse())) }
                )
        )
    }

    fun getLocalData(){
        composite.add(
            repository.getLocalData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _data.postValue(Result.Success(it)) },
                    { _data.postValue(Result.Error(it.parse())) }
                )
        )
    }

    fun relaySuccess(){
        composite.add(
            repository.relaySuccess()
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { Log.d("HomeActivity", "relaySuccess: Operaton successful") }
        )
    }

    private fun updateLocalData(data: HomeData){
        composite.add(
            repository.updateLocalData(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { Log.d("HomeActivity", "updateLocalData: Opeation successful") }
        )
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }
}