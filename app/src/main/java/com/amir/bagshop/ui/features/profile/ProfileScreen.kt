package com.amir.bagshop.ui.features.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import com.amir.bagshop.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amir.bagshop.theme.Brown
import com.amir.bagshop.theme.BrownBright
import com.amir.bagshop.theme.brownFewBright
import com.amir.bagshop.util.MyScreens
import com.amir.bagshop.util.styleTime
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getNavViewModel<ProfileViewModel>()
    viewModel.loadUserData()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp , end = 16.dp ,  bottom = 56.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            ProfileToolbar {
                navigation.popBackStack()
            }


            MainAnimation()


            ShowDataSection("Email Address", viewModel.email.value) { null }
            ShowDataSection("Address", viewModel.address.value) {
                viewModel.showDialog.value = true
            }
            ShowDataSection(
                "Postal Code",
                viewModel.postalCode.value
            ) { viewModel.showDialog.value = true }
            ShowDataSection("Login Time", styleTime(viewModel.loginTime.value.toLong())) { null }



            ButtonSignOut(context, viewModel, navigation)


            if (viewModel.showDialog.value) {
                AddUserLocationDataDialog(
                    showSaveLocation = false,
                    onDismiss = { viewModel.showDialog.value = false },
                    onSubmitClicked = { address, postalCode, _ ->

                        viewModel.setUserLocation(address, postalCode)
                    }
                )
            }


        }
    }
}


@Composable
fun ShowDataSection(title: String, text: String, onLocationClick: () -> Unit?) {

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLocationClick.invoke() }
            .padding(start = 10.dp, top = 16.dp, bottom = 16.dp),
    )
    {

        Text(text = title, color = Brown, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Text(text = text, color = brownFewBright, fontSize = 15.sp, fontWeight = FontWeight.Medium)

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp), thickness = 1.dp, color = Brown
        )


    }


}


@Composable
fun MainAnimation() {


    val composition by rememberLottieComposition(

        LottieCompositionSpec.RawRes(R.raw.profile)
    )

    LottieAnimation(
        composition = composition,
        modifier = Modifier
            .size(300.dp)
            .padding(top = 16.dp, start = 40.dp, bottom = 16.dp),
        iterations = LottieConstants.IterateForever

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolbar(onBackClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                "My Profile",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 45.dp)
                    .fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
    )
}

@Composable
fun ButtonSignOut(context: Context, viewModel: ProfileViewModel, navigation: NavController) {

    Button(
        onClick = {
            Toast.makeText(context, "خداحافظ ): ", Toast.LENGTH_LONG).show()
            viewModel.signOut()
            navigation.navigate(MyScreens.IntroScreen.route) {
                popUpTo(MyScreens.IntroScreen.route) {
                    inclusive = true
                }
                navigation.popBackStack()
                navigation.popBackStack()
            }

        },
        modifier = Modifier
            .size(300.dp, 60.dp)
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        colors = ButtonDefaults.buttonColors(Brown)
    ) {

        Text(text = " Sign Out", fontSize = 17.sp)

    }
}


@Composable
fun AddUserLocationDataDialog(
    showSaveLocation: Boolean,
    onDismiss: () -> Unit,
    onSubmitClicked: (String, String, Boolean) -> Unit
) {

    val context = LocalContext.current
    val checkedState = remember { mutableStateOf(true) }
    val userAddress = remember { mutableStateOf("") }
    val userPostalCode = remember { mutableStateOf("") }
  

    Dialog(onDismissRequest = onDismiss) {

        Card(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Add Location Data",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Brown
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    value = userAddress.value,
                    onValueChange = { userAddress.value = it },
                    placeholder = { Text("Your address ...") },
                    label = { Text("Your address ...") },
                    singleLine = true,
                    shape = ShapeDefaults.Medium

                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    value = userPostalCode.value,
                    onValueChange = { userPostalCode.value = it },
                    placeholder = { Text("Write something ...") },
                    label = { Text("Write something ...") },
                    singleLine = true,
                    shape = ShapeDefaults.Medium

                )

                Spacer(modifier = Modifier.height(6.dp))

                if (showSaveLocation) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                        )

                        Text(text = "Save To Profile")

                    }

                }
                Spacer(modifier = Modifier.height(6.dp))
                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {

                        if (
                            (userAddress.value.isNotEmpty() || userAddress.value.isNotBlank()) &&
                            (userPostalCode.value.isNotEmpty() || userPostalCode.value.isNotBlank())
                        ) {
                            onSubmitClicked(
                                userAddress.value,
                                userPostalCode.value,
                                checkedState.value
                            )
                            onDismiss.invoke()
                        } else {
                            Toast.makeText(context, "please write first...", Toast.LENGTH_SHORT)
                                .show()
                        }


                    }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}



