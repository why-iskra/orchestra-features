package ru.unit.orchestra_features.example.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import orchestra_features.generated.Orchestra
import ru.unit.orchestra_features.common.extension.asFlow
import ru.unit.orchestra_features.example.android.databinding.ActivityMainBinding
import ru.unit.orchestra_features.example.android.orchestra_features.generated.TestScope1
import ru.unit.orchestra_features.interactive.android.AndroidFeatureScopeInjector
import ru.unit.orchestra_features.interactive.android.OrchestraActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        AndroidFeatureScopeInjector.inject(Orchestra.interactiveScopes)

        startActivity(
            Intent(this, OrchestraActivity::class.java)
        )

        binding.switch1.isChecked = TestScope1.features.helloFeature.state.isToggled
        binding.switch2.isChecked = TestScope1.features.helloFeature2.state.isToggled

        binding.switch1.setOnCheckedChangeListener { _, p1 ->
            CoroutineScope(Dispatchers.Main).launch {
                TestScope1.features.helloFeature.toggle(p1)
            }
        }

        binding.switch2.setOnCheckedChangeListener { _, p1 ->
            CoroutineScope(Dispatchers.Main).launch {
                TestScope1.features.helloFeature2.toggle(p1)
            }
        }

        lifecycleScope.launchWhenResumed {
            launch {
                TestScope1.features.f6.asFlow().collect {
                    binding.textView7.text = "f6: ${it.isToggled}"
                }
            }
            launch {
                TestScope1.features.f5.asFlow().collect {
                    binding.textView6.text = "f5: ${it.isToggled}"
                }
            }
            launch {
                TestScope1.features.f4.asFlow().collect {
                    binding.textView5.text = "f4: ${it.isToggled}"
                }
            }
            launch {
                TestScope1.features.f3.asFlow().collect {
                    binding.textView4.text = "f3: ${it.isToggled}"
                }
            }
            launch {
                TestScope1.features.f2.asFlow().collect {
                    binding.textView3.text = "f2: ${it.isToggled}"
                }
            }
            launch {
                TestScope1.features.f1.asFlow().collect {
                    binding.textView2.text = "f1: ${it.isToggled}"
                }
            }
            launch {
                TestScope1.features.helloFeature.asFlow().collect {
                    binding.textView1.text = "helloFeature: ${it.isToggled}"
                }
            }
            launch {
                TestScope1.features.helloFeature2.asFlow().collect {
                    binding.textView.text = "helloFeature2: ${it.isToggled}"
                }
            }
        }
    }
}