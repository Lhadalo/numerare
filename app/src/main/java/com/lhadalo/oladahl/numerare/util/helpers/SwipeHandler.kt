package com.lhadalo.oladahl.numerare.util.helpers

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView

class SwipeHandler(private val callback: (action: Int, id: Int) -> Unit) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var buttonAction = NO_ACTION
    private var itemPos = 0
    private var swipeBack: Boolean = false
    private lateinit var listener: TouchListener

    companion object {
        const val TAG = "SwipeHandler"
        private const val BUTTON_WIDTH = 300F
        const val LEFT_ACTION = 10
        const val RIGHT_ACTION = 20
        const val NO_ACTION = 30

    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
            = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }


    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun getAnimationDuration(recyclerView: RecyclerView, animationType: Int, animateDx: Float, animateDy: Float): Long {
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

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

                    if (buttonAction != NO_ACTION) {
                        if (buttonAction == LEFT_ACTION) {

                            //callback(LEFT_ACTION, viewHolder.adapterPosition)
                        } else if (buttonAction == RIGHT_ACTION) {
                            //callback(RIGHT_ACTION, viewHolder.adapterPosition)
                        }
                    }

                    //buttonAction = NO_ACTION
                }

                false

            }

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    fun setTouchListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

    }

    class TouchListener : RecyclerView.OnItemTouchListener {
        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

}