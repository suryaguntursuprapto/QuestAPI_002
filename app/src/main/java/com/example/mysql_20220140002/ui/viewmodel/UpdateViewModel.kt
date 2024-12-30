package com.example.mysql_20220140002.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysql_20220140002.model.Mahasiswa
import com.example.mysql_20220140002.repository.MahasiswaRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UpdateUiEvent(
    val nama: String = "",
    val nim: String = "",
    val jenisKelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)

data class UpdateUiState(
    val updateUiEvent: UpdateUiEvent = UpdateUiEvent(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class UpdateViewModel(private val mhsRepository: MahasiswaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UpdateUiState())
    val uiState: StateFlow<UpdateUiState> = _uiState

    fun loadMahasiswa(nim: String) {
        viewModelScope.launch {
            _uiState.value = UpdateUiState(isLoading = true)
            try {
                val mahasiswa = mhsRepository.getMahasiswabyNim(nim)
                _uiState.value = UpdateUiState(
                    updateUiEvent = UpdateUiEvent(
                        nama = mahasiswa.nama,
                        nim = mahasiswa.nim,
                        jenisKelamin = mahasiswa.jenisKelamin,
                        alamat = mahasiswa.alamat,
                        kelas = mahasiswa.kelas,
                        angkatan = mahasiswa.angkatan
                    )
                )
            } catch (e: Exception) {
                _uiState.value = UpdateUiState(errorMessage = "Failed to load data.")
            }
        }
    }

    fun updateMahasiswa(nim: String) {
        val state = _uiState.value.updateUiEvent
        viewModelScope.launch {
            try {
                val mahasiswa = Mahasiswa(
                    nama = state.nama,
                    nim = state.nim,
                    jenisKelamin = state.jenisKelamin,
                    alamat = state.alamat,
                    kelas = state.kelas,
                    angkatan = state.angkatan
                )
                mhsRepository.updateMahasiswa(nim,mahasiswa)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to update data.")
            }
        }
    }

    fun updateUpdateState(event: UpdateUiEvent) {
        _uiState.value = _uiState.value.copy(updateUiEvent = event)
    }
}
