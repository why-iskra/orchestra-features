package ru.unit.orchestra_features.interactive.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.unit.orchestra_features.interactive.android.databinding.AdapterScopeBinding
import ru.unit.orchestra_features.interactive.android.model.ScopeModel
import ru.unit.orchestra_features.common.support.interactive.ElementData

class ScopeAdapter : ListAdapter<ScopeModel, ScopeAdapter.ScopeViewHolder>(callback) {

    var listener: Listener? = null

    class ScopeViewHolder(
        private val binding: AdapterScopeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(
            scopeModel: ScopeModel,
            listener: Listener?
        ) {
            binding.name.text = scopeModel.element.name
            binding.module.text = scopeModel.module
            binding.root.setOnClickListener {
                listener?.onClick(scopeModel.element)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScopeViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ScopeViewHolder(
            AdapterScopeBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ScopeViewHolder,
        position: Int
    ) = holder.bindTo(
        scopeModel = getItem(position),
        listener = listener
    )

    private companion object {

        val callback
            get() = object : DiffUtil.ItemCallback<ScopeModel>() {
                override fun areItemsTheSame(oldItem: ScopeModel, newItem: ScopeModel) =
                    oldItem.element == newItem.element

                override fun areContentsTheSame(oldItem: ScopeModel, newItem: ScopeModel) =
                    oldItem == newItem
            }
    }

    fun interface Listener {

        fun onClick(scopeElement: ElementData)
    }
}