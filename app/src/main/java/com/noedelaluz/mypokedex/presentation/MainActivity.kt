package com.noedelaluz.mypokedex.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.my_navHostFragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.favoriteFragment
        ))

        initializeUI(appBarConfiguration)

        // Eliminar el action bar
        supportActionBar?.hide()

    }

    private fun initializeUI(appBarConfiguration: AppBarConfiguration) {
        initializeToolbar(appBarConfiguration)
        initializeBottomNavigation(appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailFragment) {
                bottomNavigationView.visibility = BottomNavigationView.GONE
            } else {
                bottomNavigationView.visibility = BottomNavigationView.VISIBLE
                supportActionBar?.hide()
            }
        }
    }

    private fun initializeToolbar(appBarConfiguration: AppBarConfiguration) {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun initializeBottomNavigation(appBarConfiguration: AppBarConfiguration) {
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}