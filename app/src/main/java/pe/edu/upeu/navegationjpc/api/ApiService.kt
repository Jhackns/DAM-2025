package pe.edu.upeu.navegationjpc.api

import pe.edu.upeu.navegationjpc.modelo.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<User>>
}