package com.sampletest.shared.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampletest.shared.model.Quote
import com.sampletest.shared.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class QuoteListViewModel(
    private val repository: QuoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadQuotes()
    }

    private fun loadQuotes() {
        viewModelScope.launch {
            repository.getQuotes().collect { result ->
                result.fold(
                    onSuccess = { quotes ->
                        _uiState.value = UiState.Success(quotes)
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(error.message ?: "Unknown error")
                    }
                )
            }
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val quotes: List<Quote>) : UiState()
        data class Error(val message: String) : UiState()
    }
}