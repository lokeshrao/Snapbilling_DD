package com.snapbizz.core.utils

import android.content.Context
import android.graphics.drawable.Drawable
import javax.inject.Inject

interface ResourceProvider {
    fun getString(resId: Int): String
    fun getDrawable(resId: Int): Drawable?

}
class ResourceProviderImpl @Inject constructor(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getDrawable(resId: Int): Drawable? {
        //TD
        return null
    }
}