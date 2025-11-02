package com.example.app_actividad_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    //Constantes del juego
    companion object {
        const val TAG = "MainActivity"
        const val SCORE_KEY = "score"
        const val LEVEL_KEY = "level"
        const val USERNAME_KEY = "username"
    }

    //Crear objeto e iniciarlo
    private var game: Game


    init {
        game = Game()
    }

    //Metodo para aumentar los puntos y nivel y darselos al juego
    private val increaseScoreLevel: (Int) -> Game = { increase ->
        var randomIncrease = 1

        if (game.level > 2) {
            randomIncrease =  Random.nextInt(increase, game.level + 1)
        }

        val increasedScore = game.score + randomIncrease
        val userLevel = increasedScore / 10

        //Se copian los datos ya creados y devuelve el objeto
        game = game.copy(score = increasedScore, level = userLevel)
        game
    }

    //Metodo para disminuir los puntos y nivel y darselos al juego
    private val decreaseScoreLevel: (Int) -> Game = { decrease ->
        var decreasedScore = 0

        if (decrease <= game.score) {
            decreasedScore = game.score - decrease
        }

        val userLevel = decreasedScore / 10


        //Se copian los datos ya creados y devuelve el objeto
        game = game.copy(score = decreasedScore, level = userLevel)
        game
    }

    //Metodo para abrir EndGameActiviti (Con intent) y darle los datos
    private val goToEndGameActivity: () -> Unit = {
        val intent = Intent(this, EndGameActivity::class.java)
        intent.putExtra(SCORE_KEY, game.score)
        intent.putExtra(LEVEL_KEY, game.level)
        intent.putExtra(USERNAME_KEY, game.name)

        //Inicia la actividad con los parametros ya actualizados
        startActivity(intent)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    //Crear la pantalla
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { instance ->
            game = Game(
                score = instance.getInt(SCORE_KEY, 0),
                level = instance.getInt(LEVEL_KEY, 0)
            )
        }

        //Ocupar toda la pantalla
        enableEdgeToEdge()

        //Recogo el Nombre de Usuario
        val userName = intent.getStringExtra(LauncherActivity.USER_KEY) ?: "Player"

        game.name = userName

        //Aqui se define la Interfaz
        setContent {
            MaterialTheme {
                Scaffold(
                    //Barra Superior
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(stringResource(R.string.app_title)) })
                    }
                ) { innerPadding ->
                    /*
                    Cuerpo, recibe el objeto, las lambdas de los botones y
                    ademas el padding de estos
                     */
                    GameStateDisplay(
                        game,
                        Modifier.padding(innerPadding),
                        increaseScoreLevel,
                        decreaseScoreLevel,
                        goToEndGameActivity
                    )
                }
            }
        }
    }

    //Metodo que informa de la App a punto de ser visible
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart. Actividad a punto de ser visible")
    }


    //Metodo que informa de la visibilidad en primer plano de la App
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Actividad en primer plano")
    }

    //Mwtodo que informa del paso a segundo plano de la App
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Actividad en segundo plano.")
    }

    //Metodo que informa de que la App no es visible
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Actividad no visible.")
    }

    //Metodo que informa de la destruccion de la App
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Actividad destruida.")
    }

    //Metodo que guarda el estado de la App
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Pasamos los parametros al estado
        outState.putInt(SCORE_KEY, game.score)
        outState.putInt(LEVEL_KEY, game.level)

        Log.d(TAG, "onSaveInstanceState: Estado Guardado")
    }

    //Metodo que restaura el estado de la App
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.d(TAG, "onRestoreInstanceState: Estado Recuperado")
    }
}

@Composable
//Interfaz del Juego
fun GameStateDisplay(
    game: Game,
    modifier: Modifier = Modifier,
    onIncButton: (Int) -> Game,
    onDecButton: (Int) -> Game,
    onEndGameButton: () -> Unit
) {
    var gameNow by remember { mutableStateOf(game) }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        //Fila 1 con el nombre
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Welcome(gameNow.name)
        }

        //Fila 2 con los puntos y el nivel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var color: Color?
            if (gameNow.level >= 0 && gameNow.level < 3) {
                color = colorResource(R.color.level_0_to_2)
            } else if (gameNow.level >= 3 && gameNow.level < 7) {
                color = colorResource(R.color.level_3_to_6)
            } else {
                color = colorResource(R.color.level_7_to_9)
            }
            //Columna con las variables
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(color)
                    .padding(8.dp),
            ) {
                ShowParams(stringResource(R.string.param_score), gameNow.score)
                //Espacio entre parametros
                Spacer(Modifier.height(20.dp))

                ShowParams(stringResource(R.string.param_level), gameNow.level)

                if (gameNow.level >= 5 && gameNow.level < 10) {
                    Spacer(Modifier.height(10.dp))

                    Text("You're doing well!")

                } else if (gameNow.level == 10) {
                    onEndGameButton()
                }
            }

            Spacer(Modifier.height(10.dp))

            //Columna con el boton de aumentar los puntos
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Boton con la funcion y el numero de puntos que aumenta
                StandardButton(stringResource(R.string.button_increase)) {
                    gameNow = onIncButton(1)
                }

                Spacer(Modifier.height(4.dp))

                StandardButton(stringResource(R.string.button_decrease)) {
                    gameNow = if (gameNow.level == 0) {
                        onDecButton(1)
                    } else {
                        onDecButton(gameNow.level * 2)
                    }
                }
            }
        }

        //Fila 3 con el boton para terminar el Juego
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            //Boton con la Funcion de Terminar el Juego
            StandardButton(stringResource(R.string.button_end_game)) {
                onEndGameButton()
            }
        }
    }
}

//Funciones Auxiliares
//-- Saludar al Usuario
@Composable
fun Welcome(name: String?, modifier: Modifier = Modifier) {
    Text("Hello $name", modifier = modifier, fontWeight = FontWeight.Bold)
}

//-- Mostrar los parametros (En este caso Score y Level)
@Composable
fun ShowParams(param: String, value: Int, modifier: Modifier = Modifier) {
    Text("$param: $value", modifier = modifier)
}

//-- Botones para aumentar puntos y terminar el juego
@Composable
fun StandardButton(textData: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    //Funcion del Boton
    Button(onClick = onClick) {
        Text(textData, modifier = modifier)
    }
}

//Vista Previa sin ejecutar la App
@Preview(showBackground = true)
@Composable
fun PreviewGame() {
    GameStateDisplay(
        game = Game(),
        onIncButton = { it -> Game(score = it) },
        onDecButton = { it -> Game(score = it) },
        onEndGameButton = {}
    )
}
