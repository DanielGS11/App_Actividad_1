package com.example.app_actividad_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.app_actividad_1.ui.theme.App_Actividad_1Theme


class EndGameActivity : ComponentActivity() {
    private val restartGame: () -> Unit = {
        val intent = Intent(this, LauncherActivity::class.java)
        startActivity(intent)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        //Recogemos los valores dados al intent
        val score = intent.getIntExtra(MainActivity.SCORE_KEY, 0)
        val level = intent.getIntExtra(MainActivity.LEVEL_KEY, 0)
        val userName = intent.getStringExtra(MainActivity.USERNAME_KEY)  ?: "Player"

        var level10Reached = false

        if (level == 10) {
            level10Reached = true
        }

        //Contenido
        setContent {
            App_Actividad_1Theme {
                Scaffold(
                    //Barra Superior
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(stringResource(R.string.app_title)) })
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),

                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (level10Reached) {
                            Text(stringResource(R.string.level_10_reached), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        } else {
                            Text(stringResource(R.string.game_over), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }

                        //Espacio entre el titulo y el texto
                        Spacer(Modifier.height(30.dp))

                        Text(stringResource(R.string.final_score, score), style = MaterialTheme.typography.headlineMedium)
                        Text(stringResource(R.string.final_level, level), style = MaterialTheme.typography.headlineMedium)

                        Spacer(Modifier.height(30.dp))

                        Image(
                            painter = painterResource(id = R.drawable.trophy_img),
                            contentDescription = stringResource(R.string.trophy_description)
                        )

                        Spacer(Modifier.height(30.dp))

                        Row (

                        ) {
                            StandardButton(stringResource(R.string.button_play_again)) {
                                restartGame()
                            }

                            Spacer(Modifier.width(30.dp))

                            StandardButton(stringResource(R.string.button_send_data)) {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"

                                    putExtra(Intent.EXTRA_SUBJECT, "Puntuacion del jugador $userName")
                                    putExtra(Intent.EXTRA_TEXT, "El jugador $userName ha obtenido $score puntos y alcanzó el nivel $level")
                                }

                                startActivity(Intent.createChooser(intent, "Enviar Datos"))
                            }
                        }

                    }
                }
            }
        }
    }
}