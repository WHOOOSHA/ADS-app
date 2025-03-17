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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ads.data.Ad
import com.example.ads.data.Group
import com.example.ads.R

@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    var expandedList by remember { mutableStateOf<ListType?>(null) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Поле поиска
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Поиск...") },
                trailingIcon = {
                    IconButton(onClick = { /* Действие при нажатии на лупу */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Список результатов поиска
            SearchResults(
                searchQuery = searchQuery.text,
                expandedList = expandedList,
                onExpand = { expandedList = it },
                onCollapse = { expandedList = null }
            )
        }

        // Чёрная кнопка "Назад" внизу слева
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 32.dp, start = 16.dp)
                .size(48.dp)
                .background(Color.Black, shape = RoundedCornerShape(12.dp))
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Назад",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SearchResults(
    searchQuery: String,
    expandedList: ListType?,
    onExpand: (ListType) -> Unit,
    onCollapse: () -> Unit
) {
    val ads = remember { List(100) { Ad("Автор", "Группа", R.drawable.avatar_placeholder, R.drawable.ad_image_placeholder, "Объявление ${it + 1}", "Описание объявления") } }
    val groups = remember { List(100) { Group("Группа ${it + 1}", "Описание", "Город", listOf("Категория"), R.drawable.avatar_placeholder, listOf("Модератор")) } }

    val displayedAds = remember { mutableStateListOf<Ad>() }
    val displayedGroups = remember { mutableStateListOf<Group>() }

    LaunchedEffect(Unit) {
        displayedAds.addAll(ads.take(5))
        displayedGroups.addAll(groups.take(5))
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Объявления
        if (expandedList == null || expandedList == ListType.ADS) {
            item { ListHeader("Объявления", expandedList == ListType.ADS, { onExpand(ListType.ADS) }, onCollapse) }
            val adsToShow = if (expandedList == ListType.ADS) displayedAds else displayedAds.take(2) // Теперь в свернутом состоянии только 2
            items(adsToShow) { AdItem(it) }
            if (expandedList == ListType.ADS && displayedAds.size < ads.size) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(onClick = { loadMore(displayedAds, ads) }) {
                            Text("Загрузить ещё")
                        }
                    }
                }
            }
        }

        // Группы
        if (expandedList == null || expandedList == ListType.GROUPS) {
            item { ListHeader("Группы", expandedList == ListType.GROUPS, { onExpand(ListType.GROUPS) }, onCollapse) }
            val groupsToShow = if (expandedList == ListType.GROUPS) displayedGroups else displayedGroups.take(5)
            items(groupsToShow) { GroupItem(it) }
            if (expandedList == ListType.GROUPS && displayedGroups.size < groups.size) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(onClick = { loadMore(displayedGroups, groups) }) {
                            Text("Загрузить ещё")
                        }
                    }
                }
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
            .clickable { /* Действие при нажатии на группу */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = group.image),
            contentDescription = "Аватарка группы",
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = group.name, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ListHeader(title: String, isExpanded: Boolean, onExpand: () -> Unit, onCollapse: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { if (isExpanded) onCollapse() else onExpand() },
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

fun <T> loadMore(displayedList: MutableList<T>, fullList: List<T>) {
    val nextItems = fullList.drop(displayedList.size).take(10)
    displayedList.addAll(nextItems)
}

enum class ListType {
    ADS, GROUPS
}
