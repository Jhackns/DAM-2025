package pe.edu.upeu.sysventasjpc

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pe.edu.upeu.sysventasjpc.utils.isNight

// Anotación para habilitar Hilt en la aplicación
@HiltAndroidApp
@ExperimentalCoroutinesApi
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Define el modo nocturno según la función isNight()
        val mode = if (isNight()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        // Establece el modo nocturno predeterminado
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
