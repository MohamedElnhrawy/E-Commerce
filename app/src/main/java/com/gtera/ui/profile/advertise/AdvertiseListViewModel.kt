package com.gtera.ui.profile.advertise

import androidx.databinding.ObservableBoolean
import com.gtera.base.BaseViewModel
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.utils.APPConstants.APP_ARABIC_LANGUAGE
import java.util.*
import javax.inject.Inject

class AdvertiseListViewModel @Inject constructor() : BaseViewModel<AdvertiseListNavigator>(),
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



}