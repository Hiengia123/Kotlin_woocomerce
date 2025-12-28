package com.uilover.project2132.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.uilover.project2132.Helper.ChangeNumberItemsListener
import com.uilover.project2132.Helper.ManagmentCart
import com.uilover.project2132.Model.ItemsModel
import com.uilover.project2132.R
import java.text.DecimalFormat

class CartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CartScreen(ManagmentCart(this),
                onBackClick = {
                    finish()
                })
        }
    }
}

fun calculatorCart(managmentCart: ManagmentCart, tax: MutableState<Double>) {
    val percentTax = 0.02
    tax.value = Math.round((managmentCart.getTotalFee() * percentTax) * 100) / 100.0
}

@Composable
private fun CartScreen(
    managmentCart: ManagmentCart = ManagmentCart(LocalContext.current),
    onBackClick: () -> Unit
) {
    val cartItems = remember { mutableStateOf(managmentCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) }
    calculatorCart(managmentCart, tax)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header Section
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            val (backBtn, cartTxt) = createRefs()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cartTxt) { centerTo(parent) },
                text = "🛒 Giỏ Hàng Của Bạn",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = colorResource(R.color.darkBrown)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        colorResource(R.color.lightBrown),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onBackClick() }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "Quay lại",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        if (cartItems.value.isEmpty()) {
            // Empty Cart State
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Text(
                    text = "🛍️",
                    fontSize = 64.sp
                )
                Text(
                    text = "Giỏ hàng trống",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Hãy thêm sản phẩm vào giỏ hàng nhé!",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                CartList(cartItems = cartItems.value, managmentCart) {
                    cartItems.value = managmentCart.getListCart()
                    calculatorCart(managmentCart, tax)
                }
                CartSummary(
                    itemTotal = managmentCart.getTotalFee(),
                    tax = tax.value,
                    delivery = 10.0
                )
            }
        }
    }
}

@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val total = itemTotal + tax + delivery
    val formatter = DecimalFormat("#,###")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "Chi Tiết Thanh Toán",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.darkBrown),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Subtotal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Tổng sản phẩm",
                Modifier.weight(1f),
                fontSize = 15.sp,
                color = Color.Gray
            )
            Text(
                text = "${formatter.format(itemTotal.toLong())} ₫",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        // Tax
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Thuế (2%)",
                Modifier.weight(1f),
                fontSize = 15.sp,
                color = Color.Gray
            )
            Text(
                text = "${formatter.format(tax.toLong())} ₫",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        // Delivery
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Giao hàng",
                Modifier.weight(1f),
                fontSize = 15.sp,
                color = Color.Gray
            )
            Text(
                text = "${formatter.format(delivery.toLong())} ₫",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE0E0E0))
                .padding(vertical = 12.dp)
        )

        // Total
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .background(
                    Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Tổng Cộng",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.darkBrown)
            )
            Text(
                text = "${formatter.format(total.toLong())} ₫",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(R.color.darkBrown)
            )
        }

        // Checkout Button
        Button(
            onClick = {},
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.darkBrown)
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "💳 Thanh Toán Ngay",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun CartList(
    cartItems: ArrayList<ItemsModel>,
    managmentCart: ManagmentCart,
    onItemChange: () -> Unit
) {
    LazyColumn(Modifier.padding(top = 16.dp)) {
        items(cartItems) { item ->
            CartItem(
                cartItems,
                item = item,
                managmentCart = managmentCart,
                onItemChange = onItemChange
            )
        }
    }
}

@Composable
fun CartItem(
    cartItems: ArrayList<ItemsModel>,
    item: ItemsModel,
    managmentCart: ManagmentCart,
    onItemChange: () -> Unit
) {
    val formatter = DecimalFormat("#,###")
    val totalPrice = item.numberInCart * item.price

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Product Image
        Image(
            painter = rememberAsyncImagePainter(item.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .background(
                    Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp)
        )

        // Product Info Column
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            // Product Title
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 2,
                lineHeight = 20.sp
            )

            // Size and Color Display
            if (item.selectedSize.isNotEmpty() || item.selectedColor.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(
                            Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.selectedSize.isNotEmpty()) {
                        Text(
                            text = "Size: ${item.selectedSize}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (item.selectedSize.isNotEmpty() && item.selectedColor.isNotEmpty()) {
                        Text(
                            text = " | ",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    if (item.selectedColor.isNotEmpty()) {
                        Text(
                            text = "Màu: ${item.selectedColor}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Price Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                // Total Price (prominent)
                Text(
                    text = "${formatter.format(totalPrice.toLong())} ₫",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkBrown)
                )
            }

            // Quantity Controls
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Minus Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            managmentCart.minusItem(
                                cartItems,
                                cartItems.indexOf(item),
                                object : ChangeNumberItemsListener {
                                    override fun onChanged() {
                                        onItemChange()
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "−",
                        color = colorResource(R.color.darkBrown),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Quantity Number
                Text(
                    text = item.numberInCart.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                // Plus Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            colorResource(R.color.darkBrown),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            managmentCart.plusItem(
                                cartItems,
                                cartItems.indexOf(item),
                                object : ChangeNumberItemsListener {
                                    override fun onChanged() {
                                        onItemChange()
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
