package com.example.appcliente

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appcliente.databinding.ActivityIngresoBinding
import com.example.appcliente.models.Viaje
import com.example.appcliente.ui.dashboard.DashboardFragment
import org.json.JSONException


class Ingreso : AppCompatActivity() {
    private lateinit var binding: ActivityIngresoBinding
    val activity = Activity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngresoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        eventos()
    }
    fun eventos(){
        val sharedPref = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val id = sharedPref.getString("id_cli", "").toString()
        val idInt = id.toInt()
        binding.btnAgregarV.setOnClickListener {
            val calleP = binding.edtCalleP.text.toString()
            val calleS = binding.edtCalleS.text.toString()
            val ref = binding.edtReferencia.text.toString()
            val barrio = binding.edtBarrio.text.toString()
            val inf = binding.edtInfo.text.toString()

            if (TextUtils.isEmpty(calleP)) {
                binding.edtCalleP.error = "Ingrese la calle principal"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(calleS)) {
                binding.edtCalleS.error = "Ingrese la calle secundaria"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(ref)) {
                binding.edtReferencia.error = "Ingrese la referencia"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(barrio)) {
                binding.edtBarrio.error = "Ingrese el Barrio o Sector"
                return@setOnClickListener
            }
            agregar(Viaje(1,idInt,calleP,calleS,ref,barrio,inf,1))
            onBackPressed()

        }
    }
    fun agregar(viaje: Viaje) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.100.43/sv_viajes_taxis/ingresarViaje.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    Toast.makeText(this, "Viaje agregado con éxito", Toast.LENGTH_SHORT).show()
                    val refreshIntent = Intent(DashboardFragment.REFRESH_ACTION)
                    sendBroadcast(refreshIntent)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al parsear la respuesta", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id_cli_per"] = viaje.id_cli_per.toString()
                params["call_pri_via"] = viaje.call_pri_via
                params["call_sec_via"] = viaje.call_sec_via
                params["ref_via"] = viaje.ref_via
                params["sect_via"] = viaje.sect_via
                params["inf_via"] = viaje.inf_via
                params["est_via"] = viaje.est_via.toString()
                return params
            }
        }
        queue.add(stringRequest)
    }
}