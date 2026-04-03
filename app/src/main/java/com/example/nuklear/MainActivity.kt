package com.example.nuklear

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.nuklear.ui.theme.NuKlearTheme
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    // Injecting the ViewModel for Auth logic
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NuKlearTheme {
                // Collect the Supabase session status as a Compose State
                val sessionStatus by Supabase.client.auth.sessionStatus.collectAsState(
                    initial = SessionStatus.Initializing
                )
                val context = androidx.compose.ui.platform.LocalContext.current
                val message by authViewModel.successMessage

                LaunchedEffect(message) {
                    message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        // Reset the message after showing
                        authViewModel.successMessage.value = null
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (sessionStatus) {
                        is SessionStatus.Authenticated -> {
                            // User is logged in, show the main navigation
                            NuClearAppContent(authViewModel)
                        }
                        is SessionStatus.Initializing -> {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(modifier = Modifier.size(50.dp))
                            }
                        }
                        else -> {
                            // Not logged in (SessionStatus.NotAuthenticated)
                            AuthScreen(
                                isLoading = authViewModel.isLoading.value,
                                onAuthAction = { email, pass, isSignUp ->
                                    authViewModel.handleAuth(email, pass, isSignUp) {
                                        // Success is handled by the sessionStatus collector above
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class AppDestinations(val label: String, val icon: Int) {
    HOME("Home", R.drawable.ic_home),
    FAVORITES("Favorites", R.drawable.ic_favorite),
    PROFILE("Profile", R.drawable.ic_account_box),
}

@Composable
fun NuClearAppContent(authViewModel: AuthViewModel) {
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
                    icon = { Icon(painterResource(destination.icon), destination.label) },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination },
                    colors = myItemColors
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues: PaddingValues ->
            val contentModifier = Modifier.padding(paddingValues)

            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen(contentModifier)
                AppDestinations.FAVORITES -> FavoritesScreen(contentModifier)
                AppDestinations.PROFILE -> ProfileScreen(
                    modifier = contentModifier,
                    onLogout = { authViewModel.logout() } // Pass logout to profile
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NuClearAppPreview() {
    NuKlearTheme {
        NuClearAppContent(authViewModel = AuthViewModel())
    }
}