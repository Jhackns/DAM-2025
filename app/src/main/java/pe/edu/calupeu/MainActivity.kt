package pe.edu.calupeu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var txtVal1: EditText
    private lateinit var txtVal2: EditText
    private lateinit var resultadoTV: TextView
    private var resultado: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.calc)

        // Inicializar componentes
        txtVal1 = findViewById(R.id.txtNum1)
        txtVal2 = findViewById(R.id.txtNum2)
        resultadoTV = findViewById(R.id.txtViewResul)

        // Array de IDs de botones de operaciones
        val buttons = arrayOf(R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide)

        // Asignar listener a cada botón de operación
        for (b in buttons) {
            val button = findViewById<Button>(b)
            button.setOnClickListener { operar(it) }
        }

        // Configurar botón Limpiar
        findViewById<Button>(R.id.btnClear).setOnClickListener { clear() }
    }

    private fun operar(view: View) {
        val num1Str = txtVal1.text.toString()
        val num2Str = txtVal2.text.toString()

        // Verificar si los campos están vacíos
        if (num1Str.isEmpty() || num2Str.isEmpty()) {
            resultadoTV.text = "Resultado: Error - Ingrese ambos números"
            return
        }

        try {
            val num1=num1Str.toDouble()
            val num2=num2Str.toDouble()

            when (view.id) {
                R.id.btnAdd->{
                    resultado = (num1 + num2).toString()
                    resultadoTV.text=resultado
                }
                R.id.btnSubtract->{
                    resultado=(num1-num2).toString()
                    resultadoTV.text=resultado
                }
                R.id.btnMultiply->{
                    resultado=(num1*num2).toString()
                    resultadoTV.text=resultado
                }
                R.id.btnDivide->{
                    if (num2==0.0) {
                        resultadoTV.text="Resultado: Error - División por cero"
                    }else{
                        resultado=(num1 / num2).toString()
                        resultadoTV.text=resultado
                    }
                }
            }
        } catch (e: NumberFormatException) {
            resultadoTV.text = "Resultado: Error - Números inválidos"
        }
    }

    private fun clear() {
        txtVal1.text.clear()
        txtVal2.text.clear()
        resultadoTV.text = "Resultado: 0"
        resultado = ""
    }
}