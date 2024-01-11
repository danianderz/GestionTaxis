package com.example.appcliente.ui.notifications

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appcliente.ViajeAdapter2
import com.example.appcliente.databinding.FragmentNotificationsBinding
import com.example.appcliente.models.Viaje
import com.example.appcliente.ui.dashboard.DashboardViewModel
import org.json.JSONArray
import org.json.JSONException

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var email: String

    private lateinit var adapter: ViajeAdapter2
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapter = ViajeAdapter2()
        layoutManager = LinearLayoutManager(activity)
        binding.rvViajes.adapter = adapter
        binding.rvViajes.layoutManager = layoutManager
        // Aquí hacemos la llamada a Volley para obtener los datos de los viajes
        val sharedPref = requireActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val id = sharedPref.getString("id_cli", "")
        val queue = Volley.newRequestQueue(activity)
        val url = "http://192.168.100.43/sv_viajes_taxis/listViajesClie2.php?id_cli_per=$id"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val viajes = parseJson(response)
                adapter.updateListViajes(viajes)
            },
            Response.ErrorListener { error ->
                Log.e("Volley error", error.toString())
                Toast.makeText(activity, "Error al cargar los viajes", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)
        return root
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}