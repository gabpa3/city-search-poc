package com.gabcode.citysearchpoc.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabcode.citysearchpoc.domain.City
import com.gabcode.citysearchpoc.domain.usecases.FetchCitiesUseCase
import com.gabcode.citysearchpoc.domain.usecases.None
import com.gabcode.citysearchpoc.domain.usecases.SearchCitiesUseCase
import com.gabcode.citysearchpoc.domain.usecases.SwitchFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchCitiesUseCase: FetchCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val switchFavoriteUseCase: SwitchFavoriteUseCase,
): ViewModel() {

    private val mQuery = MutableStateFlow("")
    val query = mQuery.asStateFlow()

    private val mSearchResult = MutableStateFlow<List<City>>(listOf())
    val searchResult = mSearchResult.asStateFlow()

    private val mCitiesLoaded = MutableStateFlow<UIState<Boolean>>(UIState.Initial)
    val citiesLoaded = mCitiesLoaded
        .onStart { fetchCities() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UIState.Initial
        )

    fun onQueryChange(newQuery: String) {
        mQuery.value = newQuery
    }

    fun toggleFavorite(city: City) {
        viewModelScope.launch {
            val favoriteUpdated = city.isFavorite.not()
            switchFavoriteUseCase.invoke(SwitchFavoriteUseCase.Params(city.id, favoriteUpdated))

            // TODO update mSearch list UI
        }
    }

    private fun fetchCities() {
        if (mCitiesLoaded.value.isSuccess) return

        mCitiesLoaded.value = UIState.Loading
        viewModelScope.launch {
            fetchCitiesUseCase.invoke(None())
                .onSuccess { available ->
                    mCitiesLoaded.value = UIState.Success(available)
                    observeQueryChanges()
                }.onFailure { e ->
                    mCitiesLoaded.value = UIState.Error(e.message ?: "Error in fetching")
                }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeQueryChanges() {
        viewModelScope.launch {
            while (isActive && citiesLoaded.value is UIState.Success<*>) {
                mQuery
                    .debounce(300)
                    .distinctUntilChanged()
                    .collectLatest { prefix ->
                        Log.i(TAG,"prefix: $prefix")
                        if (prefix.isNotBlank()) {
                            searchCitiesUseCase.invoke(SearchCitiesUseCase.Params(prefix))
                                .catch { e ->
                                    Log.e(TAG, "Error in searching query", e)
                                    // TODO do something in UI
                                }.collect { result ->
                                    Log.i("ViewModel", "search: $result")
                                    mSearchResult.value = result
                                }
                        } else {
                            mSearchResult.value = emptyList()
                        }
                    }
            }
        }
    }
}
