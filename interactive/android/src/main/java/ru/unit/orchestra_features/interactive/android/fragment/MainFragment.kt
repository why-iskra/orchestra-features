package ru.unit.orchestra_features.interactive.android.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.search.SearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.unit.orchestra_features.interactive.android.R
import ru.unit.orchestra_features.interactive.android.adapter.FeatureAdapter
import ru.unit.orchestra_features.interactive.android.adapter.ScopeAdapter
import ru.unit.orchestra_features.interactive.android.databinding.FragmentMainBinding
import ru.unit.orchestra_features.interactive.android.utils.updateStatusBarState
import ru.unit.orchestra_features.interactive.android.viewmodel.MainViewModel
import ru.unit.orchestra_features.common.utils.delegate.SingleAssignment


class MainFragment : Fragment(R.layout.fragment_main) {

    private val model: MainViewModel by viewModels()

    private val featureAdapter = FeatureAdapter()
    private val scopeAdapter = ScopeAdapter()

    private val singleAssignmentController = SingleAssignment.GroupController()

    private var binding by SingleAssignment<FragmentMainBinding>().apply {
        singleAssignmentController.add(controller())
    }

    private var bottomSheetBehavior by SingleAssignment<BottomSheetBehavior<*>>().apply {
        singleAssignmentController.add(controller())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainBinding.bind(view)

        setupSearch()
        setupBottomSheet()
        setupFab()
        setupScopeNameTextView()
        setupFeaturesRecyclerView()
        setupScopesRecyclerView()
        setupObservers()
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

    private fun CoroutineScope.collectFlows() {
        launch {
            model.statusBarStateFlow.collect(activity::updateStatusBarState)
        }

        launch {
            model.featureModelsFlow.collect(featureAdapter::submitList)
        }

        launch {
            model.scopeModelsFlow.collect(scopeAdapter::submitList)
        }

        launch {
            model.scopeNameFlow.collect(binding.scopeNameTextView::setText)
        }
    }

    private fun setupSearch() {
        binding.searchView.addTransitionListener { _, _, newState ->
            when (newState) {
                SearchView.TransitionState.HIDDEN -> model.onSearchHidden()
                SearchView.TransitionState.SHOWN -> model.onSearchShown()
                else -> Unit
            }
        }
    }

    private fun setupBottomSheet() {
        val callback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.fab.isVisible = (newState == BottomSheetBehavior.STATE_HIDDEN)

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
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun setupScopeNameTextView() {
        binding.scopeNameTextView.isSelected = true
    }

    private fun setupFeaturesRecyclerView() {
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.appBar.viewTreeObserver.removeOnGlobalLayoutListener(this)

                binding.featuresRecyclerView.updatePadding(
                    top = binding.appBar.height
                )
            }
        }

        binding.appBar.viewTreeObserver.addOnGlobalLayoutListener(listener)

        binding.featuresRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null

            adapter = featureAdapter
        }

        featureAdapter.listener = FeatureAdapter.Listener { featureElement ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToFeatureFragment(featureElement.id)
            )
        }
    }

    private fun setupScopesRecyclerView() {
        binding.scopesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null

            adapter = scopeAdapter
        }

        scopeAdapter.listener = ScopeAdapter.Listener { scopeElement ->
            model.onScopeChosen(scopeElement)

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}