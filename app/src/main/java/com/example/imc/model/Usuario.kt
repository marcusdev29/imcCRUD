package com.example.imc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabelaUsuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val altura: Double,
    val peso: Double
)
