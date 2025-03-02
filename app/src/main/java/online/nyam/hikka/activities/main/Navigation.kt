package online.nyam.hikka.activities.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import online.nyam.hikka.activities.main.screens.HomeScreen
import online.nyam.hikka.activities.main.screens.MangaScreen

@Serializable
object HomeRoute

@Serializable
data class MangaRoute(
    val slug: String
)

@Composable
fun Navigation(
    controller: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(navController = controller, modifier = modifier, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                snackbarHostState = snackbarHostState,
                onOpenManga = {
                    controller.navigate(MangaRoute(it.slug))
                }
            )
        }
        composable<MangaRoute> {
            MangaScreen()
        }
    }
}
