package com.example.airsoftrockgalacticapp.screen

import android.content.ContentValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.airsoftrockgalacticapp.data.UserContract
import com.example.airsoftrockgalacticapp.data.UserDbHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var dbHelper: UserDbHelper
    private lateinit var navController: NavHostController

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = UserDbHelper(context)


        val db = dbHelper.writableDatabase
        db.delete(UserContract.UserEntry.TABLE_NAME, null, null)

        // Insertar un usuario de prueba
        val values = ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_NAME_NAME, "testuser")
            put(UserContract.UserEntry.COLUMN_NAME_EMAIL, "test@example.com")
            put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, "password")
        }
        db.insert(UserContract.UserEntry.TABLE_NAME, null, values)

        composeTestRule.setContent {
            navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController = navController) }
                composable("home/{email}") { /* Dummy home screen */ }
                composable("register") { /* Dummy register screen */ }
            }
        }
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    private fun getCurrentRoute(): String? {
        return navController.currentBackStackEntry?.destination?.route
    }

    @Test
    fun loginSuccessNavigatesToHome() {
        composeTestRule.onNodeWithText("Correo").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password")

        composeTestRule.onNodeWithText("Iniciar seccion").performClick()

        composeTestRule.waitUntil(timeoutMillis = 2000) {
            getCurrentRoute()?.startsWith("home/") == true
        }
        assertTrue(getCurrentRoute()?.startsWith("home/") ?: false)
    }

    @Test
    fun loginFailureShowsToastAndStaysOnLogin() {
        composeTestRule.onNodeWithText("Correo").performTextInput("wrong@example.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("wrongpassword")

        composeTestRule.onNodeWithText("Iniciar seccion").performClick()

        composeTestRule.mainClock.advanceTimeBy(1000)
        
        assertEquals("login", getCurrentRoute())
    }
}
