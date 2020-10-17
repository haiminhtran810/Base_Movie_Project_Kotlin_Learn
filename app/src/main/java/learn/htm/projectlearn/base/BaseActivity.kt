package learn.htm.projectlearn.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.hilt.android.AndroidEntryPoint
import learn.htm.projectlearn.BR

@AndroidEntryPoint
abstract class BaseActivity<viewDataBinding : ViewDataBinding, viewModel : BaseViewModel>() :
    AppCompatActivity() {

    protected var viewBinding: viewDataBinding? = null

    protected abstract val viewModel: viewModel

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (viewBinding == null) {
            viewBinding = DataBindingUtil.setContentView(this, layoutId)
            viewBinding?.apply {
                setVariable(BR.viewModel, viewModel)
                lifecycleOwner = this@BaseActivity
                executePendingBindings()
            }
        }
        observeEvent()
    }

    protected fun observeEvent() {}
}