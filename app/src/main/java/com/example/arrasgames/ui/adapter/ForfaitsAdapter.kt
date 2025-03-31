package com.example.arrasgames.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arrasgames.R
import com.example.arrasgames.model.Forfait
import com.example.arrasgames.ui.activities.ForfaitDetailActivity

class ForfaitsAdapter(
    private val forfaits: MutableList<Forfait>,
    private val onItemClick: (Forfait) -> Unit
) : RecyclerView.Adapter<ForfaitsAdapter.ForfaitViewHolder>() {

    inner class ForfaitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomTextView: TextView = itemView.findViewById(R.id.textViewNom)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        val prixTextView: TextView = itemView.findViewById(R.id.textViewPrix)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForfaitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forfait, parent, false)
        return ForfaitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForfaitViewHolder, position: Int) {
        val forfait = forfaits[position]
        holder.nomTextView.text = forfait.name
        holder.descriptionTextView.text = forfait.description
        holder.prixTextView.text = "${forfait.price} €"

        // Lorsque l'utilisateur clique sur un forfait
        holder.itemView.setOnClickListener {
            val intent=Intent(holder.itemView.context,ForfaitDetailActivity::class.java)
            intent.putExtra("forfait",forfait)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun updateList(newList : List<Forfait>){
        forfaits.clear()
        forfaits.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = forfaits.size
}