package com.example.appcliente.models
import java.io.Serializable

data class Viaje (
    val id: Int,
    val id_cli_per: Int,
    val call_pri_via: String,
    val call_sec_via: String,
    val ref_via: String,
    val sect_via: String,
    val inf_via: String,
    val est_via: Int
): Serializable