package ru.unit.orchestra_features.interactive.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.unit.orchestra_features.interactive.android.databinding.OfiaActivityOrchestraBinding

class OrchestraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = OfiaActivityOrchestraBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }
}