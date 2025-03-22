package com.example.ads.screens

import android.net.Uri
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
fun CreateAdsScreen(navController: NavController) {
    var heading by remember { mutableStateOf(TextFieldValue()) }
    var adText by remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

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
        ImageUploader(selectedImageUri) { uri -> selectedImageUri = uri }

        TextField(
            value = heading,
            onValueChange = { heading = it },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = adText,
            onValueChange = { adText = it },
            label = { Text("Текст объявления") },
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

        val predefinedGroups = remember { listOf("Группа 1", "Группа 2", "Группа 3") }
        var selectedGroups by remember { mutableStateOf(mutableSetOf<String>()) }
        var searchQueryGroups by remember { mutableStateOf(TextFieldValue()) }
        var isDropdownExpandedGroups by remember { mutableStateOf(false) }
        val keyboardControllerGroups = LocalSoftwareKeyboardController.current
        val focusRequesterGroups = remember { FocusRequester() }

        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                OutlinedTextField(
                    value = searchQueryGroups,
                    onValueChange = {
                        searchQueryGroups = it
                        isDropdownExpandedGroups = true
                    },
                    label = { Text("Группы") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterGroups)
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                keyboardControllerGroups?.show()
                            } else {
                                isDropdownExpandedGroups = false
                            }
                        }
                )

                if (isDropdownExpandedGroups) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .shadow(4.dp, RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .heightIn(max = 200.dp)
                    ) {
                        val filteredGroups = predefinedGroups.filter {
                            it.contains(searchQueryGroups.text, ignoreCase = true)
                        }

                        items(filteredGroups) { group ->
                            DropdownMenuItem(
                                text = { Text(group) },
                                onClick = {
                                    selectedGroups = selectedGroups.toMutableSet().apply { add(group) }
                                    isDropdownExpandedGroups = false
                                    searchQueryGroups = TextFieldValue()
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }
            }
        }

        if (selectedGroups.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedGroups.forEach { group ->
                    AssistChip(
                        onClick = { selectedGroups = selectedGroups.toMutableSet().apply { remove(group) } },
                        label = { Text(group) }
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