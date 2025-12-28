package com.uilover.project2132.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.uilover.project2132.Helper.ManagmentCart
import com.uilover.project2132.Model.ItemsModel
import com.uilover.project2132.R
import java.text.DecimalFormat

class DetailActivity : BaseActivity() {
    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = intent.getSerializableExtra("object") as ItemsModel
        managmentCart = ManagmentCart(this)

        setContent {
            DetailScreen(
                item = item,
                onBackClick = { finish() },
                onAddToCartClick = { size, color ->
                    item.numberInCart = 1
                    item.selectedSize = size
                    item.selectedColor = color
                    managmentCart.insertItem(item)
                },
                onCartClick = {
                    startActivity(Intent(this, CartActivity::class.java))
                }
            )
        }
    }

    @Composable
    private fun DetailScreen(
        item: ItemsModel,
        onBackClick: () -> Unit,
        onAddToCartClick: (String, String) -> Unit,  // size, color
        onCartClick: () -> Unit
    ) {
        val context = LocalContext.current

        // Create list of images from main image and gallery
        val imageList = buildList {
            add(item.image)  // Main image first
            if (item.product_gallery.img1.isNotEmpty()) add(item.product_gallery.img1)
            if (item.product_gallery.img2.isNotEmpty()) add(item.product_gallery.img2)
            if (item.product_gallery.img3.isNotEmpty()) add(item.product_gallery.img3)
        }

        var selectedImageUrl by remember { mutableStateOf(imageList.firstOrNull() ?: "") }
        var selectedSize by remember { mutableStateOf("") }
        var selectedColor by remember { mutableStateOf("") }
        // Removed selectedModelIndex - not needed in new database structure

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
                    .padding(bottom = 16.dp)
            ) {
                val (back, fav, mainImage, thumbnail) = createRefs()
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            colorResource(R.color.lightBrown),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .constrainAs(mainImage) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }
                )
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 48.dp, start = 16.dp)
                        .clickable { onBackClick() }
                        .constrainAs(back) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                )

                LazyRow(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .background(
                            color = colorResource(R.color.white),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .constrainAs(thumbnail) {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }
                ) {
                    items(imageList) { imageUrl ->
                        ImageThumbnail(
                            imageUrl = imageUrl,
                            isSelected = selectedImageUrl == imageUrl,
                            onClick = { selectedImageUrl = imageUrl }

                        )

                    }
                }


            }

            // Product Title
            Text(
                text = item.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C2C2C),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )

            // Price Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp)
            ) {
                val formatter = DecimalFormat("#,###")
                Text(
                    text = "${formatter.format(item.price.toLong())} ₫",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkBrown)
                )
            }

            RatingBar(rating = item.rated)  // Changed from item.rating to item.rated

            // Size Selector
            SizeSelector(
                selectedSize = selectedSize,
                onSizeSelected = { selectedSize = it }
            )

            // Color Selector
            ColorSelector(
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it }
            )

            // ModelSelector removed - new database doesn't have model/variants field

            Text(
                text = item.description,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.background(
                        colorResource(R.color.lightBrown),
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_2),
                        contentDescription = "Giỏ hàng",
                        tint = Color.Black
                    )
                }
                Button(
                    onClick = {
                        if (selectedSize.isEmpty() || selectedColor.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Vui lòng chọn kích thước và màu sắc",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onAddToCartClick(selectedSize, selectedColor)
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.darkBrown)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(50.dp)
                ) {
                    Text(text = "Thêm Vào Giỏ", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }

    private @Composable
    fun ModelSelector(
        models: ArrayList<String>,
        selectedModeIndex: Int,
        onModelSelected: (Int) -> Unit
    ) {
        LazyRow(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            itemsIndexed(models) { index, model ->
                Box(modifier = Modifier
                    .padding(end = 16.dp)
                    .height(40.dp)
                    .then(
                        if (index == selectedModeIndex) {
                            Modifier.border(
                                1.dp,
                                colorResource(R.color.darkBrown),
                                RoundedCornerShape(10.dp)
                            )
                        } else {
                            Modifier.border(
                                1.dp,
                                colorResource(R.color.darkBrown),
                                RoundedCornerShape(10.dp)
                            )
                        }
                    )
                    .background(
                        if (index == selectedModeIndex) colorResource(R.color.darkBrown) else
                            colorResource(R.color.white),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onModelSelected(index) }
                    .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = model,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (index == selectedModeIndex) colorResource(R.color.white)
                        else colorResource(R.color.black),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    @Composable
    private fun RatingBar(rating: Double) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Chọn Mẫu",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "$rating Đánh Giá", style = MaterialTheme.typography.bodyMedium)
        }
    }

    private @Composable
    fun ImageThumbnail(imageUrl: String, isSelected: Boolean, onClick: () -> Unit) {
        val backColor = if (isSelected) colorResource(R.color.darkBrown) else
            colorResource(R.color.veryLightBrown)

        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(55.dp)
                .then(
                    if (isSelected) {
                        Modifier.border(
                            1.dp,
                            colorResource(R.color.darkBrown),
                            RoundedCornerShape(10.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .background(backColor, shape = RoundedCornerShape(10.dp))
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    private fun SizeSelector(
        selectedSize: String,
        onSizeSelected: (String) -> Unit
    ) {
        val sizes = listOf("S", "M", "L", "XL")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Kích thước",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                items(sizes) { size ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                if (size == selectedSize) colorResource(R.color.darkBrown)
                                else Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (size == selectedSize) 2.dp else 1.dp,
                                color = if (size == selectedSize) colorResource(R.color.darkBrown)
                                else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSizeSelected(size) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = size,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (size == selectedSize) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ColorSelector(
        selectedColor: String,
        onColorSelected: (String) -> Unit
    ) {
        val colors = mapOf(
            "Đỏ" to Color(0xFFE53935),
            "Trắng" to Color(0xFFFFFFFF),
            "Đen" to Color(0xFF000000),
            "Nâu" to Color(0xFF795548)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Màu sắc",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                items(colors.keys.toList()) { colorName ->
                    val colorValue = colors[colorName] ?: Color.Gray

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    colorValue,
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .border(
                                    width = if (colorName == selectedColor) 3.dp else 2.dp,
                                    color = if (colorName == selectedColor) colorResource(R.color.darkBrown)
                                    else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .clickable { onColorSelected(colorName) }
                        )

                        if (colorName == selectedColor) {
                            Text(
                                text = "✓",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.darkBrown),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}