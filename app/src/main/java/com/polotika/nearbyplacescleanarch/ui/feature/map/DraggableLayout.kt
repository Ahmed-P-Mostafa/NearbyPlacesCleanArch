package com.polotika.nearbyplacescleanarch.ui.feature.map

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class DraggableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private var lastTouch = 0L
    private val SCROLL_TIME = 200L

    private var dragListener : IDragCallback?=null

    fun setDrag(dragListener : IDragCallback){
        this.dragListener = dragListener
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        when(ev.action){
            MotionEvent.ACTION_DOWN -> lastTouch = SystemClock.uptimeMillis()
            MotionEvent.ACTION_UP ->{
                val now = SystemClock.uptimeMillis()
                if (now - lastTouch>SCROLL_TIME)
                    dragListener?.onDrag()
            }
        }



        return super.dispatchTouchEvent(ev)

    }
}