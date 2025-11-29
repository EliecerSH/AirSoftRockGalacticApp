package com.example.airsoftrockgalacticapp.screen

import android.content.ContentValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.airsoftrockgalacticapp.data.CartContract
import com.example.airsoftrockgalacticapp.data.CartDbHelper
import com.example.airsoftrockgalacticapp.data.ProductContract
import com.example.airsoftrockgalacticapp.data.ProductDbHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat
import java.util.Locale

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var cartDbHelper: CartDbHelper
    private lateinit var productDbHelper: ProductDbHelper
    private lateinit var navController: NavHostController

    private val testProduct = ContentValues().apply {
        put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "test-product")
        put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "Test Product")
        put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 10000.0)
        put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "img")
        put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 1)
        put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "test")
        put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Test Description")
    }
    private var testProductId: Long = -1

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        cartDbHelper = CartDbHelper(context)
        productDbHelper = ProductDbHelper(context)

        // Limpiar las bases de datos
        cartDbHelper.writableDatabase.delete(CartContract.CartEntry.TABLE_NAME, null, null)
        productDbHelper.writableDatabase.delete(ProductContract.ProductEntry.TABLE_NAME, null, null)

        // Insertar un producto de prueba
        testProductId = productDbHelper.writableDatabase.insert(ProductContract.ProductEntry.TABLE_NAME, null, testProduct)
    }

    @After
    fun tearDown() {
        cartDbHelper.close()
        productDbHelper.close()
    }

    private fun setContent(startDestination: String = "cart") {
        composeTestRule.setContent {
            navController = rememberNavController()
            NavHost(navController = navController, startDestination = startDestination) {
                composable("cart") { CartScreen(navController = navController) }
                composable("payment/{total}") { /* Dummy payment screen */ }
            }
        }
    }

    private fun getCurrentRoute(): String? {
        return navController.currentBackStackEntry?.destination?.route
    }

    @Test
    fun whenCartIsEmpty_showsEmptyMessage() {
        setContent()
        composeTestRule.onNodeWithText("Tu carrito está vacío").assertExists()
    }

    @Test
    fun whenCartHasItems_displaysItemsAndTotals() {
        cartDbHelper.addProductToCart(testProductId)
        setContent()

        val price = 10000.0
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

        composeTestRule.onNodeWithText("Test Product").assertExists()
        composeTestRule.onNodeWithText("Cantidad: 1").assertExists()
        composeTestRule.onNodeWithText(currencyFormat.format(price)).assertExists()
        composeTestRule.onNodeWithText("Subtotal: ${currencyFormat.format(price)}").assertExists()
        composeTestRule.onNodeWithText("IVA (19%): ${currencyFormat.format(price * 0.19)}").assertExists()
        composeTestRule.onNodeWithText("Total: ${currencyFormat.format(price * 1.19)}").assertExists()
    }

    @Test
    fun clearCartButton_emptiesTheCart() {
        cartDbHelper.addProductToCart(testProductId)
        setContent()

        composeTestRule.onNodeWithText("Vaciar").performClick()
        composeTestRule.onNodeWithText("Tu carrito está vacío").assertExists()
    }

    @Test
    fun payButton_navigatesToPaymentScreen() {
        cartDbHelper.addProductToCart(testProductId)
        setContent()

        composeTestRule.onNodeWithText("Pagar").performClick()

        composeTestRule.waitUntil(2000) { getCurrentRoute()?.startsWith("payment/") == true }
        assertTrue(getCurrentRoute()?.startsWith("payment/") ?: false)
    }
}
