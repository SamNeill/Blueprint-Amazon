package dev.jahir.blueprint.ui.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.jahir.frames.extensions.resources.dpToPx

class RecyclerViewGridSpacingDecoration(
    private val spanCount: Int,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = column * spacing.dpToPx / spanCount
        outRect.right = spacing.dpToPx - (column + 1) * spacing.dpToPx / spanCount

        if (position >= spanCount) {
            outRect.top = spacing.dpToPx
        }
    }
} 