package com.mackosoft.lebonalbum.view.albumslist

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mackosoft.lebonalbum.common.extensions.asPx
import com.mackosoft.lebonalbum.common.extensions.doOnEachNextLayout

// sticky header solution from: https://gist.github.com/filipkowicz/1a769001fae407b8813ab4387c42fcbd
class AlbumsListDecoration(
    parent: RecyclerView,
    private val isHeader: (itemPosition: Int) -> Boolean
) : RecyclerView.ItemDecoration() {

    private var currentHeader: Pair<Int, RecyclerView.ViewHolder>? = null

    private val marginTop       = 16.asPx
    private val marginStart     = 16.asPx
    private val marginEnd       = 16.asPx
    private val marginBottom    = 16.asPx


    init {
        parent.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                // clear saved header as it can be outdated now
                currentHeader = null
            }
        })

        parent.doOnEachNextLayout {
            // clear saved layout as it may need layout update
            currentHeader = null
        }
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.getChildAt(0) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) { return }

        val headerView = getHeaderViewForItem(topChildPosition, parent) ?: return

        val contactPoint = headerView.bottom
        val childInContact = getChildInContact(parent, contactPoint) ?: return

        val childInContactPos = parent.getChildAdapterPosition(childInContact)
        if (childInContactPos == RecyclerView.NO_POSITION) { return }
        if (isHeader(childInContactPos)) {
            moveHeader(c, headerView, childInContact)
            return
        }

        drawHeader(c, headerView)
    }


    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }


    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, (nextHeader.top - 25.asPx - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }


    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
        if (parent.adapter == null) return null

        val headerPosition = getHeaderPositionForItem(itemPosition)
        if(headerPosition == RecyclerView.NO_POSITION) return null

        val headerType = parent.adapter?.getItemViewType(headerPosition) ?: return null

        // if match reuse viewHolder
        if (currentHeader?.first == headerPosition && currentHeader?.second?.itemViewType == headerType) {
            return currentHeader?.second?.itemView
        }

        // create new VH for us
        return parent.adapter?.createViewHolder(parent, headerType)?.let {
            // bind it the usual way
            parent.adapter?.onBindViewHolder(it, headerPosition)

            fixLayoutSize(parent, it.itemView)

            // save for next draw
            currentHeader = headerPosition to it

            return@let it.itemView
        }
    }


    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val mBounds = Rect()
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            // we need to discard top / bottom decoration because visually speaking, the contact point is not clear
            if (mBounds.bottom > contactPoint) {
                if (mBounds.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }


    /**
     * Properly measures and layouts the top sticky header as well as its decoration
     *
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        // Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)

        // view decorations
        view.setPaddingRelative(marginStart, 0, marginEnd, 0)

        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }


    private fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = RecyclerView.NO_POSITION
        var currentPosition = itemPosition

        do {
            if (isHeader(currentPosition)) {
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)

        return headerPosition
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        outRect.top = marginTop
        outRect.left = marginStart
        outRect.right = marginEnd
        outRect.bottom = marginBottom

    }

}