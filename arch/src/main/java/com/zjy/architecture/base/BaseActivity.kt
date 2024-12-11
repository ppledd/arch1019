package com.zjy.architecture.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.zjy.architecture.ext.setCustomDensity
import com.zjy.architecture.fragment.FragmentHandleBackUtil
import com.zjy.architecture.mvvm.Loading
import com.zjy.architecture.widget.LoadingDialog

/**
 * @author zhengjy
 * @since 2020/07/22
 * Description:
 */
abstract class BaseActivity : AppCompatActivity(), Loadable {

    var TAG = javaClass.simpleName

    @get:LayoutRes
    abstract val layoutId: Int

    //自定义加载框
    open var dialog: LoadingDialog? = null

    open fun setContentView() {
        if (layoutId != 0) {
            setContentView(layoutId)
        }
    }

    protected open fun setSystemBar() {

    }

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun setEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomDensity(this)
        setContentView()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (!isInMultiWindowMode && !isInPictureInPictureMode) {
                setSystemBar()
            }
        } else {
            setSystemBar()
        }
        initView()
        initData()
        setEvent()
    }

    fun setupLoading(loading: Loading) {
        if (loading.loading) {
            loading(loading.cancelable)
        } else {
            dismiss()
        }
    }

    override fun loading(cancelable: Boolean) {
        if (dialog == null) {
            dialog = LoadingDialog(this, cancelable)
            dialog?.setCanceledOnTouchOutside(false)
        }
        if (dialog?.isShowing == false) {
            dialog?.setCancelable(cancelable)
            dialog?.show()
        }
    }

    override fun dismiss() {
        if (!isFinishing && dialog?.isShowing == true) {
            dialog?.cancel()
        }
    }

    override fun onBackPressed() {
        if (!FragmentHandleBackUtil.handleBackPress(this)) {
            onBackPressedSupport()
        }
    }

    /**
     * 子Activity需要继承这个方法
     */
    open fun onBackPressedSupport() {
        super.onBackPressed()
    }
}

val BaseActivity.instance
    get() = this