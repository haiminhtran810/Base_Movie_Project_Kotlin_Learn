package learn.htm.projectlearn.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import learn.htm.projectlearn.BR
import learn.htm.projectlearn.R
import learn.htm.projectlearn.extension.showDialog
import learn.htm.projectlearn.service.MusicPlayConnection
import learn.htm.projectlearn.service.MusicPlayService

abstract class BaseFragment<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> : Fragment() {

    protected lateinit var viewBinding: ViewBinding

    protected abstract val viewModel: ViewModel

    private var errorMessageDialog: AlertDialog? = null

    private var musicPlayConnection: MusicPlayConnection? = null

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            setVariable(BR.viewModel, viewModel)
            executePendingBindings()
        }
        viewBinding.lifecycleOwner = viewLifecycleOwner
        observeEvent()
    }


    protected open fun observeEvent() {
        viewModel.apply {
            /*httpUnauthorized.observe(viewLifecycleOwner, Observer {

            })*/
            unexpectedError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.unexpected_error))
            })
            httpUnavailableError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.http_unavailable_error))
            })
            rxMapperError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.rx_mapper_error))
            })
            httpForbiddenError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.http_forbidden_error))
            })
            httpGatewayTimeoutError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.no_internet_error))
            })
            errorMessage.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = it)
            })
        }
    }

    private fun handleErrorMessage(message: String) {
        if (errorMessageDialog?.isShowing != true) {
            errorMessageDialog =
                context?.showDialog(message = message, positiveMessage = getString(R.string.ok))
        }
    }

    open fun startMusicService() {
        Intent(requireContext(), MusicPlayService::class.java).let { intent ->
            requireActivity().startService(intent)
        }
    }

    open fun stopMusicService() {
        Intent(requireContext(), MusicPlayService::class.java).let { intent ->
            requireActivity().stopService(intent)
        }
    }

    open fun bindServiceMusic() {
        musicPlayConnection = MusicPlayConnection()
        Intent(requireContext(), MusicPlayService::class.java).let { intent ->
            musicPlayConnection?.let { connection ->
                requireActivity().bindService(
                    intent,
                    connection,
                    Context.BIND_AUTO_CREATE
                )
            }
        }
    }

    open fun unBindServiceMusic() {
        musicPlayConnection?.let { connection ->
            Intent(requireContext(), MusicPlayService::class.java).let {
                requireActivity().unbindService(connection)
            }
        }
    }
}