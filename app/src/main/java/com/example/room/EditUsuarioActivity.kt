package com.example.room

import android.content.Intent
import com.example.imc.AppDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.imc.model.Usuario

import com.example.room.databinding.ActivityEditUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUsuarioBinding
    private val usuarioDao by lazy { AppDatabase.getInstance(this).usuarioDao() }
    private var usuarioId: Int = -1 // Inicialize com um valor inválido

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receber os dados do usuário que está sendo editado
        usuarioId = intent.getIntExtra("USUARIO_ID", -1)
        carregarDadosUsuario(usuarioId)

        binding.buttonSalvar.setOnClickListener {
            val nome = binding.editNome.text.toString() // Pega o nome do EditText
            val peso = binding.editPeso.text.toString().toDoubleOrNull()
            val altura = binding.editAltura.text.toString().toDoubleOrNull()

            if (peso != null && altura != null && usuarioId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val usuario = Usuario(usuarioId, nome, altura, peso) // Cria o objeto Usuario
                    usuarioDao.atualizar(usuario) // Atualiza o usuário no banco de dados
                    withContext(Dispatchers.Main) {
                        setResult(RESULT_OK) // Define o resultado da atividade
                        finish() // Fecha a Activity
                    }
                }
            } else {
                // Lidar com o caso onde peso ou altura são inválidos
                Log.e("EditUsuarioActivity", "Peso ou altura inválidos")
            }
        }
    }

    private fun carregarDadosUsuario(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val usuario = usuarioDao.buscarPorId(id) // Assumindo que você tenha esse método no seu DAO
            withContext(Dispatchers.Main) {
                if (usuario != null) {
                    binding.editNome.setText(usuario.nome)
                    binding.editPeso.setText(usuario.peso.toString())
                    binding.editAltura.setText(usuario.altura.toString())
                }
            }
        }
    }

}

