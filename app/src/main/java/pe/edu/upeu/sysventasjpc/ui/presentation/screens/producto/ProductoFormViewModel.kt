package pe.edu.upeu.sysventasjpc.ui.presentation.screens.producto

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.sysventasjpc.modelo.Categoria
import pe.edu.upeu.sysventasjpc.modelo.Marca
import pe.edu.upeu.sysventasjpc.modelo.ProductoDto
import pe.edu.upeu.sysventasjpc.modelo.ProductoResp
import pe.edu.upeu.sysventasjpc.modelo.UnidadMedida
import pe.edu.upeu.sysventasjpc.repository.CategoriaRepository
import pe.edu.upeu.sysventasjpc.repository.MarcaRepository
import pe.edu.upeu.sysventasjpc.repository.ProductoRepository
import pe.edu.upeu.sysventasjpc.repository.UnidadMedidaRepository
import javax.inject.Inject

@HiltViewModel
class ProductoFormViewModel @Inject constructor(
    private val prodRepo: ProductoRepository,
    private val marcRepo: MarcaRepository,
    private val cateRepo: CategoriaRepository,
    private val umRepo: UnidadMedidaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _producto = MutableStateFlow<ProductoResp?>(null)
    val producto: StateFlow<ProductoResp?> = _producto

    private val _marcs = MutableStateFlow<List<Marca>>(emptyList())
    val marcs: StateFlow<List<Marca>> = _marcs

    private val _categors = MutableStateFlow<List<Categoria>>(emptyList())
    val categors: StateFlow<List<Categoria>> = _categors

    private val _unidMeds = MutableStateFlow<List<UnidadMedida>>(emptyList())
    val unidMeds: StateFlow<List<UnidadMedida>> = _unidMeds

    private val _operationSuccess = MutableStateFlow<Boolean?>(null)
    val operationSuccess: StateFlow<Boolean?> = _operationSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getProducto(idX: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _producto.value = prodRepo.buscarProductoId(idX)
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener producto: ${e.message}"
                Log.e("ProductoFormVM", "Error al obtener producto", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDatosPrevios() {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                _marcs.value = marcRepo.findAll()
                _categors.value = cateRepo.findAll()
                _unidMeds.value = umRepo.findAll()
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener datos previos: ${e.message}"
                Log.e("ProductoFormVM", "Error al obtener datos previos", e)
            }
        }
    }

    fun addProducto(producto: ProductoDto) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                if (validateProducto(producto)) {
                    val success = prodRepo.insertarProducto(producto)
                    _operationSuccess.value = success
                    if (!success) {
                        _errorMessage.value = "Error al agregar producto"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al agregar producto: ${e.message}"
                Log.e("ProductoFormVM", "Error al agregar producto", e)
                _operationSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun editProducto(producto: ProductoDto) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                if (validateProducto(producto)) {
                    val success = prodRepo.modificarProducto(producto)
                    _operationSuccess.value = success
                    if (!success) {
                        _errorMessage.value = "Error al editar producto"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al editar producto: ${e.message}"
                Log.e("ProductoFormVM", "Error al editar producto", e)
                _operationSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun validateProducto(producto: ProductoDto): Boolean {
        return when {
            producto.nombre.isBlank() -> {
                _errorMessage.value = "El nombre del producto es requerido"
                false
            }
            producto.pu <= 0 -> {
                _errorMessage.value = "El precio unitario debe ser mayor a 0"
                false
            }
            producto.stock < 0 -> {
                _errorMessage.value = "El stock no puede ser negativo"
                false
            }
            producto.marca <= 0 -> {
                _errorMessage.value = "Debe seleccionar una marca"
                false
            }
            producto.categoria <= 0 -> {
                _errorMessage.value = "Debe seleccionar una categoría"
                false
            }
            producto.unidadMedida <= 0 -> {
                _errorMessage.value = "Debe seleccionar una unidad de medida"
                false
            }
            else -> true
        }
    }

    fun clearOperationResult() {
        _operationSuccess.value = null
        _errorMessage.value = null
    }
}