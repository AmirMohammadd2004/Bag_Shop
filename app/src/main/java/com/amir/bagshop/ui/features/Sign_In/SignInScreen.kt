package com.amir.bagshop.ui.features.Sign_In


import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amir.bagshop.theme.MainAppThem
import com.amir.bagshop.R ///////////////
import com.amir.bagshop.util.MyScreens
import com.amir.bagshop.util.NetworkChecker
import com.amir.bagshop.util.VALUE_SUCCESS
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {

    MainAppThem {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) { SignInScreen() }
    }
}


@Composable
fun SignInScreen() {

    val viewModel = getNavViewModel<SignInViewModel>()

    val navigation = getNavController()


    val context = LocalContext.current



    Box {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4F)
                .background(
                    color = Color(70, 37, 8, 230)
                )
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            IconApp()
            Spacer(modifier = Modifier.height(30.dp))
            MainCardView(viewModel, navigation) {

                viewModel.stateSignIn {

                    if (it == VALUE_SUCCESS) {

                        navigation.navigate(MyScreens.IntroScreen.route) {
                            popUpTo(MyScreens.IntroScreen.route) {
                                inclusive = true
                            }
                        }

                    } else {

                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}

@Composable
fun IconApp() {

    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .size(90.dp)
    ) {

        Image(
            modifier = Modifier.padding(14.dp),
            painter = painterResource(R.drawable.icon_app),
            contentDescription = null
        )
    }
}


@Composable
fun MainCardView(
    viewModel: SignInViewModel, navigation: NavController, SignInEvent: () -> Unit
) {

    val textFieldEmail = viewModel.email.observeAsState("")
    val textFieldPassword = viewModel.password.observeAsState("")

    val context = LocalContext.current


    Card(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(top = 70.dp)
            .padding(bottom = 160.dp)
            .shadow(8.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign In",
                color = Color(70, 37, 8, 230),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(15.dp)
            )



            MaiTExtField(
                edtvalue = textFieldEmail.value,
                onvalueChange = { viewModel.email.value = it },
                hint = "Email",
                icon = R.drawable.email_ic
            )

            MaiTExtFieldPass(
                edtvalue = textFieldPassword.value,
                onvalueChange = { viewModel.password.value = it },
                hint = "Password",
                icon = R.drawable.lock_ic
            )



            Button(
                {

                    if (textFieldEmail.value.isNotEmpty() && textFieldPassword.value.isNotEmpty()) {

                        if (Patterns.EMAIL_ADDRESS.matcher(textFieldEmail.value).matches()) {

                            if (NetworkChecker(context).isInternetConnected) {

                                SignInEvent.invoke()

                            } else {
                                Toast.makeText(
                                    context,
                                    " دسترسی به اینترنت خود را چک کنید. ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(
                                context, " ایمیل را به شکل صحیح وارد کنید .", Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            context, " لطفا تمامی مقادیر را وارد کنید. ", Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                modifier = Modifier
                    .padding(15.dp)
                    .size(250.dp, 47.dp),
                colors = ButtonDefaults.buttonColors(Color(70, 37, 8, 230))
            ) {
                Text(
                    modifier = Modifier.padding(5.dp), fontSize = 18.sp, text = "Log In"
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    fontSize = 18.sp,
                    text = "Don't have an account?"
                )
                TextButton(

                    {
                        navigation.navigate(MyScreens.SignUpScreen.route) {

                            popUpTo(MyScreens.SignInScreen.route) {
                                inclusive = true

                            }

                        }
                    }


                ) {


                    Text(
                        "Register here", color = Color(165, 141, 122, 255)
                    )
                }
            }


        }

    }
}


@Composable
fun MaiTExtField(
    edtvalue: String, onvalueChange: (String) -> Unit, hint: String, icon: Int

) {
    OutlinedTextField(
        value = edtvalue,
        onValueChange = onvalueChange,
        label = { Text(hint) },
        placeholder = { Text(hint) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(vertical = 8.dp),
        leadingIcon = { Icon(painterResource(icon), contentDescription = null) })
}

@Composable
fun MaiTExtFieldPass(
    edtvalue: String, onvalueChange: (String) -> Unit, hint: String, icon: Int

) {

    val visibilityPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = edtvalue,
        onValueChange = onvalueChange,
        label = { Text(hint) },
        placeholder = { Text(hint) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(vertical = 8.dp),
        leadingIcon = { Icon(painterResource(icon), contentDescription = null) },
        visualTransformation = if (visibilityPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {

            val image = if (visibilityPassword.value) {

                painterResource(R.drawable.visibility_ic)
            } else {

                painterResource(R.drawable.visibility_off)
            }
            Icon(painter = image, contentDescription = null, modifier = Modifier.clickable {

                visibilityPassword.value = !visibilityPassword.value
            })
        },

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}
