package com.sayildiz.spendingtrack

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayildiz.spendingtrack.model.Spend
import com.sayildiz.spendingtrack.model.SpendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthViewModel @Inject constructor(
    private val spendRepository: SpendRepository
) : ViewModel() {
    // .stateIn turns cold flow to hot flow
    // scope: provide a coroutine scope
    // started: when it should emit values
    // initial value: initial value it holds before emitting new values
    val itemsFlow: StateFlow<List<Spend>> = spendRepository.getAllSpends()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertSpend(spend: Spend) = viewModelScope.launch {
        Log.w("month screen", "Save")
        spendRepository.insertAll(spend)
    }

    fun deleteSpend(spend: Spend) = viewModelScope.launch {
        Log.w("month screen", "Delete")
        spendRepository.delete(spend)
    }


}
