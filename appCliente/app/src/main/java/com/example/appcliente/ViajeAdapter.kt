package com.example.appcliente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.databinding.ItemTaxiBinding
import com.example.appcliente.databinding.ItemTaxiEsperaBinding
import com.example.appcliente.models.Viaje

class ViajeAdapter(var viajes: List<Viaje> = emptyList()) : RecyclerView.Adapter<ViajeAdapter.PetAdapterViewHolder>() {
    //crear el viewHolder
    lateinit var setOnclickListenerCursoEdit:(Viaje) -> Unit
    lateinit var setOnclickListenerViajeCancel:(Viaje) -> Unit
    inner class PetAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: ItemTaxiEsperaBinding = ItemTaxiEsperaBinding.bind(itemView)

        fun bind(viaje: Viaje) {
            binding.txtCalleP.text = viaje.call_pri_via
            binding.txtCalleS.text = viaje.call_sec_via
            binding.txtBarrio.text = viaje.sect_via
            binding.btnCancelar.setOnClickListener{
                setOnclickListenerViajeCancel(viaje)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_taxi_espera, parent, false)
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