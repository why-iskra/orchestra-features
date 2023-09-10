package ru.unit.orchestra_features.interactive.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.unit.orchestra_features.interactive.android.databinding.AdapterFeatureBinding
import ru.unit.orchestra_features.interactive.android.model.FeatureModel
import ru.unit.orchestra_features.common.support.interactive.ElementData

class FeatureAdapter : ListAdapter<FeatureModel, FeatureAdapter.FeatureViewHolder>(callback) {

    var listener: Listener? = null

    class FeatureViewHolder(
        private val binding: AdapterFeatureBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(
            featureModel: FeatureModel,
            listener: Listener?
        ) {
            binding.name.text = featureModel.element.name
            binding.data.text = featureModel.state?.data.toString()

            val hasState = (featureModel.state != null)

            binding.data.isVisible = hasState
            binding.switchView.isVisible = hasState

            val hasDescription = !featureModel.description.isNullOrBlank()

            binding.separatorView.isVisible = hasDescription
            binding.description.isVisible = hasDescription

            binding.description.text = featureModel.description

            binding.switchView.setOnClickListener { _ ->
                featureModel.toggleable?.__interactiveToggle(binding.switchView.isChecked)
            }

            binding.switchView.isEnabled = (featureModel.toggleable != null)
            binding.switchView.isChecked = featureModel.state?.isToggled ?: false

            if (featureModel.isKnown) {
                binding.root.setOnClickListener {
                    listener?.onClick(featureModel.element)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeatureViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return FeatureViewHolder(
            AdapterFeatureBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: FeatureViewHolder,
        position: Int
    ) = holder.bindTo(
        featureModel = getItem(position),
        listener = listener
    )

    private companion object {

        val callback
            get() = object : DiffUtil.ItemCallback<FeatureModel>() {
                override fun areItemsTheSame(oldItem: FeatureModel, newItem: FeatureModel) =
                    oldItem.element == newItem.element

                override fun areContentsTheSame(oldItem: FeatureModel, newItem: FeatureModel) =
                    oldItem == newItem
            }
    }

    fun interface Listener {

        fun onClick(featureElement: ElementData)
    }
}