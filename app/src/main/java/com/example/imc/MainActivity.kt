package com.example.imc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imc.model.Usuario
import com.example.room.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(this) }
    private val usuarioDao by lazy { db.usuarioDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editNome = findViewById<EditText>(R.id.edit_nome)
        val editPeso = findViewById<EditText>(R.id.edit_peso)
        val editAltura = findViewById<EditText>(R.id.edit_altura)
        val buttonInserir = findViewById<Button>(R.id.button_inserir)

        buttonInserir.setOnClickListener {
            val nome = editNome.text.toString()
            val peso = editPeso.text.toString().toDoubleOrNull()
            val altura = editAltura.text.toString().toDoubleOrNull()

            if (nome.isNotBlank() && peso != null && altura != null) {
                // Calcular o IMC
                val imc = peso / (altura * altura)
                val resultado = when {
                    imc <= 18.5 -> "$nome, o seu IMC é %.2f e você está Abaixo do peso".format(imc)
                    imc in 18.5..24.9 -> "$nome, o seu IMC é %.2f e você está no peso Normal".format(imc)
                    imc in 25.0..29.9 -> "$nome, o seu IMC é %.2f e você está com Sobrepeso".format(imc)
                    else -> "$nome, o seu IMC é %.2f e você está Obeso".format(imc)
                }

                // Inserir o usuário no banco
                val usuario = Usuario(nome = nome, altura = altura, peso = peso)
                inserirUsuario(usuario, resultado)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inserirUsuario(usuario: Usuario, resultadoIMC: String) {
        CoroutineScope(Dispatchers.IO).launch {
            usuarioDao.inserir(listOf(usuario))

            // Redirecionar para a tela de resultado com o IMC
            withContext(Dispatchers.Main) {
                val intent = Intent(this@MainActivity, telaResultadoCalculo::class.java)
                intent.putExtra("RESULTADO_IMC", resultadoIMC)
                startActivity(intent)
            }
        }
    }

    private fun carregarDadosDoUsuario() {
        CoroutineScope(Dispatchers.IO).launch {
            val usuarios = usuarioDao.listarTodos()
            if (usuarios.isNotEmpty()) {
                val usuario = usuarios[0]  // Aqui estamos pegando o primeiro usuário como exemplo
                withContext(Dispatchers.Main) {
                    findViewById<EditText>(R.id.edit_nome).setText(usuario.nome)
                    findViewById<EditText>(R.id.edit_peso).setText(usuario.peso.toString())
                    findViewById<EditText>(R.id.edit_altura).setText(usuario.altura.toString())
                }
            }
        }
    }

}

