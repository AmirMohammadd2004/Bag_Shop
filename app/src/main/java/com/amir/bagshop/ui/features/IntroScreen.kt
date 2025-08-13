package com.amir.bagshop.ui.features


import com.amir.bagshop.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import com.amir.bagshop.theme.MainAppThem
import com.amir.bagshop.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainAppThem {
        Surface(
            modifier = Modifier.fillMaxSize()

        ) { IntroScreen() }
    }
}


@Composable
fun IntroScreen() {

    val navigation = getNavController()



    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.bag1),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 290.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,

        ) {

        Button(
            onClick =  {navigation.navigate( MyScreens.SignUpScreen.route ) },
            modifier = Modifier.size(250.dp, 40.dp),
            colors = ButtonDefaults.buttonColors(Color(193, 164, 138, 200))
        ) {
            Text("Sign Up", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navigation.navigate( MyScreens.SignInScreen.route ) },
            modifier = Modifier.size(250.dp, 40.dp),
            colors = ButtonDefaults.buttonColors(Color(70, 37, 8, 200))
        ) {
            Text("Sign In", fontSize = 20.sp)
        }
    }
}