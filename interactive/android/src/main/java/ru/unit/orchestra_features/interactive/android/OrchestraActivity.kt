package ru.unit.orchestra_features.interactive.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.MaterialColors
import ru.unit.orchestra_features.interactive.android.databinding.ActivityOrchestraBinding

class OrchestraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOrchestraBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }
}