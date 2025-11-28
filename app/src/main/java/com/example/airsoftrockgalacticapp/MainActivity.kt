package com.example.airsoftrockgalacticapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.airsoftrockgalacticapp.screen.ContactanosScreen
import com.example.airsoftrockgalacticapp.screen.HomeScreen
import com.example.airsoftrockgalacticapp.screen.LoginScreen
import com.example.airsoftrockgalacticapp.screen.NosotrosScreen
import com.example.airsoftrockgalacticapp.screen.RegisterScreen
import com.example.airsoftrockgalacticapp.ui.theme.AirSoftRockGalacticAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeDataStore = remember { ThemeDataStore(this) }
            val isDarkMode by themeDataStore.isDarkMode.collectAsState(initial = false)

            AirSoftRockGalacticAppTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()

    // NavHost es el encargado de gestionar las animaciones entre pantallas.
    // Se definen con enterTransition y exitTransition.
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = Modifier.fillMaxSize(),
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable(
            route = "home/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            HomeScreen(email = email, navController = navController)
        }
        composable("contactanos") {
            ContactanosScreen(viewModel = mainViewModel, navController = navController)
        }
        composable("nosotros") {
            NosotrosScreen(navController = navController)
        }
    }
}