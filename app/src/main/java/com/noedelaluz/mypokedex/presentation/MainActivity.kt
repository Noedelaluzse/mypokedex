package com.noedelaluz.mypokedex.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.my_navHostFragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.favoriteFragment
        ))

        initializeToolbar(appBarConfiguration)
        initializeNavPill()

        // Eliminar el action bar
        supportActionBar?.hide()
    }

    private fun initializeToolbar(appBarConfiguration: AppBarConfiguration) {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun initializeNavPill() {
        binding.tabHome.setOnClickListener { navigateToTab(R.id.homeFragment) }
        binding.tabFavorites.setOnClickListener { navigateToTab(R.id.favoriteFragment) }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailFragment) {
                binding.navPill.isVisible = false
            } else {
                binding.navPill.isVisible = true
                supportActionBar?.hide()
                selectTab(destination.id)
            }
        }
    }

    private fun navigateToTab(destinationId: Int) {
        if (navController.currentDestination?.id == destinationId) return
        navController.navigate(
            destinationId,
            null,
            navOptions {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) { saveState = true }
            }
        )
    }

    private fun selectTab(destinationId: Int) {
        val homeSelected = destinationId == R.id.homeFragment
        val favoritesSelected = destinationId == R.id.favoriteFragment
        binding.tabHome.isSelected = homeSelected
        binding.tabHomeLabel.isVisible = homeSelected
        binding.tabFavorites.isSelected = favoritesSelected
        binding.tabFavoritesLabel.isVisible = favoritesSelected
    }
}
