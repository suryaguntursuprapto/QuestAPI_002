package com.example.mysql_20220140002.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mysql_20220140002.ui.view.DestinasiEntry
import com.example.mysql_20220140002.ui.view.DestinasiHome
import com.example.mysql_20220140002.ui.view.DestinasiUpdate
import com.example.mysql_20220140002.ui.view.DetailScreen
import com.example.mysql_20220140002.ui.view.EntryMhsScreen
import com.example.mysql_20220140002.ui.view.HomeScreen
import com.example.mysql_20220140002.ui.view.UpdateMhsScreen

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier,
    ) {
        // Halaman Home
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToltemEntry = { navController.navigate(DestinasiEntry.route) },
                onDetailClick = { nim ->
                    navController.navigate("detail_screen/$nim")
                }
            )
        }

        // Halaman Entry Mahasiswa
        composable(DestinasiEntry.route) {
            EntryMhsScreen(
                navigateBack = {
                    navController.navigate(DestinasiHome.route) {
                        popUpTo(DestinasiHome.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Halaman Detail Mahasiswa
        composable("detail_screen/{nim}") { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: ""
            DetailScreen(
                nim = nim,
                onUpdate = { nim -> navController.navigate("update_screen/$nim") },
                onBack = { navController.navigateUp() }
            )
        }


        // Halaman Update Mahasiswa
        composable("${DestinasiUpdate.route}/{nim}") { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: ""
            UpdateMhsScreen(
                nim = nim,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
