@Composable
fun ProductoMainSC(
    text: String,
    darkMode: MutableState<Boolean>,
    navController: NavHostController,
    viewModel: ProductoMainViewModel = hiltViewModel()
) {
    val productos by viewModel.productos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getProductos()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearOperationResult()
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lista de Productos",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        navController.navigate(Destinations.ProductoFormSC.route + "/0")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                    Text("Nuevo Producto")
                }
            }

            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay productos registrados")
                }
            } else {
                LazyColumn {
                    items(productos) { producto ->
                        ProductoItem(
                            producto = producto,
                            onEditClick = {
                                val productoJson = Gson().toJson(producto.toDto())
                                navController.navigate(Destinations.ProductoFormSC.route + "/$productoJson")
                            },
                            onDeleteClick = {
                                viewModel.deleteProducto(producto.idProducto)
                            }
                        )
                    }
                }
            }
        }
    }
} 