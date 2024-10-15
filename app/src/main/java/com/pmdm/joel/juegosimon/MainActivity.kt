package com.pmdm.joel.juegosimon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

// Enum class para los botones
enum class ButtonType(val color: Color, val label: String, val number: Int) {
    YELLOW(Color.Yellow, "Botón Amarillo", 1),
    RED(Color.Red, "Botón Rojo", 2),
    BLUE(Color.Blue, "Botón Azul", 3),
    GREEN(Color.Green, "Botón Verde", 4)
}

// Clase main
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val rondas = remember { Rondas() }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "Ronda: ${rondas.ronda}", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "Combinación a pulsar: ${rondas.botonesQuePulsar.joinToString()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Se asocia a cada botón su tipo
                ButtonType.values().forEach { buttonType ->
                    CustomButton(buttonType) {
                        rondas.onButtonClicked(buttonType.number)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = rondas.message, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// Clase con el código de las rondas
class Rondas {
    var ronda by mutableStateOf(1)
    var botonesQuePulsar by mutableStateOf(generateSequence())
    var message by mutableStateOf("")
    private val botonesPulsados = mutableListOf<Int>()

    fun onButtonClicked(buttonNumber: Int) {
        botonesPulsados.add(buttonNumber)
        if (botonesPulsados.size == 4) {
            if (botonesPulsados == botonesQuePulsar) {
                ronda++
                botonesQuePulsar = generateSequence()
                botonesPulsados.clear()
                message = "¡Ronda completada! Preparate para la siguiente ronda."
            } else {
                message = "Juego terminado. Has llegado a la ronda $ronda."
            }
        }
    }
    private fun generateSequence(): List<Int> {
        return List(4) { Random.nextInt(1, 5) }
    }
}

@Composable
fun CustomButton(buttonType: ButtonType, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = buttonType.color),
        modifier = Modifier
            .padding(bottom = 16.dp)
    ) {
        Text(text = buttonType.label, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtons() {
    val rondas = Rondas()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        ButtonType.values().forEach { buttonType ->
            CustomButton(buttonType) {
                rondas.onButtonClicked(buttonType.number)
            }
        }
    }
}
