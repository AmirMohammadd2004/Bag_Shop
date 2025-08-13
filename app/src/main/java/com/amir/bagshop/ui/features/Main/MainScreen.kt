package com.amir.bagshop.ui.features.Main


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.amir.bagshop.model.data.AdsResponse.Ad
import com.amir.bagshop.model.data.CheckOut
import com.amir.bagshop.model.data.ProductsResponse.Product
import com.amir.bagshop.theme.Brown
import com.amir.bagshop.util.CATEGORY
import com.amir.bagshop.util.MyScreens
import com.amir.bagshop.util.NetworkChecker
import com.amir.bagshop.util.PAYMENT_PENDING
import com.amir.bagshop.util.PAYMENT_SUCCESS
import com.amir.bagshop.util.TAGS
import com.amir.bagshop.util.formatPrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import org.koin.core.parameter.parametersOf
import com.amir.bagshop.R
import com.amir.bagshop.util.NO_PAYMENT


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val context = LocalContext.current

    val isConnected = NetworkChecker(context).isInternetConnected

    val viewModel = getNavViewModel<MainViewmodel> {
        parametersOf(isConnected)
    }
    val navigation = getNavController()

    if (NetworkChecker(context).isInternetConnected) {
        viewModel.badgeNumber()
    }

    if (viewModel.getPaymentStatus() == PAYMENT_PENDING) {
        if (NetworkChecker(context).isInternetConnected) {

            viewModel.getCheckoutData()
        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            if (viewModel.showProgressBar.value)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Brown)
//------------------------------------------------------------------------------

            TopToolBar(

                onCartClick = {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.CartScreen.route)
                    } else {
                        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
                    }
                },

                onProfileClick = {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.ProfileScreen.route)
                    } else {
                        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
                    }
                },

                badgeNumber = viewModel.badgeNumber.intValue
            )
//------------------------------------------------------------------------------

            CategoryBar(CATEGORY) {
                navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
            }
//------------------------------------------------------------------------------

            val productDataState = viewModel.dataProduct
            val adsDataState = viewModel.dataAds
//------------------------------------------------------------------------------

            ProductSubjectList(TAGS, productDataState.value, adsDataState.value) {
                navigation.navigate(MyScreens.ProductScreen.route + "/" + it)

            }
//------------------------------------------------------------------------------


        }

        if (viewModel.showPaymentDialog.value) {
            PaymentResultDialog(

                checkoutResult = viewModel.checkOutData.value,
                onDismiss = {
                    viewModel.showPaymentDialog.value = false
                    viewModel.setPaymentStatus(NO_PAYMENT)

                }
            )
        }
    }
}

//------------------------------------------------------------------------------

@Composable
private fun PaymentResultDialog(
    checkoutResult: CheckOut,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            shape = ShapeDefaults.Medium
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Payment Result",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Main Data
                if (checkoutResult.order?.status?.toInt() == PAYMENT_SUCCESS) {

                    AsyncImage(
                        model = R.drawable.success_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(110.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + formatPrice(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order!!.amount).length - 1
                            )
                        )
                    )

                } else {

                    AsyncImage(
                        model = R.drawable.fail_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(top = 6.dp, bottom = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was not successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + formatPrice(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order.amount).length - 1
                            )
                        )
                    )

                }

                // Ok Button
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "ok")
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
        }
    }
}
//------------------------------------------------------------------------------

@Composable
fun ProductSubjectList(
    tags: List<String>,
    product: List<Product>,
    ads: List<Ad>,
    onProductClick: (String) -> Unit
) {


    if (product.isNotEmpty()) {

        Column {

            tags.forEachIndexed { it, _ ->

                val dataWithProduct = product.filter { Product ->

                    Product.tags == tags[it]
                }

                ProductSubject(tags[it], dataWithProduct, onProductClick)

                if (ads.size >= 2) {
                    if (it == 1 || it == 2) {
                        BigPictureTabLiGhat(ads[it - 1], onProductClick)
                    }
                }

            }
        }
    }
}

//------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolBar(
    badgeNumber: Int,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {


    TopAppBar(

        title = {
            Text(
                text = "Bag Shop",
                color = Color(70, 37, 8, 230),
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        },

        actions = {

            IconButton({ onCartClick.invoke() }) {

                if (badgeNumber == 0) {

                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Brown
                    )
                } else {
                    BadgedBox(
                        modifier = Modifier.padding(end = 8.dp),
                        badge = { Badge { Text(text = badgeNumber.toString()) } }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Brown
                        )
                    }
                }
            }

            IconButton(onClick = { onProfileClick.invoke() }) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(70, 37, 8, 230)
                )
            }

        })

}


//------------------------------------------------------------------------------

@Composable
fun CategoryBar(categoryList: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {

    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {

        items(categoryList.size) {

            CategoryItem(categoryList[it], onCategoryClicked)
        }
    }

}

@Composable
fun CategoryItem(subject: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onCategoryClicked.invoke(subject.first) }
    ) {
        Surface(
            color = Color(58, 28, 10, 20),
        ) {

            Image(
                painter = painterResource(subject.second),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)

            )
        }

        Text(
            text = subject.first,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

    }

}

//------------------------------------------------------------------------------


@Composable
fun ProductSubject(subject: String, data: List<Product>, onProductClick: (String) -> Unit) {


    Column(
        modifier = Modifier
            .padding(top = 32.dp)
    ) {
        Text(
            text = subject,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(70, 37, 8, 230),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.titleLarge
        )


        LazyRow(
            modifier = Modifier.padding(top = 16.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {

            items(data.size) {

                CardPopular(data[it], onProductClick)

            }
        }
    }
}

@Composable
fun CardPopular(product: Product, onProductClick: (String) -> Unit) {

    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onProductClick.invoke(product.productId) },
        elevation = CardDefaults.outlinedCardElevation(15.dp),

        ) {

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = product.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(220.dp)
            )

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .padding(top = 16.dp)
            )
            {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    color = Color(70, 37, 8, 230)
                )
                Text(
                    text = formatPrice(product.price) + " Tomans",
                    fontWeight = FontWeight.W400,
                    color = Color(70, 37, 8, 230)
                )
                Text(
                    text = product.soldItem + " Sold",
                    fontWeight = FontWeight.W300,
                    color = Color(70, 37, 8, 230)

                )
            }

        }

    }

}


//------------------------------------------------------------------------------


@Composable
fun BigPictureTabLiGhat(ads: Ad, onProductClick: (String) -> Unit) {

    AsyncImage(
        model = ads.imageURL,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            .clickable { onProductClick.invoke(ads.productId) }
            .clip(shape = ShapeDefaults.Medium)

    )

}


//------------------------------------------------------------------------------




