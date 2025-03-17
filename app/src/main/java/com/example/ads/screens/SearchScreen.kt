package com.example.ads.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ads.data.Ad
import com.example.ads.data.Group
import com.example.ads.data.ListType
import com.example.ads.viewModels.SearchViewModel

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = viewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val ads by viewModel.visibleAds.collectAsState()
    val groups by viewModel.visibleGroups.collectAsState()
    val expandedList by viewModel.expandedList.collectAsState()
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { focusManager.clearFocus() }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = { Text("Поиск...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Поиск") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            SearchResults(
                ads = ads,
                groups = groups,
                expandedList = expandedList,
                onToggleList = viewModel::toggleList,
                onLoadMoreAds = viewModel::loadMoreAds,
                onLoadMoreGroups = viewModel::loadMoreGroups
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 32.dp, start = 16.dp)
                .size(48.dp)
                .background(Color.Black, shape = RoundedCornerShape(12.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
        }
    }
}

@Composable
fun SearchResults(
    ads: List<Ad>,
    groups: List<Group>,
    expandedList: ListType?,
    onToggleList: (ListType) -> Unit,
    onLoadMoreAds: () -> Unit,
    onLoadMoreGroups: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        when (expandedList) {
            null -> {
                item {
                    ListHeader(
                        title = "Объявления",
                        isExpanded = false,
                        onToggle = { onToggleList(ListType.ADS) }
                    )
                }
                items(ads) { AdItem(it) }

                item {
                    ListHeader(
                        title = "Группы",
                        isExpanded = false,
                        onToggle = { onToggleList(ListType.GROUPS) }
                    )
                }
                items(groups) { GroupItem(it) }
            }

            ListType.ADS -> {
                item {
                    ListHeader(
                        title = "Объявления",
                        isExpanded = true,
                        onToggle = { onToggleList(ListType.ADS) }
                    )
                }
                items(ads) { AdItem(it) }
                item { LoadMoreButton(onLoadMoreAds) }
            }

            ListType.GROUPS -> {
                item {
                    ListHeader(
                        title = "Группы",
                        isExpanded = true,
                        onToggle = { onToggleList(ListType.GROUPS) }
                    )
                }
                items(groups) { GroupItem(it) }
                item { LoadMoreButton(onLoadMoreGroups) }
            }
        }
    }
}

@Composable
fun GroupItem(group: Group) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* Действие при нажатии */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = group.image), contentDescription = "Аватарка", modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = group.name, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ListHeader(title: String, isExpanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onToggle() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
            contentDescription = if (isExpanded) "Свернуть" else "Развернуть"
        )
    }
}

@Composable
fun LoadMoreButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(onClick = onClick) { Text("Загрузить ещё") }
    }
}
