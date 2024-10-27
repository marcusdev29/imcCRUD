package com.example.imc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imc.model.Usuario
import com.example.room.EditUsuarioActivity
import com.example.room.UsuarioAdapter
import com.example.room.databinding.ActivityTelaResultadoCalculoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class telaResultadoCalculo : AppCompatActivity() {

    private lateinit var binding: ActivityTelaResultadoCalculoBinding
    private lateinit var usuarioAdapter: UsuarioAdapter
    private val db by lazy { AppDatabase.getInstance(this) }
    private val usuarioDao by lazy { db.usuarioDao() }

    companion object {
        private const val EDIT_USER_REQUEST_CODE = 1 // Código da requisição para edição
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflando o layout
        binding = ActivityTelaResultadoCalculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar o RecyclerView com o Adapter
        usuarioAdapter = UsuarioAdapter(this, emptyList(), onEditClick = { usuario ->
            editUsuario(usuario)
        }, onDeleteClick = { usuario ->
            deleteUsuario(usuario)
        })

        binding.recyclerviewCalculos.apply {
            layoutManager = LinearLayoutManager(this@telaResultadoCalculo)
            adapter = usuarioAdapter
        }

        // Carregar os usuários registrados
        carregarUsuarios()

        // Receber o resultado do IMC da MainActivity
        val resultadoIMC = intent.getStringExtra("RESULTADO_IMC")
        binding.textViewResultado.text = resultadoIMC ?: "Erro ao calcular o IMC"

        // Botão para voltar à MainActivity
        binding.buttonVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun carregarUsuarios() {
        CoroutineScope(Dispatchers.IO).launch {
            val usuarios = usuarioDao.listarTodos()
            withContext(Dispatchers.Main) {
                usuarioAdapter.updateData(usuarios)
            }
        }
    }

    private fun editUsuario(usuario: Usuario) {
        val intent = Intent(this, EditUsuarioActivity::class.java).apply {
            putExtra("USUARIO_ID", usuario.id) // Passa o ID do usuário
        }
        startActivityForResult(intent, EDIT_USER_REQUEST_CODE) // Crie uma constante para o código da requisição
    }


    private fun deleteUsuario(usuario: Usuario) {
        CoroutineScope(Dispatchers.IO).launch {
            usuarioDao.deletar(usuario)
            carregarUsuarios()
        }
    }

    // Método para receber o resultado da edição
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_USER_REQUEST_CODE && resultCode == RESULT_OK) {
            carregarUsuarios() // Recarregar os usuários
        }
    }
}

