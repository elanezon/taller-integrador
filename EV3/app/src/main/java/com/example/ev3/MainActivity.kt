package com.example.ev3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ev3.databinding.ActivityMainBinding
import com.example.ev3.ui.usuario.UsuarioViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Recibir el correo desde LoginActivity
        val userEmail = intent.getStringExtra("USER_EMAIL")

        // Obtener el UsuarioViewModel
        val usuarioViewModel = ViewModelProvider(this)[UsuarioViewModel::class.java]

        // Pasar el correo al ViewModel
        userEmail?.let {
            usuarioViewModel.setUserEmail(it)
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_inicio, R.id.navigation_carga, R.id.navigation_informacion, R.id.navigation_usuario
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}