package com.forematic.intercom.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.data.MessageDataSource
import com.forematic.intercom.ui.viewmodels.MainViewModel

class MainViewModelFactory(
    private val intercomDataSource: IntercomDataSource,
    private val messageDataSource: MessageDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(intercomDataSource, messageDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}