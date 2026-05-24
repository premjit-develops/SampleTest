package com.sampletest.shared.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampletest.shared.model.FavoriteQuote
import com.sampletest.shared.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class FavoritesViewModel(
    private val repository: QuoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavoriteQuotes().collect { result ->
                result.fold(
                    onSuccess = { favorites ->
                        _uiState.value = UiState.Success(favorites ?: emptyList())
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(error.message ?: "Unknown error")
                    }
                )
            }
        }
    }

    fun deleteFavorite(quoteId: Int) {
        viewModelScope.launch {

            repository.deleteFavorite(quoteId)

        }
    }

    fun refresh() {
        loadFavorites()
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val favorites: List<FavoriteQuote>) : UiState()
        data class Error(val message: String) : UiState()
    }
}