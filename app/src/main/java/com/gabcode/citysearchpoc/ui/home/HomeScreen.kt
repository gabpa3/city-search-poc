package com.gabcode.citysearchpoc.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gabcode.citysearchpoc.ui.MainViewModel
import com.gabcode.citysearchpoc.ui.UIState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: MainViewModel, navToMap: () -> Unit) {
    val searchQuery by viewModel.query.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()
    val dataLoadedState by viewModel.citiesLoaded.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        SearchBar(queryState = searchQuery, enabled = dataLoadedState.isSuccess) { newQuery ->
            viewModel.onQueryChange(newQuery)
        }

        when (dataLoadedState) {
            UIState.Initial -> {
                GuideText(message = "Awaiting data load")
            }
            UIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            is UIState.Success -> {
                AnimatedVisibility(
                    visible = searchResult.isEmpty(),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enter = fadeIn(),
                    exit = fadeOut()
                ){
                    GuideText(message = "Introduce your city in the search bar")
                }
                AnimatedVisibility(
                    visible = searchResult.isNotEmpty(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        onClick = navToMap,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("See in Map")
                    }
                }

                LazyColumn {
                    items(searchResult, key = { it.id }) { item ->
                        CityItem(
                            name = item.name,
                            country = item.country,
                            isFavorite = item.isFavorite
                        ) {
                            viewModel.toggleFavorite(item)
                        }
                    }
                }
            }
            is UIState.Error -> {
                // TODO show error dialog
            }
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    queryState: String,
    enabled: Boolean,
    onInputChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = queryState,
        enabled = enabled,
        onValueChange = { onInputChange(it) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        placeholder = { Text("Search...") },
        singleLine = true,
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun CityItem(
    name: String,
    country: String,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = country,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        FavoriteButton(enabled = isFavorite) {
            onToggleFavorite()
        }
    }
}

@Composable
private fun FavoriteButton(enabled: Boolean, onClick: () -> Unit) {
    val favoriteState by remember { mutableStateOf(enabled) }
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            imageVector = if (favoriteState) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun GuideText(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = message, modifier = Modifier.align(Alignment.Center))
    }
}

@Preview
@Composable
private fun CitiItemPreview() {
    CityItem(name = "Ciudad Autonoma de Buenos Aires", country = "AR", isFavorite = false) { }
}
