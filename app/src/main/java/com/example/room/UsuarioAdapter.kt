package com.example.room
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imc.model.Usuario

class UsuarioAdapter(
    private val context: Context,
    private var usuarios: List<Usuario>,
    private val onEditClick: (Usuario) -> Unit,
    private val onDeleteClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    // Atualizar dados do adapter
    fun updateData(newUsuarios: List<Usuario>) {
        usuarios = newUsuarios
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.bind(usuario)
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.text_nome)
        private val alturaTextView: TextView = itemView.findViewById(R.id.text_altura)
        private val pesoTextView: TextView = itemView.findViewById(R.id.text_peso)
        private val editButton: Button = itemView.findViewById(R.id.button_edit)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)

        fun bind(usuario: Usuario) {
            nomeTextView.text = usuario.nome
            alturaTextView.text = "Altura: ${usuario.altura}"
            pesoTextView.text = "Peso: ${usuario.peso}"

            // Configurar ações de clique
            editButton.setOnClickListener { onEditClick(usuario) }
            deleteButton.setOnClickListener { onDeleteClick(usuario) }
        }
    }
}

