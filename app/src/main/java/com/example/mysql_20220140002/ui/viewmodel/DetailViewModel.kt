package com.example.mysql_20220140002.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.mysql_20220140002.model.Mahasiswa
import com.example.mysql_20220140002.repository.MahasiswaRepository
import kotlinx.coroutines.launch
import okio.IOException

sealed class DetailUiState {
    data class Success(val mahasiswa: Mahasiswa): DetailUiState()
    object Error: DetailUiState()
    object Loading: DetailUiState()
}

class DetailViewModel(private val mhsRepository: MahasiswaRepository) : ViewModel() {
    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    fun getMhsDetail(nim: String) {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            detailUiState = try {
                val mahasiswa = mhsRepository.getMahasiswabyNim(nim)
                DetailUiState.Success(mahasiswa)
            } catch (e: IOException) {
                DetailUiState.Error
            } catch (e: HttpException) {
                DetailUiState.Error
            }
        }
    }

    fun updateMhs(nim: String, mahasiswa: Mahasiswa) {
        viewModelScope.launch {
            try {
                mhsRepository.updateMahasiswa(nim, mahasiswa)
                detailUiState = DetailUiState.Success(mahasiswa)
            } catch (e: IOException) {
                detailUiState = DetailUiState.Error
            } catch (e: HttpException) {
                detailUiState = DetailUiState.Error
            }
        }
    }

    fun deleteMhs(nim: String, onDeleteSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                mhsRepository.deleteMahasiswa(nim)
                onDeleteSuccess()
            } catch (e: IOException) {
                detailUiState = DetailUiState.Error
            } catch (e: HttpException) {
                detailUiState = DetailUiState.Error
            }
        }
    }
}
