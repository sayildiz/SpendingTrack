package com.sayildiz.spendingtrack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayildiz.spendingtrack.model.SpendMonth
import com.sayildiz.spendingtrack.model.SpendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class YearViewModel @Inject constructor(
    private val spendRepository: SpendRepository
): ViewModel(){

    val yearItemsFlow: StateFlow<List<SpendMonth>> = spendRepository.getYear()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}