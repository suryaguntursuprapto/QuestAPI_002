package com.example.mysql_20220140002.repository

import com.example.mysql_20220140002.model.Mahasiswa
import com.example.mysql_20220140002.service_api.MahasiswaService
import okio.IOException

interface MahasiswaRepository{
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)

    suspend fun getMahasiswa():List<Mahasiswa>

    suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa)

    suspend fun deleteMahasiswa(nim: String)

    suspend fun getMahasiswabyNim(nim: String): Mahasiswa
}

class NetworkKontakRepository(
    private val kontakApiService: MahasiswaService
): MahasiswaRepository{
    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {
        kontakApiService.insertmahasiswa(mahasiswa)
    }

    override suspend fun getMahasiswa(): List<Mahasiswa> {
      return  kontakApiService.getAllMahasiswa()
    }

    override suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa) {
        kontakApiService.updateMahasiswa(nim,mahasiswa)
    }

    override suspend fun deleteMahasiswa(nim: String) {
        try {
            val response = kontakApiService.deleteMahasiswa(nim)
            if(response.isSuccessful){
                throw IOException("Failed to delete kontak, HTTP Status code: " +
                        "${response.code()}" )
            }else{
                response.message()
                println(response.message())
            }
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun getMahasiswabyNim(nim: String): Mahasiswa {
      return  kontakApiService.getMahasiswabyNim(nim)
    }

}