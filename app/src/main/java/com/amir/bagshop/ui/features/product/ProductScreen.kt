package com.amir.bagshop.ui.features.product

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.amir.bagshop.model.data.ProductsResponse.Product
import com.amir.bagshop.theme.Brown
import com.amir.bagshop.theme.MainAppThem
import com.amir.bagshop.util.MyScreens
import com.amir.bagshop.util.NetworkChecker
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import com.amir.bagshop.R
import com.amir.bagshop.model.data.Comment
import com.amir.bagshop.theme.BrownBright
import com.amir.bagshop.util.formatPrice
import java.nio.file.WatchEvent


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    MainAppThem {
        ProductScreen("")
    }
}


@Composable
fun ProductScreen(id: String) {

    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getNavViewModel<ProductViewModel>()
    viewModel.loadData(id, NetworkChecker(context).isInternetConnected)



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopBar(

                productName = viewModel.dataProductById.value.material,
                badgeNumber = viewModel.badgeNumber.intValue,
                onBackClick = { navigation.popBackStack() },
                onCardClick =  {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.CartScreen.route)
                    } else {
                        Toast.makeText(context, "لطفا اینترنت خود را متصل کنید", Toast.LENGTH_SHORT)
                            .show()
                    }
                })


            val commentChecker = if (NetworkChecker(context).isInternetConnected) {
                viewModel.comments.value
            } else {
                listOf()
            }

            ProductItem(
                data = viewModel.dataProductById.value,
                onCategoryClick = { navigation.navigate(MyScreens.CategoryScreen.route + "/" + it) },
                comment = viewModel.comments.value.size.toString(),
                comments = commentChecker,
                addedComment = {
                    viewModel.addNewComment(productId = id, text = it) { payam ->
                        Toast.makeText(context, payam, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        AddToCart(viewModel.dataProductById.value.price, viewModel.showAnimation.value) {
            if (NetworkChecker(context).isInternetConnected) {
                viewModel.addProductToCart(id) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "لطفا اینترنت خود را متصل کنید.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


@Composable
fun AddToCart(
    price: String,
    animation: Boolean,
    onToCartClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(end = 12.dp, start = 12.dp, bottom = 56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {

        Button(
            { onToCartClick.invoke() },
            colors = ButtonDefaults.buttonColors(Brown), modifier = Modifier.size(180.dp, 40.dp)
        ) {

            if (animation == true) {

                DotsTyping()
            } else {
                Text("Add Product To Cart")
            }
        }
        Surface(
            modifier = Modifier
                .clip(shape = ShapeDefaults.Small)
                .padding(end = 12.dp)
                .align(Alignment.Bottom),
            color = BrownBright
        ) {
            Text(
                text = formatPrice(price)+ " Tomans",
                modifier = Modifier.padding(6.dp),
                color = Color(Brown.value)
            )
        }

    }


}

@Composable
fun DotsTyping() {

    val dotSize = 10.dp
    val delayUnit = 350
    val maxOffset = 10f

    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun ProductItem(
    data: Product,
    onCategoryClick: (String) -> Unit,
    comment: String,
    comments: List<Comment>,
    addedComment: (String) -> Unit
) {


    Column(modifier = Modifier.padding(16.dp)) {

        ProductDesign(data, onCategoryClick)

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            thickness = 2.dp,
            color = Color.LightGray
        )

        ProductDetail(data, comment)


        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            thickness = 2.dp,
            color = Color.LightGray
        )


        ProductComment(comments = comments, addNewComment = { addedComment.invoke(it) })
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun CommentBody(comment: Comment) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        border = BorderStroke(1.5.dp, BrownBright),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = comment.userEmail, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = comment.text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun ProductComment(comments: List<Comment>, addNewComment: (String) -> Unit) {

    val showCommentDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (comments.isNotEmpty()) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Comments", color = Brown,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            TextButton(onClick = {
                if (NetworkChecker(context).isInternetConnected) {
                    showCommentDialog.value = true
                } else {
                    Toast.makeText(context, "لطفا اینترنت خود را متصل کنید", Toast.LENGTH_LONG)
                        .show()
                }
            }) {
                Text(text = "Add new Comment", fontSize = 14.sp, color = Brown)
            }
        }

        comments.forEach {

            CommentBody(it)
        }


    } else {

        TextButton({
            if (NetworkChecker(context).isInternetConnected) {
                showCommentDialog.value = true
            } else {

                Toast.makeText(context, "لطفا اینترنت خود را متصل کنید", Toast.LENGTH_LONG).show()
            }

        }) {
            Text(text = "Add new Comment", fontSize = 14.sp, color = Brown)
        }

    }


    if (showCommentDialog.value) {

        AddNewCommentDialog(
            onDismiss = { showCommentDialog.value = false },
            onPositiveClick = { addNewComment.invoke(it) }
        )
    }

}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun AddNewCommentDialog(
    onDismiss: () -> Unit,
    onPositiveClick: (String) -> Unit
) {

    val context = LocalContext.current
    val userComment = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss }) {


        Card(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Write Your Comment", modifier = Modifier
                        .padding(top = 15.dp), fontWeight = FontWeight.Bold,
                    color = Brown,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = userComment.value,
                    onValueChange = { userComment.value = it },
                    label = { Text("Write Something....") },
                    placeholder = { Text("Write Something....") },
                    shape = ShapeDefaults.Medium,
                    singleLine = false,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton({ onDismiss.invoke() }) {

                        Text("Cancel", color = Brown)
                    }
                    TextButton({
                        if (userComment.value.isNotEmpty() && userComment.value.isNotBlank()) {
                            if (NetworkChecker(context).isInternetConnected) {
                                onPositiveClick.invoke(userComment.value)
                                onDismiss.invoke()
                            } else {
                                Toast.makeText(
                                    context,
                                    "لطفا اینترنت خود را متصل کنید.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "کامنت را به درستی وارد کنید.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    ) {
                        Text("Ok", color = Brown)

                    }
                }
            }
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun ProductDetail(data: Product, comments: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            val context = LocalContext.current

            val sold = if (NetworkChecker(context).isInternetConnected) {
                comments + " Comments"
            } else {
                "No Internet"
            }

            Row {
                Image(
                    painter = painterResource(R.drawable.ic_details_comment),
                    contentDescription = null
                )

                Text(
                    text = sold,
                    modifier = Modifier.padding(start = 8.dp),
                    color = Brown
                )

            }
            Row(modifier = Modifier.padding(top = 10.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_details_material),
                    contentDescription = null
                )
                Text(text = data.material, modifier = Modifier.padding(start = 8.dp), color = Brown)

            }
            Row(modifier = Modifier.padding(top = 10.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_details_sold),
                    contentDescription = null
                )
                Text(
                    text = data.soldItem + " Sold",
                    modifier = Modifier.padding(start = 8.dp),
                    color = Brown
                )

            }


        }

        Surface(
            modifier = Modifier
                .clip(shape = ShapeDefaults.Small)
                .padding(end = 12.dp)
                .align(Alignment.Bottom),
            color = BrownBright
        ) {
            Text(
                text = data.tags,
                modifier = Modifier.padding(6.dp),
                color = Color(Brown.value)
            )
        }
    }


}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun ProductDesign(product: Product, onCategoryClick: (String) -> Unit) {

    AsyncImage(
        modifier = Modifier
            .height(270.dp)
            .clip(ShapeDefaults.Medium),
        model = product.imgUrl,
        contentScale = ContentScale.Crop,
        contentDescription = null
    )

    Text(
        text = product.name, modifier = Modifier
            .padding(top = 16.dp), fontWeight = FontWeight.Bold,
        color = Brown,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.titleLarge
    )

    Text(
        text = product.detailText,
        modifier = Modifier
            .padding(top = 4.dp),
        color = Brown,
        textAlign = TextAlign.Justify
    )

    TextButton({ onCategoryClick.invoke(product.category) }) {
        Text(
            text = "#" + product.category,
            color = Color(Brown.value),
        )
    }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    productName: String,
    badgeNumber: Int,
    onBackClick: () -> Unit,
    onCardClick: () -> Unit
) {


    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {

            IconButton({ onBackClick.invoke() }) {

                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Brown)
            }
        },

        title = {
            Text(
                text = productName,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Brown
            )
        },


        actions = {

            IconButton({ onCardClick.invoke() }) {

                if (badgeNumber == 0) {

                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Brown
                    )

                } else {

                    BadgedBox(
                        modifier = Modifier.padding(end = 15.dp),
                        badge = { Badge { Text(text = badgeNumber.toString()) } }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Brown
                        )

                    }
                }

            }
        },
    )
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////