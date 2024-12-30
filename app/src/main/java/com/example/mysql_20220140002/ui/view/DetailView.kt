package com.example.mysql_20220140002.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mysql_20220140002.model.Mahasiswa
import com.example.mysql_20220140002.ui.navigation.DestinasiNavigasi
import com.example.mysql_20220140002.ui.viewmodel.DetailUiState
import com.example.mysql_20220140002.ui.viewmodel.DetailViewModel
import com.example.mysql_20220140002.ui.viewmodel.PenyediaViewModel

object DestinasiDetail : DestinasiNavigasi {
    override val route = "DetailMhs"
    override val titleRes = "Detail Mhs"
}

@Composable
fun DetailScreen(
    nim: String,
    onUpdate: () -> Unit,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    // Memuat data mahasiswa berdasarkan NIM
    LaunchedEffect(nim) {
        viewModel.getMhsDetail(nim)
    }

    val detailUiState = viewModel.detailUiState

    when (detailUiState) {
        is DetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DetailUiState.Success -> {
            val mahasiswa = detailUiState.mahasiswa
            DetailContent(
                mahasiswa = mahasiswa,
                onUpdate = { onUpdate },
                onDelete = {
                    viewModel.deleteMhs(nim) {
                        onBack() // Navigasi kembali setelah penghapusan
                    }
                }
            )
        }
        is DetailUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error loading detail")
            }
        }
    }
}


@Composable
fun DetailContent(
    mahasiswa: Mahasiswa,
    onUpdate: (Mahasiswa) -> Unit,
    onDelete: () -> Unit
) {
    var nama by remember { mutableStateOf(TextFieldValue(mahasiswa.nama)) }
    var kelas by remember { mutableStateOf(TextFieldValue(mahasiswa.kelas)) }
    var alamat by remember { mutableStateOf(TextFieldValue(mahasiswa.alamat)) }
    var angkatan by remember { mutableStateOf(TextFieldValue(mahasiswa.angkatan)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama") }
        )
        TextField(
            value = kelas,
            onValueChange = { kelas = it },
            label = { Text("Kelas") }
        )
        TextField(
            value = alamat,
            onValueChange = { alamat = it },
            label = { Text("Alamat") }
        )
        TextField(
            value = angkatan,
            onValueChange = { angkatan = it },
            label = { Text("Angkatan") }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                val updatedMahasiswa = mahasiswa.copy(
                    nama = nama.text,
                    kelas = kelas.text,
                    alamat = alamat.text,
                    angkatan = angkatan.text
                )
                onUpdate(updatedMahasiswa)
            }) {
                Text("Update")
            }

            Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
                Text("Delete")
            }
        }
    }
}
