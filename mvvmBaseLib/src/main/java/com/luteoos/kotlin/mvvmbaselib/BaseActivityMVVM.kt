package com.luteoos.kotlin.mvvmbaselib

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager

/**
 * Created by Luteoos on 13.09.2018
 */
abstract class BaseActivityMVVM<T: BaseViewModel> : AppCompatActivity() {
    lateinit var viewModel: T

    protected abstract fun getLayoutID(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard()
        setPortraitOrientation(true)
        setContentView(getLayoutID())
    }

    protected fun connectToVMMessage(){
        /**
         * invoke this after creating viewmodel to observe message and use onVMMessage
         */
        viewModel.VMMessage().observe(this, Observer { value -> onVMMessage(value) })
    }

    /**
     * override it to handle message from ViewModel
     */
    abstract fun onVMMessage(msg: String?)

    override fun onBackPressed() {
        hideKeyboard()
        super.onBackPressed()
    }

    override fun onStop() {
        viewModel.detachBus()
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.detachDisposable()
        super.onDestroy()
    }
    protected fun setPortraitOrientation(isPortrait: Boolean) {
        when(isPortrait){
            true -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            false -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    protected fun hideKeyboard(){
        if(this.currentFocus != null){
            val inputMng = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMng.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
    }

    val isNetworkOnLine: Boolean
        get(){
            val activeNetInf = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                    .activeNetworkInfo
            return activeNetInf != null && activeNetInf.isConnected
        }
}
