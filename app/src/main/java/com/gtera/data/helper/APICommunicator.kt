package com.gtera.data.helper

import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.*
import com.gtera.data.model.response.*
import com.gtera.data.remote.ApiInterface
import com.gtera.di.providers.ResourceProvider
import io.michaelrocks.paranoid.Obfuscate
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Obfuscate
class APICommunicator @Inject constructor(
    private val apiService: ApiInterface,
    val resourceProvider: ResourceProvider
) {

    fun getHome(
        listener: APICommunicatorListener<HomeContent?>
    ) {
        buildObservable(apiService.getHomeContent(), listener)
    }

    fun searchProducts(searchText:String,
                       listener: APICommunicatorListener<BaseResponse<ArrayList<Product?>?>?>
    ) {
        buildObservable(apiService.searchProducts(), listener)
    }



    private fun <T> buildObservable(
        observable: Single<T>?,
        listener: APICommunicatorListener<T>
    ) {
        observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(Observers.getSingleObserver(listener, resourceProvider))
    }

    private fun buildObservable(
        observable: Completable?,
        listener: APICommunicatorListener<Void?>
    ) {
        observable!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Observers.getCompletableObserver(listener, resourceProvider))
    }

}