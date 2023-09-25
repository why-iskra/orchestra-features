package ru.unit.orchestra_features.example.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.unit.orchestra_features.common.extension.asFlow
import ru.unit.orchestra_features.example.android.databinding.ActivityMainBinding
import ru.unit.orchestra_features.example.android.orchestra_features.generated.Orchestra
import ru.unit.orchestra_features.example.android.orchestra_features.generated.TestFeatureScope
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

        binding.switch1.isChecked = TestFeatureScope.helloFeature.state.isToggled
        binding.switch2.isChecked = TestFeatureScope.hello2Feature.state.isToggled

        binding.switch1.setOnCheckedChangeListener { _, p1 ->
            CoroutineScope(Dispatchers.Main).launch {
                TestFeatureScope.helloFeature.toggle(p1)
            }
        }

        binding.switch2.setOnCheckedChangeListener { _, p1 ->
            CoroutineScope(Dispatchers.Main).launch {
                TestFeatureScope.hello2Feature.toggle(p1)
            }
        }

        lifecycleScope.launchWhenResumed {
            launch {
                TestFeatureScope.testFeature.asFlow().collect {
                    binding.textView7.text = "f6: ${it.isToggled}"
                }
            }
            launch {
                TestFeatureScope.f5Feature.asFlow().collect {
                    binding.textView6.text = "f5: ${it.isToggled}"
                }
            }
            launch {
                TestFeatureScope.f4Feature.asFlow().collect {
                    binding.textView5.text = "f4: ${it.isToggled}"
                }
            }
            launch {
                TestFeatureScope.f3Feature.asFlow().collect {
                    binding.textView4.text = "f3: ${it.isToggled}"
                }
            }
            launch {
                TestFeatureScope.f2Feature.asFlow().collect {
                    binding.textView3.text = "f2: ${it.isToggled}"
                }
            }
            launch {
                TestFeatureScope.f1Feature.asFlow().collect {
                    binding.textView2.text = "f1: ${it.isToggled}"
                }
            }
            launch {
                TestFeatureScope.helloFeature.asFlow().collect {
                    binding.textView1.text = "helloFeature: ${it.isToggled}"
                }
            }
            launch {
                TestFeatureScope.hello2Feature.asFlow().collect {
                    binding.textView.text = "helloFeature2: ${it.isToggled}"
                }
            }
        }
    }
}