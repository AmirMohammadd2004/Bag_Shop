package com.amir.bagshop.ui.features.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.amir.bagshop.model.data.ProductsResponse.Product
import com.amir.bagshop.theme.Brown
import com.amir.bagshop.theme.BrownBright
import com.amir.bagshop.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel


@Composable
fun CategoryScreen(categoryName: String) {

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    val navigation = getNavController()
    val viewModel = getNavViewModel<CategoryViewModel>()
    viewModel.getDataFromLocal(categoryName)
    val data = viewModel.dataFromLocal.value
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopBar(categoryName)

        CategoryList(data) {

            navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
        }

    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun CategoryList(data: List<Product>, onProductClick: (String) -> Unit) {


    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        items(data.size) {

            ItemCard(data[it], onProductClick)
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


@Composable
fun ItemCard(data: Product, onProductClick: (String) -> Unit) {


    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .clip(shape = ShapeDefaults.Large)
            .background(Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
        onClick = { onProductClick.invoke(data.productId) },
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

                    Text(
                        text = data.name,
                        fontSize = 18.sp,
                        color = Brown
                    )
                    Text(
                        text = data.price + " tomans",
                        fontSize = 15.sp,
                        color = Brown
                    )
                }

                Surface(
                    modifier = Modifier
                        .clip(shape = ShapeDefaults.Large)
                        .padding(bottom = 12.dp, end = 12.dp),
                    color = BrownBright
                ) {
                    Text(
                        text = data.soldItem + " Sold",
                        modifier = Modifier.padding(8.dp),
                        color = Color(Brown.value)
                    )
                }

            }

        }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(categoryName: String) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = categoryName,
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                    fontWeight = FontWeight.Bold,
                    color = Brown,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
    )
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
