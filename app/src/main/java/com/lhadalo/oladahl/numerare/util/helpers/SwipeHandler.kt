package com.lhadalo.oladahl.numerare.util.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import com.lhadalo.oladahl.numerare.R

class SwipeHandler(private val context: Context, private val callback: (action: Int, id: Int) -> Unit) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var buttonAction = NO_ACTION
    private var swipeBack: Boolean = false

    private val iconAdd: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_add_24dp)
    private val iconSubtract: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_remove_24dp)

    private var halfIcon: Int = -1

    companion object {
        const val TAG = "SwipeHandler"
        const val LEFT_ACTION = 10
        const val RIGHT_ACTION = 20
        const val NO_ACTION = 30
        private const val iconHorizontalMargin = 72
        private const val BUTTON_WIDTH = 200F
    }

    init {
        iconAdd?.let { halfIcon = it.intrinsicHeight / 2 }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        Log.d(TAG, getMoveThreshold(viewHolder).toString())

        if (dX == 0F) {
            if (buttonAction != NO_ACTION) {
                callback(buttonAction, viewHolder.adapterPosition)
                buttonAction = NO_ACTION
            }
        }

        if (actionState == ACTION_STATE_SWIPE) {
            recyclerView.setOnTouchListener { _, event ->
                swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP

                if (swipeBack) {
                    buttonAction = when {
                        dX < -BUTTON_WIDTH -> RIGHT_ACTION
                        dX > BUTTON_WIDTH -> LEFT_ACTION
                        else -> NO_ACTION
                    }
                }

                false
            }
        }

        drawIcon(c, viewHolder, dX)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawIcon(c: Canvas, holder: RecyclerView.ViewHolder, dX: Float) {

        if (dX < 0) { //Swiping Left
            iconAdd?.let {

                setIconColor(iconAdd, dX < -BUTTON_WIDTH, LEFT_ACTION)

                val top = holder.itemView.top + ((holder.itemView.bottom - holder.itemView.top) / 2 - halfIcon)
                iconAdd.setBounds(
                        holder.itemView.right - iconHorizontalMargin - halfIcon * 2,
                        top,
                        holder.itemView.right - iconHorizontalMargin,
                        top + iconAdd.intrinsicHeight
                )

                iconAdd.draw(c)
            }
        } else if (dX > 0) {
            iconSubtract?.let {
                val top = holder.itemView.top + ((holder.itemView.bottom - holder.itemView.top) / 2 - halfIcon)

                setIconColor(iconSubtract, dX > BUTTON_WIDTH, RIGHT_ACTION)

                iconSubtract.setBounds(
                        iconHorizontalMargin,
                        top,
                        iconHorizontalMargin + iconSubtract.intrinsicWidth,
                        top + iconSubtract.intrinsicHeight
                )

                iconSubtract.draw(c)
            }
        }
    }

    private fun setIconColor(drawable: Drawable, triggerAction: Boolean, swipeAction: Int) {
        if (triggerAction) {
            when (swipeAction) {
                LEFT_ACTION -> DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.color_success))
                RIGHT_ACTION -> DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.color_error))
            }
        } else {
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.white))
        }
    }
}