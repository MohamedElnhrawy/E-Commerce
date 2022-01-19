package com.gtera.di.providers

import android.content.Context
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.gtera.data.local.LocaleHelper
import com.gtera.data.local.preferences.PreferencesHelper
import dagger.internal.Preconditions
import javax.inject.Inject

/**
 * Concrete implementation of the [BaseResourceProvider] interface.
 */
class ResourceProvider @Inject constructor(context: Context) : BaseResourceProvider {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val mContext: Context = Preconditions.checkNotNull(
        context,
        "context cannot be null"
    )

    override fun getString(@StringRes id: Int): String {
        val newContext: Context = LocaleHelper.setLocale(
            mContext,
            preferencesHelper.appLanguage
        )
        return newContext.resources.getString(id)
    }


    override fun getString(@StringRes id: Int, vararg formatArgs: String?): String {
        return mContext.getString(id, *formatArgs)
    }


    override fun getDrawable(@DrawableRes id: Int): Drawable {
        return mContext.getDrawable(id)!!
    }

    fun getBoolean(@BoolRes id: Int): Boolean {
        return mContext.resources.getBoolean(id)
    }

    fun getInt(@IntegerRes id: Int): Int {
        return mContext.resources.getInteger(id)
    }

    override fun getColor(@ColorRes id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mContext.getColor(id!!)
        } else {
            TODO("VERSION.SDK_INT < M")
        }
    }

    override fun getColorFromRes(@ColorRes id: Int): Int {
        return ContextCompat.getColor(mContext, id)
    }

    override fun getAsset(): AssetManager {
        return mContext.resources.assets
    }


    override fun getDimension(@DimenRes id: Int): Float {
        return mContext.resources.getDimension(id)
    }

}