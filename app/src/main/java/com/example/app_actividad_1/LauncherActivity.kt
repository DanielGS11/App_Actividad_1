package com.example.app_actividad_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_actividad_1.ui.theme.App_Actividad_1Theme

class LauncherActivity : ComponentActivity() {
    companion object {
        const val USER_KEY = "user"
    }

    //Metodo para mandar  el nombre del usuario al MainActivity
    private val toMainActivity: (String) -> Unit = { name ->
        val intent = Intent(this, MainActivity::class.java)

        var userName = name;

        if (userName.isEmpty()) {
            userName = "Player"
        }

        intent.putExtra(USER_KEY, userName)

        startActivity(intent)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            App_Actividad_1Theme {
                Scaffold(
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
                        /*
                        Se invoca la variable aqui para que tanto la columna donde
                        se introduce el nombre como el boton que lo envia pueda usarlo
                        y se guarde el dato a enviar
                         */
                        var name by remember { mutableStateOf("") }

                        Text(
                            stringResource(R.string.game_welcome),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(30.dp))

                        TextField(
                            label = { Text(stringResource(R.string.name_request)) }, value = name,
                            onValueChange = { name = it },
                            singleLine = true
                        )

                        Spacer(Modifier.height(80.dp))

                        StandardButton(stringResource(R.string.button_play)) {
                            toMainActivity(name)
                        }
                    }
                }
            }
        }
    }
}