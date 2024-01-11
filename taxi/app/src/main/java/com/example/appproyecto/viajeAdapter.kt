package com.example.appproyecto


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appproyecto.databinding.ItemViajeBinding
import com.example.appproyecto.models.Viaje

class viajeAdapter(var viajes: List<Viaje> = emptyList()) : RecyclerView.Adapter<viajeAdapter.PetAdapterViewHolder>() {
    //crear el viewHolder
    lateinit var setOnclickListenerViajeAceptar:(Viaje) -> Unit
    lateinit var setOnclickListenerViajeCancel:(Viaje) -> Unit
    inner class PetAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: ItemViajeBinding = ItemViajeBinding.bind(itemView)

        fun bind(viaje: Viaje) {
            binding.txtCalleP.text = viaje.call_pri
            binding.txtCalleS.text = viaje.call_sec
            binding.txtBarrio.text = viaje.sec
            binding.txtRef.text = viaje.ref
            binding.btnRechazar.setOnClickListener{
                setOnclickListenerViajeCancel(viaje)
            }
            binding.btnAceptar.setOnClickListener{
                setOnclickListenerViajeAceptar(viaje)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viaje, parent, false)
        return PetAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetAdapterViewHolder, position: Int) {
        val curso = viajes[position]
        holder.bind(curso)
    }

    override fun getItemCount(): Int {
        return viajes.size
    }
    fun updateListViajes(viajes:List<Viaje>){
        this.viajes=viajes
        notifyDataSetChanged()
    }
}