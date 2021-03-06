package me.mapyt.app.presentation.exceptions

import androidx.annotation.StringRes

data class ViewModelOperationException(@StringRes val messageId: Int) : Exception() {

}