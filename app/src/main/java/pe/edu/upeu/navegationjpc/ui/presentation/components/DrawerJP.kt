package pe.edu.upeu.navegationjpc.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pe.edu.upeu.navegationjpc.ui.navigation.BottomNavigationBar
import pe.edu.upeu.navegationjpc.ui.navigation.NavigationHost
import pe.edu.upeu.navegationjpc.ui.theme.ThemeType

data class DrawerItem(val title: String, val route: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppDrawer(
    darkMode: MutableState<Boolean>,
    themeType: MutableState<ThemeType>
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Lista de destinos para el Drawer
    val drawerItems = listOf(
        DrawerItem("Home", "home"),
        DrawerItem("Profile", "profile"),
        DrawerItem("Settings", "settings"),
        DrawerItem("Calc", "calc")
    )
    var selectedItem by remember { mutableStateOf("home") }
    // Drawer con navegación
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                //.fillMaxSize()

            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Menú", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
                    drawerItems.forEach { item ->
                        // Resalta el item seleccionado
                        val backgroundColor = if (item.route == selectedItem) Color.LightGray else Color.Transparent
                        Text(
                            text = item.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(backgroundColor)
                                .padding(8.dp)
                                .clickable {
                                    selectedItem = item.route
                                    navController.navigate(item.route)
                                    scope.launch { drawerState.close() }
                                }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    ) {
        Scaffold(

            topBar = { CustomTopAppBar(
                "",
                darkMode = darkMode,
                themeType = themeType,
                navController = navController,
                scope = scope,
                scaffoldState = drawerState,
                openDialog={}
            )},
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    NavigationHost(navController)
                }
            },
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
            )
        }
}