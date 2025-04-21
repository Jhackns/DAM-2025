package pe.edu.upeu.sysventasjpc.repository

import android.util.Log
import pe.edu.upeu.sysventasjpc.data.remote.RestProducto
import pe.edu.upeu.sysventasjpc.modelo.ProductoDto
import pe.edu.upeu.sysventasjpc.modelo.ProductoResp
import pe.edu.upeu.sysventasjpc.utils.TokenUtils
import javax.inject.Inject

interface ProductoRepository {
    suspend fun deleteProducto(producto: ProductoDto): Boolean
    suspend fun reportarProductos(): List<ProductoResp>
    suspend fun buscarProductoId(id: Long): ProductoResp
    suspend fun insertarProducto(producto: ProductoDto): Boolean
    suspend fun modificarProducto(producto: ProductoDto): Boolean
}

class ProductoRepositoryImp @Inject constructor(
    private val restProducto: RestProducto,
    //private val actividadDao: ActividadDao,
) : ProductoRepository {

    override suspend fun deleteProducto(producto: ProductoDto): Boolean {
        return try {
            val response = restProducto.deleteProducto(TokenUtils.TOKEN_CONTENT, producto.idProducto)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error al eliminar producto", e)
            false
        }
    }

    override suspend fun reportarProductos(): List<ProductoResp> {
        return try {
            val response = restProducto.reportarProducto(TokenUtils.TOKEN_CONTENT)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("ProductoRepository", "Error al obtener productos: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error al obtener productos", e)
            emptyList()
        }
    }

    override suspend fun buscarProductoId(id: Long): ProductoResp {
        return try {
            val response = restProducto.getProductoId(TokenUtils.TOKEN_CONTENT, id)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Producto no encontrado")
            } else {
                throw Exception("Error al buscar producto: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error al buscar producto", e)
            throw e
        }
    }

    override suspend fun insertarProducto(producto: ProductoDto): Boolean {
        return try {
            val response = restProducto.insertarProducto(TokenUtils.TOKEN_CONTENT, producto)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error al insertar producto", e)
            false
        }
    }

    override suspend fun modificarProducto(producto: ProductoDto): Boolean {
        return try {
            val response = restProducto.actualizarProducto(TokenUtils.TOKEN_CONTENT, producto.idProducto, producto)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error al modificar producto", e)
            false
        }
    }
}