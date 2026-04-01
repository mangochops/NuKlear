package com.example.nuklear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.nuklear.ui.theme.NuKlearTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NuKlearTheme {
                NuClearAppContent()
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Home", R.drawable.ic_home),
    FAVORITES("Favorites", R.drawable.ic_favorite),
    PROFILE("Profile", R.drawable.ic_account_box),
}

@Composable
fun NuClearAppContent() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    val myItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = colorResource(id = R.color.light_blue_400),
            selectedIconColor = colorResource(id = R.color.white),
            selectedTextColor = colorResource(id = R.color.light_blue_600),
            unselectedIconColor = colorResource(id = R.color.gray_600),
            unselectedTextColor = colorResource(id = R.color.gray_600)
        )
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination },
                    colors = myItemColors
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            val contentModifier = Modifier.padding(innerPadding)

            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen(contentModifier)
                AppDestinations.FAVORITES -> FavoritesScreen(contentModifier)
                AppDestinations.PROFILE -> ProfileScreen(contentModifier)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NuClearAppPreview() {
    NuKlearTheme {
        NuClearAppContent()
    }
}