package ru.unit.orchestra_features.interactive.android.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.unit.orchestra_features.interactive.android.R
import ru.unit.orchestra_features.interactive.android.adapter.FeatureAdapter
import ru.unit.orchestra_features.interactive.android.databinding.FragmentFeatureBinding
import ru.unit.orchestra_features.interactive.android.utils.updateStatusBarState
import ru.unit.orchestra_features.interactive.android.viewmodel.FeatureViewModel
import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.common.utils.delegate.SingleAssignment
import ru.unit.orchestra_features.common.utils.delegate.dataClassBeautifier


class FeatureFragment : Fragment(R.layout.fragment_feature) {

    private val id: String get() = arguments?.getString("id")!!

    private val model: FeatureViewModel by viewModels {
        ViewModelFactory(id)
    }

    private val singleAssignmentController = SingleAssignment.GroupController()

    private var binding by SingleAssignment<FragmentFeatureBinding>().apply {
        singleAssignmentController.add(controller())
    }

    private var bottomSheetBehavior by SingleAssignment<BottomSheetBehavior<*>>().apply {
        singleAssignmentController.add(controller())
    }

    private val dependsOnAdapter = FeatureAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFeatureBinding.bind(view)

        setupNameTextView()
        setupBottomSheet()
        setupFab()
        setupObservers()
        setupSwitch()
        setupDependsOnRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        singleAssignmentController.reset()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectFlows()
            }
        }
    }

    private fun setupNameTextView() {
        binding.nameTextView.isSelected = true
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun CoroutineScope.collectFlows() {
        launch {
            model.statusBarStateFlow.collect(activity::updateStatusBarState)
        }

        launch {
            model.featureNameFlow.collect(binding.nameTextView::setText)
        }

        launch {
            model.featureDescriptionFlow.collect(::handleDescription)
        }

        launch {
            model.featureStateFlow.collect(::handleFeatureState)
        }

        launch {
            model.dependsOnFlow.collect(dependsOnAdapter::submitList)
        }
    }

    private fun handleDescription(description: String) {
        binding.description.text = description

        val hasDescription = description.isNotBlank()

        binding.description.isVisible = hasDescription
        binding.separatorView.isVisible = hasDescription
    }

    private fun handleFeatureState(state: Feature.State<out Any?>) {
        binding.switchView.isChecked = state.isToggled

        binding.data.text = state.data.dataClassBeautifier()
    }

    private fun setupSwitch() {
        binding.switchView.setOnClickListener {
            model.onToggle(binding.switchView.isChecked)
        }
    }

    private fun setupBottomSheet() {
        val callback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    model.onBottomSheetShown()
                } else {
                    model.onBottomSheetHidden()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            addBottomSheetCallback(callback)
        }
    }

    private fun setupDependsOnRecyclerView() {
        binding.dependsOnRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null

            adapter = dependsOnAdapter
        }

        dependsOnAdapter.listener = FeatureAdapter.Listener { featureElement ->
            findNavController().navigate(
                FeatureFragmentDirections.actionFeatureFragmentSelf(featureElement.id)
            )
        }
    }

    private class ViewModelFactory(
        private val id: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>) =
            FeatureViewModel(id) as T
    }
}