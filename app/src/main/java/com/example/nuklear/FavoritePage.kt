package com.example.nuklear

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nuklear.ui.theme.NuKlearTheme

@Composable
fun FavoritesScreen(
    viewModel: StationViewModel,
    modifier: Modifier = Modifier) {
    // For now, let's pretend these IDs were saved as favorites
    // In a real app, this would come from a Room database or your Rails backend

    val favoriteStations = viewModel.favoriteStations

    if (favoriteStations.isEmpty()) {
        
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "No Favorites Yet",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Stations you heart will appear here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "Your Saved Stations",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(favoriteStations, key = { it.id }) { station ->
                // Reusing the StationCard from your HomeScreen
                StationCard(
                    station = station,
                    onFavoriteClick = { viewModel.toggleFavorite(station.id)}
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Favorites - Empty State")
@Composable
fun FavoritesScreenEmptyPreview() {
    NuKlearTheme {
        // We pass a dummy modifier for the preview
        FavoritesScreen(viewModel = StationViewModel())
    }
}
