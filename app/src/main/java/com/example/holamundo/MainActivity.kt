package com.example.holamundo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity principal que muestra el texto "Hola Mundo :)".
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
