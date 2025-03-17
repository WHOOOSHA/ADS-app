package com.example.ads.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.interaction.MutableInteractionSource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateGroupScreen(navController: NavController) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var city by remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageUploader()

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Город") },
            modifier = Modifier.fillMaxWidth()
        )

        val predefinedCategories = remember { listOf("Бизнес", "Образование", "Развлечения") }
        var selectedCategories by remember { mutableStateOf(mutableSetOf<String>()) }
        var searchQueryCategories by remember { mutableStateOf(TextFieldValue()) }
        var isDropdownExpandedCategories by remember { mutableStateOf(false) }
        val keyboardControllerCategories = LocalSoftwareKeyboardController.current
        val focusRequesterCategories = remember { FocusRequester() }

        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                OutlinedTextField(
                    value = searchQueryCategories,
                    onValueChange = {
                        searchQueryCategories = it
                        isDropdownExpandedCategories = true
                    },
                    label = { Text("Категории") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterCategories)
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                keyboardControllerCategories?.show()
                            } else {
                                isDropdownExpandedCategories = false
                            }
                        }
                )

                if (isDropdownExpandedCategories) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .shadow(4.dp, RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .heightIn(max = 200.dp)
                    ) {
                        val filteredCategories = predefinedCategories.filter {
                            it.contains(searchQueryCategories.text, ignoreCase = true)
                        }

                        items(filteredCategories) { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategories = selectedCategories.toMutableSet().apply { add(category) }
                                    isDropdownExpandedCategories = false
                                    searchQueryCategories = TextFieldValue()
                                    focusManager.clearFocus()
                                }
                            )
                        }

                        if (searchQueryCategories.text.isNotBlank() && searchQueryCategories.text !in predefinedCategories) {
                            item {
                                DropdownMenuItem(
                                    text = { Text("Добавить: \"${searchQueryCategories.text}\"") },
                                    onClick = {
                                        val newCategory = searchQueryCategories.text
                                        selectedCategories = selectedCategories.toMutableSet().apply { add(newCategory) }
                                        isDropdownExpandedCategories = false
                                        searchQueryCategories = TextFieldValue()
                                        focusManager.clearFocus()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (selectedCategories.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedCategories.forEach { category ->
                    AssistChip(
                        onClick = { selectedCategories = selectedCategories.toMutableSet().apply { remove(category) } },
                        label = { Text(category) }
                    )
                }
            }
        }

        val predefinedUser = remember { listOf("Пользователь 1", "Пользователь 2", "Пользователь 3") }
        var selectedUser by remember { mutableStateOf(mutableSetOf<String>()) }
        var searchQueryUser by remember { mutableStateOf(TextFieldValue()) }
        var isDropdownExpandedUser by remember { mutableStateOf(false) }
        val keyboardControllerUser = LocalSoftwareKeyboardController.current
        val focusRequesterUser = remember { FocusRequester() }

        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                OutlinedTextField(
                    value = searchQueryUser,
                    onValueChange = {
                        searchQueryUser = it
                        isDropdownExpandedUser = true
                    },
                    label = { Text("Модераторы") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterUser)
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                keyboardControllerUser?.show()
                            } else {
                                isDropdownExpandedUser = false
                            }
                        }
                )

                if (isDropdownExpandedUser) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .shadow(4.dp, RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .heightIn(max = 200.dp)
                    ) {
                        val filteredUser = predefinedUser.filter {
                            it.contains(searchQueryUser.text, ignoreCase = true)
                        }

                        items(filteredUser) { user ->
                            DropdownMenuItem(
                                text = { Text(user) },
                                onClick = {
                                    selectedUser = selectedUser.toMutableSet().apply { add(user) }
                                    isDropdownExpandedUser = false
                                    searchQueryUser = TextFieldValue()
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }
            }
        }

        if (selectedUser.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedUser.forEach { user ->
                    AssistChip(
                        onClick = { selectedUser = selectedUser.toMutableSet().apply { remove(user) } },
                        label = { Text(user) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Отменить")
            }
            Button(onClick = { /* Действие при завершении */ }) {
                Text("Завершить")
            }
        }
    }
}
