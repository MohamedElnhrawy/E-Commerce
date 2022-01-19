package com.gtera.ui.profile.language

import androidx.databinding.ObservableBoolean
import com.gtera.base.BaseViewModel
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.splash.SplashActivity
import com.gtera.utils.APPConstants.APP_ARABIC_LANGUAGE
import com.gtera.utils.APPConstants.APP_ENGLISH_LANGUAGE
import java.util.*
import javax.inject.Inject

class LanguageViewModel @Inject constructor() : BaseViewModel<LanguageNavigator>(),
    ViewHolderInterface {

    var isLangAr: ObservableBoolean = ObservableBoolean(false)
    var isEnabled = ObservableBoolean(false)
    var oldLang  = ""

    override fun onViewCreated() {
        super.onViewCreated()
        isLangAr.set(
            Objects.equals(appRepository.getAppLanguage(), APP_ARABIC_LANGUAGE)
        )
        oldLang = appRepository.getAppLanguage().toString()
    }

    override fun onViewRecreated() {
        super.onViewRecreated()

    }

    override fun onViewClicked(position: Int, id: Int) {
        TODO("Not yet implemented")
    }


    fun onArabicLanguageSelection() {
//        appRepository.changeLanguage(APP_ARABIC_LANGUAGE)
        isLangAr.set(true)
        if (isTheSameLang())
            isEnabled.set(true)
        else
            isEnabled.set(false)
    }

    fun onEnglishLanguageSelection() {
//        appRepository.changeLanguage(APP_ENGLISH_LANGUAGE)
        isLangAr.set(false)
        if (isTheSameLang())
            isEnabled.set(true)
        else
            isEnabled.set(false)
    }

    fun onSaveButtonClick() {

        if (isTheSameLang())
           onLanguageChange(if (isLangAr.get()) APP_ARABIC_LANGUAGE else APP_ENGLISH_LANGUAGE)
    }

    private fun onLanguageChange(langKey: String) {
        appRepository.changeLanguage(context, langKey)
        openNewActivity(SplashActivity::class.java, null)
    }

    fun isTheSameLang():Boolean{
        return  (!oldLang.equals(if (isLangAr.get()) APP_ARABIC_LANGUAGE else APP_ENGLISH_LANGUAGE))
    }
}