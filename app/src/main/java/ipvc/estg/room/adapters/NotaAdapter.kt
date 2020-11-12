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
    context: Context, private val itemClickListener: OnItemClickListener
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
    //definição do layout que apresenta a listagem de notas
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(itemView)
    }
    //Ordenação das notas no array
    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notaItemView.text = current.nota
        holder.notaSubItemView.text=current.texto

        holder.bind(current, itemClickListener)
    }
    //GETPOS do array notas passa o parametro position
    fun getPosicaoNota(position: Int): Nota {
        return notas[position]
    }

    //aviso ao notaViewModel que os dados foram alterados para atualização da GUI
    internal fun setNotas(notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }
    //interface para o click numa nota
    interface OnItemClickListener {
        fun onItemClicked(nota: Nota)
    }
    override fun getItemCount() = notas.size //Contador do tamanho do array notas
}