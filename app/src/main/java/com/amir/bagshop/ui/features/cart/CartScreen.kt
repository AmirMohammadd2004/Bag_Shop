package com.amir.bagshop.ui.features.cart

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.amir.bagshop.R
import com.amir.bagshop.model.data.ProductsResponse.Product
import com.amir.bagshop.theme.Brown
import com.amir.bagshop.theme.BrownBright
import com.amir.bagshop.util.MyScreens
import com.amir.bagshop.util.NetworkChecker
import com.amir.bagshop.util.formatPrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import androidx.core.net.toUri
import com.amir.bagshop.ui.features.profile.AddUserLocationDataDialog
import com.amir.bagshop.util.PAYMENT_PENDING

@Composable
fun CartScreen() {

    val getDataDialogState = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getNavViewModel<CartScreenViewModel>()

    viewModel.loadCartData()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            TopBar(
                onBackClick = { navigation.popBackStack() },
                onProfileClick = { navigation.navigate(MyScreens.ProfileScreen.route) }
            )

            if (viewModel.productList.value.isNotEmpty()) {
                CartList(
                    data = viewModel.productList.value,
                    isChangingData = viewModel.isChangingNumber.value,
                    onAddItemClick = { viewModel.addItem(it) },
                    onRemoveItemClick = { viewModel.removeItem(it) },
                    onItemClick = { navigation.navigate(MyScreens.ProductScreen.route + "/" + it) }
                )
            } else {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.empty_shopping_cart)
                )
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.fillMaxSize(),
                    iterations = LottieConstants.IterateForever
                )
            }
        }

        if (viewModel.productList.value.isNotEmpty()) {
            PurchaseAll(formatPrice(viewModel.totalPrice.intValue.toString())) {
                val locationData = viewModel.getUserLocation()
                if (locationData.first == "Click To Add" || locationData.second == "Click To Add") {
                    getDataDialogState.value = true
                } else {
                    performPurchase(context, viewModel, locationData.first, locationData.second)
                }
            }
        }
    }

    // نمایش دیالوگ خارج از PurchaseAll
    if (getDataDialogState.value) {
        AddUserLocationDataDialog(
            showSaveLocation = true,
            onDismiss = { getDataDialogState.value = false },
            onSubmitClicked = { address, postalCode, isChecked ->
                if (NetworkChecker(context).isInternetConnected) {
                    if (isChecked) {
                        viewModel.setUserLocation(address, postalCode)
                    }
                    performPurchase(context, viewModel, address, postalCode)
                } else {
                    Toast.makeText(context, "لطفا اینترنت خود را متصل کنید.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }
}

private fun performPurchase(
    context: Context,
    viewModel: CartScreenViewModel,
    address: String,
    postalCode: String
) {
    viewModel.purchaseAll(address, postalCode) { success, link ->
        if (success) {
            Toast.makeText(context, "پرداخت با زرین پال (:", Toast.LENGTH_SHORT).show()
            viewModel.setPaymentStatus(PAYMENT_PENDING)
            val intent = Intent(Intent.ACTION_VIEW, link.toUri())
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "عملیات پرداخت به مشکل خورد ): ", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun PurchaseAll(
    totalPrice: String,
    purchaseOnclick: () -> Unit
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.12f)
            .padding(bottom = 56.dp, start = 16.dp, end = 16.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (NetworkChecker(context).isInternetConnected) {
                        purchaseOnclick()
                    } else {
                        Toast.makeText(
                            context,
                            "لطفا اینترنت خود را روشن کنید.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(Brown),
                modifier = Modifier
                    .padding(4.dp)
                    .size(200.dp, 40.dp)
                    .clip(ShapeDefaults.ExtraLarge),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Text("Let's Purchase !")
            }
            Surface(
                modifier = Modifier.padding(4.dp)
            ) {
                Text("Total $totalPrice Tomans", fontSize = 13.sp, color = Brown )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Brown)
            }
        },
        title = {
            Text(
                text = "My Cart",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Brown
            )
        },
        actions = {
            IconButton(onClick = { onProfileClick() }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Brown)
            }
        },
    )
}

@Composable
fun CartList(
    data: List<Product>,
    isChangingData: Pair<String, Boolean>,
    onAddItemClick: (String) -> Unit,
    onRemoveItemClick: (String) -> Unit,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        items(data.size) {
            CartItem(
                data = data[it],
                isChangingData = isChangingData,
                onItemClick = onItemClick,
                onRemoveItemClick = onRemoveItemClick,
                onAddItemClick = onAddItemClick
            )
        }
    }
}

@Composable
fun CartItem(
    data: Product,
    isChangingData: Pair<String, Boolean>,
    onAddItemClick: (String) -> Unit,
    onRemoveItemClick: (String) -> Unit,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .clip(shape = ShapeDefaults.Large)
            .background(Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
        onClick = { onItemClick(data.productId) },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = data.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(400.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = data.name, fontSize = 18.sp, color = Brown)
                    Text(text = "From ${data.category} Group", fontSize = 15.sp, color = Brown)
                    Text(
                        modifier = Modifier.padding(top = 15.dp),
                        text = "Product Authenticity guarantee",
                        fontSize = 15.sp,
                        color = Brown
                    )
                    Text(
                        text = "Available In Stock To Ship",
                        fontSize = 15.sp,
                        color = Brown
                    )
                    Surface(
                        modifier = Modifier
                            .clip(shape = ShapeDefaults.Large)
                            .padding(bottom = 6.dp, top = 18.dp),
                        color = BrownBright
                    ) {
                        Text(
                            text = formatPrice(
                                (data.price.toInt() * (data.quantity ?: "1").toInt()).toString()
                            ),
                            modifier = Modifier.padding(8.dp),
                            fontSize = 14.sp
                        )
                    }
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(bottom = 14.dp, end = 8.dp),
                    color = Color(224, 224, 231, 255)
                ) {
                    Card(border = BorderStroke(2.dp, Brown)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (data.quantity?.toInt() == 1) {
                                IconButton(onClick = { onRemoveItemClick(data.productId) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            } else {
                                IconButton(onClick = { onRemoveItemClick(data.productId) }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                            if (isChangingData.first == data.productId && isChangingData.second) {
                                Text(
                                    "...",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            } else {
                                Text(
                                    data.quantity ?: "1",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            IconButton(onClick = { onAddItemClick(data.productId) }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}
