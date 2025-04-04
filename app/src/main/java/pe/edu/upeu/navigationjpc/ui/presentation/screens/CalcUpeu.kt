package pe.edu.upeu.navigationjpc.ui.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upeu.navigationjpc.ui.theme.NavigationJPCTheme
import pe.edu.upeu.navigationjpc.ui.theme.lightGreenScheme
import kotlin.math.pow

fun isNumeric(toCheck: String): Boolean {
    val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
    return toCheck.matches(regex)
}

@Composable
fun ButtonX(
    modifier: Modifier,
    valuex: String,
    onExpressionChange: (String) -> Unit,
    onIsNewOpChange: (Boolean) -> Unit,
    expression: String,
    isNewOp: Boolean,
    onOpChange: (String) -> Unit,
    onOldValueChange: (String) -> Unit,
    oldValue: String,
    op: String,
    isOperator: Boolean = false
) {
    Box(
        modifier = modifier
            .background(
                color = if (isOperator) Color(0xFFFFA000) else Color(0xFF2D2D2D),
                shape = RoundedCornerShape(8.dp)
            )
            .border(0.5.dp, Color(0xFF5D5D5D), RoundedCornerShape(8.dp))
            .clickable {
                when (valuex) {
                    "π" -> {
                        val newExpression = if (isNewOp) "π" else "$expression π"
                        onExpressionChange(newExpression)
                        onIsNewOpChange(false)
                    }
                    "√" -> {
                        val newExpression = if (isNewOp) "√" else "$expression √"
                        onExpressionChange(newExpression)
                        onOpChange("√")
                        onOldValueChange(expression)
                        onIsNewOpChange(true)
                    }
                    "^" -> {
                        onOpChange("^")
                        onOldValueChange(expression)
                        onExpressionChange("$expression ^ ")
                        onIsNewOpChange(true)
                    }
                    in listOf("+", "-", "*", "/", "%") -> {
                        onOpChange(valuex)
                        onOldValueChange(expression)
                        onExpressionChange("$expression $valuex ")
                        onIsNewOpChange(true)
                    }
                    "AC" -> {
                        onExpressionChange("0")
                        onIsNewOpChange(true)
                        onOpChange("")
                        onOldValueChange("")
                    }
                    "." -> {
                        var newExpression = expression
                        if (isNewOp) {
                            newExpression = "0."
                            onIsNewOpChange(false)
                        } else if (!newExpression.split(" ").last().contains(".")) {
                            newExpression += "."
                        }
                        onExpressionChange(newExpression)
                    }
                    "=" -> {
                        if (oldValue.isNotEmpty() && op.isNotEmpty()) {
                            val currentValue = expression.split(" ").last()
                            var finalNumber = 0.0
                            val oldNum = oldValue.replace("π", "3.14159265359").toDoubleOrNull() ?: 0.0
                            val currentNum = currentValue.replace("π", "3.14159265359").toDoubleOrNull() ?: 0.0
                            when (op) {
                                "*" -> finalNumber = oldNum * currentNum
                                "/" -> finalNumber = oldNum / currentNum
                                "+" -> finalNumber = oldNum + currentNum
                                "-" -> finalNumber = oldNum - currentNum
                                "^" -> finalNumber = oldNum.pow(currentNum)
                                "√" -> finalNumber = Math.sqrt(oldNum)
                            }
                            onExpressionChange(finalNumber.toString())
                            onIsNewOpChange(true)
                            onOpChange("")
                            onOldValueChange("")
                        }
                    }
                    else -> { // Números
                        val newExpression = if (isNewOp) valuex else "$expression$valuex"
                        onExpressionChange(newExpression)
                        onIsNewOpChange(false)
                    }
                }
            }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = valuex,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = if (isOperator) Color.White else Color(0xFFFFC107)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorTextField(
    expression: String,
    modifier: Modifier,
    onExpressionChange: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFF2D2D2D), RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = expression,
            style = TextStyle(
                fontSize = 36.sp,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC107)
            ),
            maxLines = 2
        )
    }
}

@Composable
fun CalculatorFirstRow(
    expression: String,
    isNewOp: Boolean,
    onExpressionChange: (String) -> Unit,
    onIsNewOpChange: (Boolean) -> Unit,
    onOpChange: (String) -> Unit,
    onOldValueChange: (String) -> Unit,
    modifier: Modifier,
    op: String,
    oldValue: String,
    data: List<String>
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.forEach {
            ButtonX(
                modifier = Modifier
                    .weight(if (it == "=") 4f else 1f) // El botón "=" ocupa más ancho
                    .aspectRatio(if (it == "=") 7f else 1f), // Ajusta la proporción para "="
                valuex = it,
                onExpressionChange = onExpressionChange,
                onIsNewOpChange = onIsNewOpChange,
                expression = expression,
                isNewOp = isNewOp,
                onOpChange = onOpChange,
                onOldValueChange = onOldValueChange,
                op = op,
                oldValue = oldValue,
                isOperator = it in listOf("+", "-", "*", "/", "%", "^", "√", "=", "π")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalcUPeU() {
    NavigationJPCTheme(colorScheme = lightGreenScheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
                .padding(16.dp)
        ) {
            var op by remember { mutableStateOf("") }
            var isNewOp by remember { mutableStateOf(true) }
            var oldValue by remember { mutableStateOf("") }
            var expression by remember { mutableStateOf("0") }

            CalculatorTextField(
                expression = expression,
                modifier = Modifier.padding(bottom = 8.dp),
                onExpressionChange = { expression = it }
            )

            Column(modifier = Modifier.fillMaxSize()) {
                val listA = listOf("AC", "√", "%", "/")
                val listB = listOf("7", "8", "9", "*")
                val listC = listOf("4", "5", "6", "-")
                val listD = listOf("1", "2", "3", "+")
                val listE = listOf("0", ".", "π", "^")
                val listF = listOf("=")

                val listaCompleta = listOf(listA, listB, listC, listD, listE, listF)
                listaCompleta.forEachIndexed { index, row ->
                    CalculatorFirstRow(
                        isNewOp = isNewOp,
                        expression = expression,
                        onExpressionChange = { expression = it },
                        onIsNewOpChange = { isNewOp = it },
                        onOpChange = { op = it },
                        onOldValueChange = { oldValue = it },
                        modifier = Modifier
                            .weight(if (index == listaCompleta.size - 1) 0.5f else 1f) // Menor peso para la fila de "="
                            .fillMaxWidth(),
                        op = op,
                        oldValue = oldValue,
                        data = row
                    )
                }
            }
        }
    }
}