package com.geohunt.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel<I : MviIntent, S : MviState, E : MviEffect>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()


    fun onIntent(intent: I) {
        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    protected abstract suspend fun handleIntent(intent: I)

    protected fun updateState(reducer: S.() -> S) {
        _state.update { it.reducer() }
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    // ✅ Launch dengan Result handling
    protected fun <T> launchWithResult(
        showLoading: Boolean = true,
        request: suspend () -> Result<T>,
        onSuccess: (T) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                if (showLoading) onShowLoading()

                request()
                    .onSuccess { data ->
                        if (showLoading) onHideLoading()
                        onSuccess(data)
                    }
                    .onFailure { e ->
                        if (showLoading) onHideLoading()
                        onError?.invoke(e) ?: onHandleError(e)
                    }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                if (showLoading) onHideLoading()
                onError?.invoke(e) ?: onHandleError(e)
            }
        }
    }

    // ✅ Launch multiple Result sekaligus
    protected fun launchWithResults(
        showLoading: Boolean = true,
        onError: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (showLoading) onShowLoading()
                block()
                if (showLoading) onHideLoading()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                if (showLoading) onHideLoading()
                onError?.invoke(e) ?: onHandleError(e)
            }
        }
    }

    protected fun <T> launchWithFlow(
        showLoading: Boolean = true,
        request: () -> Flow<Result<T>>,
        onSuccess: (T) -> Unit,
        onLoading: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                if (showLoading) {
                    onLoading?.invoke() ?: onShowLoading()
                }

                request().collect { result ->
                    result
                        .onSuccess { data ->
                            if (showLoading) onHideLoading()
                            onSuccess(data)
                        }
                        .onFailure { e ->
                            if (showLoading) onHideLoading()
                            onError?.invoke(e) ?: onHandleError(e)
                        }
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                if (showLoading) onHideLoading()
                onError?.invoke(e) ?: onHandleError(e)
            }
        }
    }

    // Override untuk set loading di state masing-masing
    protected open fun onShowLoading() {}
    protected open fun onHideLoading() {}

    // Override untuk custom error handling per ViewModel
    protected open fun onHandleError(error: Throwable) {
        val message = when (error) {
            is IOException -> "Tidak ada koneksi internet"
            is HttpException -> "Server error: ${error.code()}"
            else -> error.message ?: "Terjadi kesalahan"
        }
        onHandleErrorMessage(message)
    }

    // Override untuk handle pesan error
    protected open fun onHandleErrorMessage(message: String) {}
}