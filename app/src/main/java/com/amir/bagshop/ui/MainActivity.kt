package com.amir.bagshop.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amir.bagshop.di.myModules
import com.amir.bagshop.model.repository.TokenInMemory
import com.amir.bagshop.model.repository.user.UserRepository
import com.amir.bagshop.theme.MainAppThem
import com.amir.bagshop.ui.features.cart.CartScreen
import com.amir.bagshop.ui.features.category.CategoryScreen
import com.amir.bagshop.ui.features.IntroScreen
import com.amir.bagshop.ui.features.Main.MainScreen
import com.amir.bagshop.ui.features.product.ProductScreen
import com.amir.bagshop.ui.features.profile.ProfileScreen
import com.amir.bagshop.ui.features.Sign_In.SignInScreen
import com.amir.bagshop.ui.features.sign_Up.SignUPScreen
import com.amir.bagshop.util.KEY_Category_ARG
import com.amir.bagshop.util.KEY_PRODUCT_ARG
import com.amir.bagshop.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.decorView.layoutDirection= View.LAYOUT_DIRECTION_LTR
        setContent {

            Koin(appDeclaration = {

                androidContext(this@MainActivity)

                modules(myModules)

            }) {

                val userRepository : UserRepository = get()
                userRepository.loadToken()

                MainAppThem {
                    Surface(modifier = Modifier.fillMaxSize() .background(Color.White)) {
                        BagShopUi()
                    }
                }

            }
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun BagShopUi() {

    val navigationController = rememberNavController()

    KoinNavHost(
        navController = navigationController,
        startDestination = MyScreens.IntroScreen.route
    ) {

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        composable(
            route = "${MyScreens.ProductScreen.route}/{${KEY_PRODUCT_ARG}}",
            arguments = listOf(navArgument(KEY_PRODUCT_ARG) {
                type = NavType.StringType
            })
        ) {
            ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG, "null"))
        }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        composable(
            route = "${MyScreens.CategoryScreen.route}/{${KEY_Category_ARG}}",
            arguments = listOf ( navArgument(KEY_Category_ARG) {
                type = NavType.StringType
            })
        ) {
            CategoryScreen(it.arguments!!.getString(KEY_Category_ARG, "null category"))
        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        composable(MyScreens.ProfileScreen.route) {
            ProfileScreen()
        }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        composable(MyScreens.CartScreen.route) {
            CartScreen()
        }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        composable(MyScreens.SignUpScreen.route) {
            SignUPScreen()
        }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        composable(MyScreens.SignInScreen.route) {
            SignInScreen()
        }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        composable(MyScreens.IntroScreen.route) {

            if (TokenInMemory.token != null) {

                MainScreen()

            } else {

                IntroScreen()
            }
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    MainAppThem {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.LightGray) {
            BagShopUi()
        }
    }
}
