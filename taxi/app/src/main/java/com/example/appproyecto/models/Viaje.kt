package com.example.appproyecto.models

import java.io.Serializable

data class Viaje (
    val id: Int,
    val id_tax_per: Int,
    val call_pri: String="",
    val call_sec: String="",
    val ref: String="",
    val sec: String="",
    val inf: String="",
    val est_via: Int
): Serializable