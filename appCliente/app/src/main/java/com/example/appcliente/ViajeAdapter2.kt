package com.example.appcliente

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.databinding.ItemTaxiBinding
import com.example.appcliente.databinding.ItemTaxiEsperaBinding
import com.example.appcliente.models.Viaje

class ViajeAdapter2(var viajes: List<Viaje> = emptyList()) : RecyclerView.Adapter<ViajeAdapter2.TaxiAdapterViewHolder>() {
    //crear el viewHolder
    lateinit var setOnclickListenerCursoEdit:(Viaje) -> Unit
    lateinit var setOnclickListenerCursoDelete:(Viaje) -> Unit
    inner class TaxiAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: ItemTaxiBinding = ItemTaxiBinding.bind(itemView)

        fun bind(curso: Viaje) {
            binding.txtCalleP.text = curso.call_pri_via
            binding.txtCalleS.text = curso.call_sec_via
            binding.txtBarrio.text = curso.sect_via
            if(curso.est_via == 0){
                binding.txtEstado.setTextColor(Color.RED)
                binding.txtEstado.text = "Cancelado"
            }else if(curso.est_via == 2){
                binding.txtEstado.setTextColor(Color.BLUE)
                binding.txtEstado.text = "En espera"
            }else if(curso.est_via == 3){
            binding.txtEstado.setTextColor(Color.RED)
            binding.txtEstado.text = "Rechazado"
           }
            else{
                binding.txtEstado.setTextColor(Color.GREEN)
                binding.txtEstado.text = "Aceptado"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxiAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_taxi, parent, false)
        return TaxiAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaxiAdapterViewHolder, position: Int) {
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