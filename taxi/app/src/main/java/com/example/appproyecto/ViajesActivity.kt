package com.example.appproyecto


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import com.example.appproyecto.databinding.ActivityViajesBinding
import com.example.appproyecto.models.Viaje
import org.json.JSONException

class ViajesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViajesBinding

    private val adapter: viajeAdapter by lazy{
        viajeAdapter()
    }
    private fun cargarAdaptador(){
        binding.rvViaje.adapter = adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viajes)
        binding = ActivityViajesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarAdaptador()
        cargarDatos()
        aceptar()
        rechazar()
    }

    fun cargarDatos(){
        val queue = Volley.newRequestQueue(this)
        var id = globales.id
        val url = "http://192.168.100.43/sv_viajes_taxis/listViaAsigTax.php?id_tax_per=$id"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val viajes = parseJson(response)
                adapter.viajes = viajes
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Log.e("Volley error", error.toString())
                Toast.makeText(this, "Error al cargar los viajes", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)
    }
    private fun rechazar(){
        binding.rvViaje.adapter = adapter
        adapter.setOnclickListenerViajeCancel = { viaje ->
            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.100.43/sv_viajes_taxis/cancelarSolVia.php?id_via=${viaje.id}"

            val postRequest = object : StringRequest(Method.POST, url, Response.Listener<String> { response ->
                Toast.makeText(this, "Viaje cancelado", Toast.LENGTH_SHORT).show()
                cargarDatos()
            }, Response.ErrorListener { error ->
                Toast.makeText(this, "Error al cancelar el viaje", Toast.LENGTH_SHORT).show()
            }) {
            }
            queue.add(postRequest)
        }
    }
    private fun aceptar(){
        binding.rvViaje.adapter = adapter
        adapter.setOnclickListenerViajeAceptar = {
            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.100.43/sv_viajes_taxis/aceptarSoli.php?id_via=${it.id}"

            val postRequest = object : StringRequest(Method.POST, url, Response.Listener<String> { response ->
                Toast.makeText(this, "Viaje aceptado" + it.id, Toast.LENGTH_SHORT).show()
                cargarDatos()
            }, Response.ErrorListener { error ->
                Toast.makeText(this, "Error al aceptar el viaje", Toast.LENGTH_SHORT).show()
            }) {
            }
            queue.add(postRequest)
        }
    }
    private fun parseJson(json: String): List<Viaje> {
        val viajes = mutableListOf<Viaje>()
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id_via")
                val id_cli_per = jsonObject.getInt("id_cli_per")
                val id_tax_per = jsonObject.getInt("id_tax_per")
                val call_pri_via = jsonObject.getString("call_pri_via")
                val call_sec_via = jsonObject.getString("call_sec_via")
                val ref_via = jsonObject.getString("ref_via")
                val sect_via = jsonObject.getString("sect_via")
                val inf_via = jsonObject.getString("inf_via")
                val est_via = jsonObject.getInt("est_via")

                val viaje = Viaje(id,0,call_pri_via, call_sec_via,ref_via, sect_via,inf_via,est_via)
                viajes.add(viaje)
            }
        } catch (e: JSONException) {
            Log.e("JSON parse error", e.toString())
        }
        return viajes
    }
}