package com.gtera.di.providers

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes

/**
 * Resolves application's resources.
 */
interface BaseResourceProvider {
    /**
     * Resolves text's id to String.
     *
     * @param id to be fetched from the resources
     * @return String representation of the {@param id}
     */
    fun getString(@StringRes id: Int): String

    /**
     * Resolves text's id to String and formats it.
     *
     * @param resId      to be fetched from the resources
     * @param formatArgs format arguments
     * @return String representation of the {@param resId}
     */
    fun getString(@StringRes resId: Int, vararg formatArgs: String?): String

    /**
     * Resolves drawablw resource.
     *
     * @param drawId      to be fetched from the resources
     * @return String representation of the {@param resId}
     */
    fun getDrawable(@StringRes drawId: Int): Drawable

    /**
     * Resolves drawablw resource.
     *
     * @param colorId      to be fetched from the resources
     * @return int representation of the {@param resId}
     */
    fun getColor(@ColorInt colorId: Int): Int



    /**
     * Resolves Asset resource.
     */
    fun getAsset(): AssetManager



    /**
     * Resolves Dimension resource.
     */
    fun getDimension(@DimenRes id: Int): Float


    /**
     * Resolves drawablw resource.
     *
     * @param id      to be fetched from the resources
     * @return int representation of the {@param resId}
     */
    fun getColorFromRes(@ColorRes id: Int): Int
}