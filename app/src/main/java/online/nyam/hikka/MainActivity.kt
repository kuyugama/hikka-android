package online.nyam.hikka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import online.nyam.hikka.ui.screens.HomeScreen
import online.nyam.hikka.ui.theme.HikkaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HikkaTheme {
                val snackbarHostState =
                    remember {
                        SnackbarHostState()
                    }
                Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState) {
                        Snackbar(
                            it,
                            containerColor = MaterialTheme.colorScheme.surface.copy(.9f),
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }) { innerPadding ->
                    HomeScreen(
                        snackbarHostState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
