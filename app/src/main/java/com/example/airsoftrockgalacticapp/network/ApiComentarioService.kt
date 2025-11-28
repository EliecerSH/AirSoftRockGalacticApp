package com.example.airsoftrockgalacticapp.network

import com.example.airsoftrockgalacticapp.model.Comentarios
import retrofit2.http.*

interface ApiComentarioService {

    @GET("api/comentarios")
    suspend fun getComentarios(): List<Comentarios>

    @POST("api/comentarios")
    suspend fun createComentario(@Body comentario: Comentarios): Comentarios

    @PUT("api/comentarios/{id_comentario}")
    suspend fun updateComentario(
        @Path("id_comentario") id: Int,
        @Body comentario: Comentarios
    ): Comentarios

    @DELETE("api/comentarios/{id_comentario}")
    suspend fun deleteComentario(
        @Path("id_comentario") id: Int
    )
}


