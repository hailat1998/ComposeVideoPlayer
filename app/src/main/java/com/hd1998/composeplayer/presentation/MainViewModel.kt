package com.hd1998.composeplayer.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd1998.composeplayer.domain.model.Video
import com.hd1998.composeplayer.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository, private val dispatcher: CoroutineDispatcher): ViewModel() {
    private var _videoStateFlow = MutableStateFlow(emptyList<Video>())
    val videoStateFlow get() = _videoStateFlow.asStateFlow()
    val loading = MutableStateFlow(false)
    init {
        repository.enQueueWork()
    }
    fun loadVideo(){
        loading.value = true
        viewModelScope.launch(dispatcher) {
            repository.getVideos().collect{

                _videoStateFlow.value = it
            }
        }
        loading.value = false
    }

    fun incrementPlayed(uri: Uri, newValue: Int) {
        viewModelScope.launch(dispatcher) {
            repository.incrementPlayed(uri, newValue)
        }
    }
}