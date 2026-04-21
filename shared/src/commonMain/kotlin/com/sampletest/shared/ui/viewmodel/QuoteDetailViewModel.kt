package com.sampletest.shared.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampletest.shared.model.Quote
import com.sampletest.shared.model.QuoteDetail
import com.sampletest.shared.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class QuoteDetailViewModel(
    private val repository: QuoteRepository,
    @InjectedParam private val quoteId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadQuoteDetail()
    }

    private fun loadQuoteDetail() {
        viewModelScope.launch {
            repository.getQuoteDetailById(quoteId)
                .onStart { _uiState.value = UiState.Loading }
                .collect { result ->
                    result.fold(
                        onSuccess = { quote ->
                            _uiState.value = if (quote != null) {
                                UiState.Success(quote)
                            } else {
                                UiState.Error("Quote not found")
                            }
                        },
                        onFailure = { error ->
                            _uiState.value = UiState.Error(error.message ?: "Unknown error")
                        }
                    )
                }
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {

            val quote = currentState.quote
            val optimisticQuote = quote.copy(isFavorite = !quote.isFavorite)
            _uiState.value = UiState.Success(optimisticQuote)


            viewModelScope.launch {
                repository.toggleFavorite(quote.id, quote.isFavorite)
                    .onFailure {
                        _uiState.value = UiState.Success(quote)
                    }
            }
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val quote: QuoteDetail) : UiState()
        data class Error(val message: String) : UiState()
    }
}