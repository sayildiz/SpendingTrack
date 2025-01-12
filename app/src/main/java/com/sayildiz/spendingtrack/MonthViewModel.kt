package com.sayildiz.spendingtrack

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayildiz.spendingtrack.model.Spend
import com.sayildiz.spendingtrack.model.SpendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SpendUIState(val spends: List<Spend> = emptyList())

@HiltViewModel
class MonthViewModel @Inject constructor(
    private val spendRepository: SpendRepository
) : ViewModel() {
    private val _spendUIState = MutableStateFlow(SpendUIState())
    val spendUIState = _spendUIState

    // .stateIn turns cold flow to hot flow
    // scope: provide a coroutine scope
    // started: when it should emit values
    // initial value: intiial value it holds before emitting new values
    val itemsFlow: StateFlow<List<Spend>> = spendRepository.getAllSpends()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    init{
        viewModelScope.launch {
            spendRepository.getAllSpends()
                .collect{ spend ->
                    _spendUIState.value = SpendUIState(spend)

                }
        }
    }

    fun insertSpend(spend: Spend) = viewModelScope.launch {
        Log.w("", "Save")
        spendRepository.insertAll(spend)
    }

}
