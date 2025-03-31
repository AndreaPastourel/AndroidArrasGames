package com.example.arrasgames.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arrasgames.R
import com.example.arrasgames.model.Reservation
import com.example.arrasgames.ui.activities.HistoriqueDetailActivity

class HistoriqueAdapter(
    private val reservations: List<Reservation>
) : RecyclerView.Adapter<HistoriqueAdapter.ReservationViewHolder>() {

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomTextView: TextView? = itemView.findViewById(R.id.textViewNom)
        val descriptionTextView: TextView? = itemView.findViewById(R.id.textViewDescription)
        val prixTextView: TextView? = itemView.findViewById(R.id.textViewPrix)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reservation, parent, false) // ✅ Vérifie bien ce fichier !
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]

        // ✅ Vérifier si les `TextView` existent bien avant d'y assigner des valeurs
        holder.nomTextView?.text = reservation.package_name ?: "Forfait inconnu"
        holder.descriptionTextView?.text = "Code: ${reservation.code}"
        holder.prixTextView?.text = "Statut: ${reservation.status}"

        // ✅ Gérer le clic pour voir les détails
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, HistoriqueDetailActivity::class.java)
            intent.putExtra("reservation", reservation)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = reservations.size
}
