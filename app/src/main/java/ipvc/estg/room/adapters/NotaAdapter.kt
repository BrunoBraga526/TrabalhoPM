package ipvc.estg.room.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.room.R
import ipvc.estg.room.entities.Nota

class NotaAdapter internal constructor(
        context: Context, val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.textView)
        val notaSubItemView: TextView = itemView.findViewById(R.id.textview)


        fun bind(nota: Nota, clickListener: OnItemClickListener) {
            notaItemView.text = nota.nota
            notaSubItemView.text = nota.texto

            itemView.setOnClickListener {
                clickListener.onItemClicked(nota)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notaItemView.text = current.nota
        holder.notaSubItemView.text=current.texto

        holder.bind(current, itemClickListener)
    }

    fun getPosicaoNota(position: Int): Nota {
        return notas[position]
    }

    internal fun setNotas(notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(nota: Nota)
    }
    override fun getItemCount() = notas.size
}