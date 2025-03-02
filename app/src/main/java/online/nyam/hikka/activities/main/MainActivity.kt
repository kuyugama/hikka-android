package online.nyam.hikka.activities.main

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
import androidx.navigation.compose.rememberNavController
import online.nyam.hikka.ui.theme.HikkaTheme
import org.koin.compose.KoinContext
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {
    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HikkaTheme {
                val snackbarHostState =
                    remember {
                        SnackbarHostState()
                    }

                rememberKoinModules { listOf(mainActivityKoinModule) }

                KoinContext {
                    val navGraphController = rememberNavController()

                    Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) {
                            Snackbar(
                                it,
                                containerColor = MaterialTheme.colorScheme.surface.copy(.9f),
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }) { innerPadding ->
                        Navigation(
                            controller = navGraphController,
                            snackbarHostState = snackbarHostState,
                            Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
