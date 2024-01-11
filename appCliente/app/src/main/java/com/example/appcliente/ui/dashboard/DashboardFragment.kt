package com.example.appcliente.ui.dashboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appcliente.Ingreso
import com.example.appcliente.RefreshFlightsEvent
import com.example.appcliente.ViajeAdapter
import com.example.appcliente.databinding.FragmentDashboardBinding
import com.example.appcliente.models.Viaje
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.Executors

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    companion object {
        const val REFRESH_ACTION = "REFRESH_ACTION"
    }
    private val refreshReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            cargarDatos()
        }
    }
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var rv: RecyclerView
    private val adapter: ViajeAdapter by lazy{
        ViajeAdapter()
    }
    private fun cargarAdaptador(){
        rv.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        rv = binding.rvViajes
        cargarAdaptador()
        val filter = IntentFilter(REFRESH_ACTION)
        requireContext().registerReceiver(refreshReceiver, filter)
        cargarDatos()
        cancelar()
        eventos()
        return binding.root
    }

    private fun parseJson(json: String): List<Viaje> {
        val viajes = mutableListOf<Viaje>()
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id_via")
                val id_cli_per = jsonObject.getInt("id_cli_per")
                val call_pri_via = jsonObject.getString("call_pri_via")
                val call_sec_via = jsonObject.getString("call_sec_via")
                val ref_via = jsonObject.getString("ref_via")
                val sect_via = jsonObject.getString("sect_via")
                val inf_via = jsonObject.getString("inf_via")
                val est_via = jsonObject.getInt("est_via")

                val viaje = Viaje(id,id_cli_per,call_pri_via, call_sec_via,ref_via, sect_via,inf_via,est_via)
                viajes.add(viaje)
            }
        } catch (e: JSONException) {
            Log.e("JSON parse error", e.toString())
        }
        return viajes
    }

    fun cargarDatos(){
        val sharedPref = requireActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val id = sharedPref.getString("id_cli", "")
        val queue = Volley.newRequestQueue(activity)
        val url = "http://192.168.100.43/sv_viajes_taxis/listViaAntClie.php?id_cli_per=$id"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val viajes = parseJson(response)
                adapter.viajes = viajes
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Log.e("Volley error", error.toString())
                Toast.makeText(activity, "Error al cargar los viajes", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)
    }
    private fun cancelar(){

        adapter.setOnclickListenerViajeCancel = { viaje ->
            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.100.43/sv_viajes_taxis/cancelarSolVia.php"

            val postRequest = object : StringRequest(Method.POST, url, Response.Listener<String> { response ->
                Toast.makeText(activity, "Viaje cancelado", Toast.LENGTH_SHORT).show()
                cargarDatos()
            }, Response.ErrorListener { error ->
                Toast.makeText(activity, "Error al cancelar el viaje", Toast.LENGTH_SHORT).show()
            }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id_via"] = viaje.id.toString()
                    return params
                }
            }
            queue.add(postRequest)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(refreshReceiver)
        _binding = null
    }
    private fun eventos(){
        binding.flbRegistrar.setOnClickListener(){
            startActivity(Intent(requireContext(), Ingreso::class.java))
        }
    }
}

