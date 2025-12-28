package com.uilover.project2132.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.uilover.project2132.Helper.SearchHelper
import com.uilover.project2132.R
import com.uilover.project2132.ViewModel.MainViewModel

class ListItemsActivity : BaseActivity() {
    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getStringExtra("id") ?: ""
        title = intent.getStringExtra("title") ?: ""

        setContent {
            ListItemScreen(
                title = title,
                onBackClick = { finish() },
                viewModel = viewModel,
                id = id

            )
        }
    }

    @Composable
    private fun ListItemScreen(
        title: String,
        onBackClick: () -> Unit,
        viewModel: MainViewModel,
        id: String
    ) {
        val items by viewModel.loadFiltered(id).observeAsState(emptyList())
        var isLoading by remember { mutableStateOf(true) }
        var searchQuery by remember { mutableStateOf("") }
        var selectedPriceFilter by remember { mutableStateOf<PriceRange?>(null) }

        // Filter items based on search query (only within this brand)
        val searchFilteredItems = remember(searchQuery, items.size) {
            SearchHelper.searchProducts(items, searchQuery)
        }

        // Apply price filter on top of search filter
        val filteredItems = remember(selectedPriceFilter, searchFilteredItems.size) {
            if (selectedPriceFilter == null) {
                searchFilteredItems
            } else {
                searchFilteredItems.filter { item ->
                    item.price >= selectedPriceFilter!!.min &&
                    item.price <= selectedPriceFilter!!.max
                }
            }
        }

        LaunchedEffect(id) {
            viewModel.loadFiltered(id)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Header with Back Button and Title
            ConstraintLayout(
                modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)
            ) {
                val (backBtn, cartTxt) = createRefs()

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(cartTxt) { centerTo(parent) },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    text = title
                )

                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "Quay lại",
                    modifier = Modifier
                        .clickable {
                            onBackClick()
                        }
                        .constrainAs(backBtn) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                )
            }

            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Icon
                Image(
                    painter = painterResource(R.drawable.search_icon),
                    contentDescription = "Tìm kiếm",
                    modifier = Modifier.padding(end = 8.dp)
                )

                // Search Input
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    "Tìm kiếm sản phẩm $title...",
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                // Clear button (show when there's text)
                if (searchQuery.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                colorResource(R.color.lightBrown),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable { searchQuery = "" }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✕",
                            color = colorResource(R.color.darkBrown),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Price Filter Chips
            PriceFilterChips(
                selectedPriceFilter = selectedPriceFilter,
                onPriceFilterSelected = { selectedPriceFilter = it }
            )

            // Results Count (only show when searching or filtering)
            if (searchQuery.isNotEmpty() || selectedPriceFilter != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tìm thấy ${filteredItems.size} sản phẩm",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.darkBrown),
                        modifier = Modifier.weight(1f)
                    )

                    // Clear all filters button
                    if (searchQuery.isNotEmpty() || selectedPriceFilter != null) {
                        Text(
                            text = "Xóa bộ lọc",
                            fontSize = 13.sp,
                            color = colorResource(R.color.darkBrown),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable {
                                    searchQuery = ""
                                    selectedPriceFilter = null
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }

            // Product List
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredItems.isEmpty() && (searchQuery.isNotEmpty() || selectedPriceFilter != null)) {
                // No search/filter results
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "😕",
                            fontSize = 64.sp
                        )
                        Text(
                            text = "Không tìm thấy sản phẩm",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = "Thử từ khóa khác trong $title",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                ListItemsFullSize(filteredItems)
            }
        }
        LaunchedEffect(items) {
            isLoading = items.isEmpty()
        }
    }
}

// Price range data class
data class PriceRange(
    val label: String,
    val min: Double,
    val max: Double
)

// Price filter chips composable
@Composable
fun PriceFilterChips(
    selectedPriceFilter: PriceRange?,
    onPriceFilterSelected: (PriceRange?) -> Unit
) {
    val priceRanges = listOf(
        PriceRange("Dưới 500K", 0.0, 500000.0),
        PriceRange("500K - 1TR", 500000.0, 1000000.0),
        PriceRange("1TR - 2TR", 1000000.0, 2000000.0),
        PriceRange("2TR - 5TR", 2000000.0, 5000000.0),
        PriceRange("Trên 5TR", 5000000.0, Double.MAX_VALUE)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        items(priceRanges.size) { index ->
            val range = priceRanges[index]
            FilterChip(
                selected = selectedPriceFilter == range,
                onClick = {
                    onPriceFilterSelected(
                        if (selectedPriceFilter == range) null else range
                    )
                },
                label = {
                    androidx.compose.material3.Text(
                        text = range.label,
                        fontSize = 13.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colorResource(R.color.darkBrown),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFFF5F5F5),
                    labelColor = Color.Black
                )
            )
        }
    }
}
