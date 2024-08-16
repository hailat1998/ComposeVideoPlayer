package com.hd1998.composeplayer.presentation

import androidx.lifecycle.ViewModel
import com.hd1998.composeplayer.domain.repository.Repository

class MainViewModel(private val repository: Repository): ViewModel() {
    init {
        repository.enQueueWork()
    }
}