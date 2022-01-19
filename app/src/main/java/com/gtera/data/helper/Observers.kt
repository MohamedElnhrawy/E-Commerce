package com.gtera.data.helper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.room.EmptyResultSetException
import com.gtera.base.BaseFragment
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.response.APIResponse
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.authorization.login.SignInActivity
import com.gtera.ui.search.SearchFragment
import io.reactivex.CompletableObserver
import io.reactivex.FlowableSubscriber
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import org.reactivestreams.Subscription

object Observers {
    fun <T> getSingleObserver(listener: APICommunicatorListener<T>, resourceProvider: ResourceProvider): SingleObserver<T> {
        return object : SingleObserver<T> {
            override fun onSubscribe(d: Disposable) {}
            override fun onSuccess(t: T) {
                listener.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                if (e !is EmptyResultSetException) listener.onError(ErrorDetails(e, resourceProvider))
            }
        }
    }

    fun <T> getSingleObserverTest(listener: APICommunicatorListener<T>, resourceProvider: ResourceProvider): DisposableSingleObserver<T> {
        return object : DisposableSingleObserver<T>() {
            override fun onSuccess(t: T) {
                listener.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                listener.onError(ErrorDetails(e, resourceProvider))
            }
        }
    }

    fun <T> getFlowableObserver(listener: APICommunicatorListener<T>, resourceProvider: ResourceProvider): FlowableSubscriber<T> {
        return object : FlowableSubscriber<T> {
            override fun onSubscribe(s: Subscription) {
                val x = 1
            }

            override fun onNext(t: T) {
                listener.onSuccess(t)
            }

            override fun onError(t: Throwable) {
                listener.onError(ErrorDetails(t, resourceProvider))
            }

            override fun onComplete() {
                val t = 1
            }
        }
    }

    @JvmStatic
    fun getCompletableObserver(listener: APICommunicatorListener<Void?>?,
                               resourceProvider: ResourceProvider): CompletableObserver {
        return object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {}
            override fun onComplete() {
                listener?.onSuccess(null)
            }

            override fun onError(e: Throwable) {
                listener?.onError(ErrorDetails(e, resourceProvider))
            }
        }
    }
    @JvmStatic
    public val emptyListener: APICommunicatorListener<Void?>
        get() = object : APICommunicatorListener<Void?> {
            override fun onSuccess(result: Void?) {}
            override fun onError(throwable: ErrorDetails?) {}
        }

    fun <T> getLiveDataObserver(listener: APICommunicatorListener<T>, lifecycleOwner:LifecycleOwner): Observer<APIResponse<T>> {

        return Observer { response: APIResponse<T> ->
            if (response.isSuccess) listener.onSuccess(
                response.response!!
            ) else if (response.error?.statusCode == 401 && lifecycleOwner !is SearchFragment) {  // as Search fragment related cars handle this 401 differently by doing nothing
               if( lifecycleOwner is BaseFragment<*,*>){

                   (lifecycleOwner as BaseFragment<*,*>).viewModel?.openNewActivity(SignInActivity::class.java, null)
                   (lifecycleOwner as BaseFragment<*, *>).viewModel?.appRepository?.deleteUserAuthenticationData(true)
               }
            }else

                listener.onError(response.error)

        }
    }


}