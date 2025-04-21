package pe.edu.upeu.sysventasjpc.ui.presentation.screens.producto

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import pe.edu.upeu.sysventasjpc.modelo.ProductoDto
import pe.edu.upeu.sysventasjpc.modelo.ProductoResp
import pe.edu.upeu.sysventasjpc.repository.ProductoRepository
import javax.inject.Inject

@HiltViewModel
class ProductoMainViewModel @Inject constructor(
    private val prodRepo: ProductoRepository,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _deleteSuccess = MutableStateFlow<Boolean?>(null)
    val deleteSuccess: StateFlow<Boolean?> = _deleteSuccess

    private val _prods = MutableStateFlow<List<ProductoResp>>(emptyList())
    val prods: StateFlow<List<ProductoResp>> = _prods

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val productos = prodRepo.reportarProductos()
                _prods.value = productos
            } catch (e: Exception) {
                Log.e("ProductoMainVM", "Error al cargar productos", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarPorId(id: Long): Flow<ProductoResp> = flow {
        try {
            emit(prodRepo.buscarProductoId(id))
        } catch (e: Exception) {
            Log.e("ProductoMainVM", "Error al buscar producto por ID", e)
            throw e
        }
    }

    fun eliminar(producto: ProductoDto) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val success = prodRepo.deleteProducto(producto)
            if (success) {
                // Recargar la lista después de eliminar
                cargarProductos()
                _deleteSuccess.value = true
            } else {
                _deleteSuccess.value = false
            }
        } catch (e: Exception) {
            Log.e("ProductoMainVM", "Error al eliminar producto", e)
            _deleteSuccess.value = false
        } finally {
            _isLoading.value = false
        }
    }

    fun clearDeleteResult() {
        _deleteSuccess.value = null
    }
    /*fun eliminarProductoDeLista(id: Long) {
        _prods.value = _prods.value.filterNot { it.idProducto == id }
    }*/
}