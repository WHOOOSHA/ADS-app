package com.example.ads.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ads.repositories.SearchRepository
import com.example.ads.data.Ad
import com.example.ads.data.Group
import com.example.ads.data.ListType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()

    private val _ads = MutableStateFlow<List<Ad>>(emptyList())
    val ads: StateFlow<List<Ad>> = _ads.asStateFlow()

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    private val _expandedList = MutableStateFlow<ListType?>(null)
    val expandedList: StateFlow<ListType?> = _expandedList.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val visibleAds = combine(ads, expandedList) { adsList, expanded ->
        when (expanded) {
            ListType.ADS -> adsList
            ListType.GROUPS -> emptyList()
            else -> adsList.take(2)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val visibleGroups = combine(groups, expandedList) { groupsList, expanded ->
        when (expanded) {
            ListType.GROUPS -> groupsList
            ListType.ADS -> emptyList()
            else -> groupsList.take(5)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadInitialData()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        collapseLists()
        search(query)
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.getInitialResults()
                .collect { (initialAds, initialGroups) ->
                    _ads.value = initialAds
                    _groups.value = initialGroups
                }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            repository.search(query)
                .collect { (filteredAds, filteredGroups) ->
                    _ads.value = filteredAds
                    _groups.value = filteredGroups
                }
        }
    }

    fun loadMoreAds() = loadMore(_ads, repository::loadMoreAds)

    fun loadMoreGroups() = loadMore(_groups, repository::loadMoreGroups)

    private fun <T> loadMore(stateFlow: MutableStateFlow<List<T>>, loader: suspend (String, Int) -> Flow<List<T>>) {
        val query = _searchQuery.value
        val currentSize = stateFlow.value.size
        viewModelScope.launch {
            loader(query, currentSize).collect { newItems ->
                stateFlow.update { it + newItems }
            }
        }
    }

    fun toggleList(type: ListType) {
        _expandedList.value = if (_expandedList.value == type) null else type
    }

    fun collapseLists() {
        _expandedList.value = null
    }
}
