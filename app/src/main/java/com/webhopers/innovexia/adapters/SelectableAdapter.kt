package com.webhopers.innovexia.adapters

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.*
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback
import com.bignerdranch.android.multiselector.MultiSelector
import com.bignerdranch.android.multiselector.SwappingHolder
import com.facebook.drawee.backends.pipeline.Fresco
import com.stfalcon.frescoimageviewer.ImageViewer
import com.webhopers.innovexia.R
import com.webhopers.innovexia.dialogs.CreateSlideDialog
import com.webhopers.innovexia.services.GlideApp
import kotlinx.android.synthetic.main.square_image_view.view.*

class SelectableAdapter(val dataset: List<String?>, val activity: AppCompatActivity, val context: Context) : RecyclerView.Adapter<SelectableAdapter.ViewHolder>() {

    val multiSelector = MultiSelector()
    var actMode: ActionMode? = null
    var itemViews = SparseArray<View>()

    //will contain the items currently selected
    val selectedPositions = HashSet<Int>()

    /**
     *
     * toggles view's selection
     */
    fun toggleItemSelection(select: Boolean, pos: Int) {
        if (itemViews[pos] != null) itemViews[pos].isSelected = select

        if (select)
            selectedPositions.add(pos)
        else
            selectedPositions.remove(pos)
        if (selectedPositions.isEmpty()) {
            actMode?.finish()
            return
        }
    }

    /**
     *
     * communication interface b/w adapter and view holder
     */
    val adapterListener = object: AdapterListener {
        override fun startActionMode() {
            activity.startSupportActionMode(multiSelectorMode)
        }

        override fun startImageViewer(context: Context, position: Int) {
            ImageViewer.Builder(context, dataset)
                    .setStartPosition(position)
                    .show()
            Fresco.initialize(context)
        }

        override fun toggleItemSelectionAdapter(select: Boolean, position: Int) {
            toggleItemSelection(select, position)
        }

        override fun getSelectedPositions(): HashSet<Int> = selectedPositions
    }

    /**
     *
     * action mode callback
     */
    val multiSelectorMode = object : ModalMultiSelectorCallback(multiSelector) {
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            when(item.itemId) {
                R.id.action_create_slide -> CreateSlideDialog(context)
            }
            return true
        }

        override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
            super.onCreateActionMode(actionMode, menu)
            actMode = actionMode
            activity.menuInflater.inflate(R.menu.action_menu, menu)
            return true

        }

        override fun onDestroyActionMode(actionMode: ActionMode) {
            super.onDestroyActionMode(actionMode)
            selectedPositions.forEach {
                if (itemViews[it] != null)
                    itemViews[it].isSelected = false
            }
            selectedPositions.clear()
            actMode = null
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.bindView(dataset[position], position)

        //put views in item views
        itemViews.put(position, view)

        //if old view holder is used then toggle its selection
        toggleItemSelection(selectedPositions.contains(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.square_image_view, parent, false)
        return ViewHolder(view, multiSelector, adapterListener)
    }

    override fun getItemCount() = dataset.size

    class ViewHolder(val view: View,
                     val multiSelector: MultiSelector,
                     val adapterListener: AdapterListener) : SwappingHolder(view, MultiSelector()) {

        fun bindView(url: String?, pos: Int): View{
            itemView.apply {
                GlideApp.with(context)
                        .load(url)
                        .centerCrop()
                        .into(itemView.square_image_view)
                setOnClickListener { viewClicked(pos) }
                setOnLongClickListener {
                    viewLongClicked()
                    true
                }
            }

            return itemView
        }

        fun viewClicked(position: Int) {
            if (multiSelector.isSelectable) {
                val isSelected = adapterListener.getSelectedPositions().contains(layoutPosition)
                adapterListener.toggleItemSelectionAdapter(!isSelected, layoutPosition)
            } else {
                adapterListener.startImageViewer(itemView.context, position)
            }
        }

        fun viewLongClicked() {
            if (!multiSelector.isSelectable) {
                multiSelector.isSelectable = true
                adapterListener.startActionMode()
                adapterListener.toggleItemSelectionAdapter(true, layoutPosition)
            }
        }
    }

    interface AdapterListener {
        fun toggleItemSelectionAdapter(select: Boolean, position: Int)
        fun getSelectedPositions(): HashSet<Int>
        fun startImageViewer(context: Context, position: Int)
        fun startActionMode()
    }
}