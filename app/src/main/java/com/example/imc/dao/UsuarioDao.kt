package com.example.imc.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.imc.model.Usuario
@Dao
interface UsuarioDao {
    @Query("SELECT * FROM tabelaUsuario WHERE id = :id")
    suspend fun buscarPorId(id: Int): Usuario?

    @Insert
    suspend fun inserir(usuario: List<Usuario>)

    @Update
    suspend fun atualizar(usuario: Usuario)

    @Delete
    suspend fun deletar(usuario: Usuario)

    @Query("SELECT * FROM tabelaUsuario")
    suspend fun listarTodos(): List<Usuario>
}
