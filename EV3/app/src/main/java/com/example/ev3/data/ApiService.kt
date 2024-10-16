package com.example.ev3.data

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("ruta/a/tu/endpoint")
    suspend fun verificarEstadoCarga(): Response<EstadoCarga>
}