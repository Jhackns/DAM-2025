@file:OptIn(ExperimentalMaterial3Api::class)

package pe.edu.upeu.navegationjpc.ui.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@Composable
fun CardScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Ejemplo de Cards") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(5) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Imagen
                        AsyncImage(
                            model = "https://www.bezoya.es/wp-content/uploads/2024/05/que-se-considera-agua-mineral.webp", // URL de la imagen
                            contentDescription = "Imagen de la Card",
                            modifier = Modifier
                                .height(120.dp) // Altura fija para la imagen
                                .fillMaxWidth(0.8f) // Ocupa el 80% del ancho del Card
                                .align(Alignment.CenterHorizontally), // Centra la imagen horizontalmente
                            contentScale = ContentScale.Crop // Escala la imagen para recortarla
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Título
                        Text(
                            text = "Título de la Card $index",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Descripción
                        Text(
                            text = "Contenido de ejemplo para la Card $index.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCardScreen() {
    CardScreen()
}