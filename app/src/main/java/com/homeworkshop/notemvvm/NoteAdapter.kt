package com.homeworkshop.notemvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.homeworkshop.notemvvm.NoteAdapter.NoteHolder

class NoteAdapter : ListAdapter<Note, NoteHolder>(DIFF_CALLBACK) {
    private var listener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val item_view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(item_view)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = getItem(position)
        holder.textViewTitle.text = currentNote!!.title
        holder.textViewDescription.text = currentNote.description
        holder.textViewPriority.text = currentNote.priority.toString()
    }

    //zwraca notatkę na danej pozycji - potrzebne np do usuwania danej notatki
    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView
        val textViewDescription: TextView
        val textViewPriority: TextView

        init {
            textViewTitle = itemView.findViewById(R.id.text_view_title)
            textViewDescription = itemView.findViewById(R.id.text_view_description)
            textViewPriority = itemView.findViewById(R.id.text_view_priority)
            //ustawiamy zdarzenie onClick dla całej ramki notatki
            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(getItem(position))
                }
            }
        }
    }

    //Aby mieć zdarzenie onClick na całej notatce potrzebujemy interface
    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    //metoda ustawiajaca referencje do OnItemClickListenera
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    companion object {
        // obiekt ktory porównuje elementy z nowej listy z elementami ze starej listy
        // przekazujemy go do superklass w konstruktorze
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
            //metoda zwraca true jeżeli elementy są takie same. Nie znaczy że zawartość jest taka sama ale że to są te same obiekty w naszej liscie
            // dlatego porównujemy je po id. Wiemy że wtedy to jest to samo Entry z listy , nawet z różna zawartościa
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            //zwraca true jeżeli nic się nie zmieniło w zawartości elementu
            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.priority == newItem.priority
            }


        }
    }
}