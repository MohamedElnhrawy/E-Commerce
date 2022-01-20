package com.gtera.data.local.db

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.gtera.data.error.ErrorDetails
import com.gtera.data.helper.Observers
import com.gtera.data.helper.Observers.getCompletableObserver
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.CartProduct
import com.gtera.data.model.User
import com.gtera.di.providers.ResourceProvider
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DataBaseCommunicator @Inject constructor(
    private val appDataBase: AppDataBase,
    val resourceProvider: ResourceProvider
) {

    fun clearData(listener: APICommunicatorListener<Void?>?) {
        Completable.fromAction {
            appDataBase.clearAllTables()
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(getCompletableObserver(listener, resourceProvider))
    }

    fun insertUser(
        user: User?,
        listener: APICommunicatorListener<Void?>?
    ) {
        Completable.fromAction {
            appDataBase.userDao().insert(user)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(getCompletableObserver(listener, resourceProvider))
    }

    fun addProductToCart(
        cartProduct: CartProduct,
        listener: APICommunicatorListener<Void?>?
    ) {
        Completable.fromAction {
            appDataBase.cartDao().insert(cartProduct)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(getCompletableObserver(listener, resourceProvider))
    }

    fun getLiveCartList(): LiveData<List<CartProduct?>?>? {
        return appDataBase.cartDao().getAll()
    }

    fun clearCart(listener: APICommunicatorListener<Void?>?) {
        Completable.fromAction {
            appDataBase.cartDao().clearCart()
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(getCompletableObserver(listener,resourceProvider))
    }

    @SuppressLint("CheckResult")
    fun getLoggedInUser(listener: APICommunicatorListener<User?>) {
        appDataBase.userDao()
            .loggedInUser
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(Observers.getSingleObserver(listener, resourceProvider));
    }

    fun deleteLoggedInUser(listener: APICommunicatorListener<Void?>) {
        getLoggedInUser(object : APICommunicatorListener<User?> {
            override fun onSuccess(result: User?) {
                Completable.fromAction {
                    appDataBase.userDao().delete(result)
                }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(getCompletableObserver(listener, resourceProvider))
            }

            override fun onError(throwable: ErrorDetails?) {
                listener.onError(throwable)
            }
        })
    }


    fun updateLoggedInUser(
        user: User?,
        listener: APICommunicatorListener<Void?>?
    ) {
        Completable.fromAction {
            appDataBase.userDao().update(user)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(getCompletableObserver(listener, resourceProvider))
    }
}