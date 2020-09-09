package com.money.budget.wealthy.widget

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.money.budget.wealthy.R
import java.util.ArrayList
import kotlin.math.abs
import kotlinx.android.synthetic.main.dots_indicator.view.dot

class DotsIndicator : LinearLayout {
    private val defaultPointColor = Color.CYAN
    private val defaultWidthFactor = 2.5f
    private var dots: MutableList<ImageView>? = null
    private var viewPager: ViewPager? = null
    private var dotsSize: Float = 0.toFloat()
    private var dotsCornerRadius: Float = 0.toFloat()
    private var dotsSpacing: Float = 0.toFloat()
    private var currentPage: Int = 0
    private var dotsWidthFactor: Float = 0.toFloat()
    private var dotsColor: Int = 0
    private var dotsClickable: Boolean = false
    private var pageChangedListener: ViewPager.OnPageChangeListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        dots = ArrayList()
        orientation = HORIZONTAL

        dotsSize = dpToPx(16).toFloat() // 16dp
        dotsSpacing = dpToPx(4).toFloat() // 4dp
        dotsCornerRadius = dotsSize / 2

        dotsWidthFactor = defaultWidthFactor
        dotsColor = defaultPointColor
        dotsClickable = true

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.DotsIndicator)

            dotsColor = a.getColor(R.styleable.DotsIndicator_dotsColor, defaultPointColor)
            setUpCircleColors(dotsColor)

            dotsWidthFactor = a.getFloat(R.styleable.DotsIndicator_dotsWidthFactor, 2.5f)
            if (dotsWidthFactor < 1) {
                dotsWidthFactor = 2.5f
            }

            dotsSize = a.getDimension(R.styleable.DotsIndicator_dotsSize, dotsSize)
            dotsCornerRadius =
                a.getDimension(R.styleable.DotsIndicator_dotsCornerRadius, dotsSize / 2).toInt()
                    .toFloat()
            dotsSpacing = a.getDimension(R.styleable.DotsIndicator_dotsSpacing, dotsSpacing)

            a.recycle()
        } else {
            setUpCircleColors(defaultPointColor)
        }

        if (isInEditMode) {
            addDots(3)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshDots()
    }

    private fun refreshDots() {
        if (viewPager != null && viewPager!!.adapter != null) {
            // Check if we need to refresh the dots count
            if (dots!!.size < viewPager!!.adapter!!.count) {
                addDots(viewPager!!.adapter!!.count - dots!!.size)
            } else if (dots!!.size > viewPager!!.adapter!!.count) {
                removeDots(dots!!.size - viewPager!!.adapter!!.count)
            }
            setUpDotsAnimators()
        } else {
            Log.e(
                DotsIndicator::class.java.simpleName,
                "You have to set an adapter to the view pager before !"
            )
        }
    }

    private fun addDots(count: Int) {
        for (i in 0 until count) {
            val dot = LayoutInflater.from(context).inflate(R.layout.dots_indicator, this, false)
            val imageView = dot!!.dot
            val params = imageView.layoutParams as RelativeLayout.LayoutParams
            params.height = dotsSize.toInt()
            params.width = params.height
            params.setMargins(dotsSpacing.toInt(), 0, dotsSpacing.toInt(), 0)
            (imageView.background as GradientDrawable).cornerRadius = dotsCornerRadius
            (imageView.background as GradientDrawable).setColor(dotsColor)

            dot.setOnClickListener {
                if (dotsClickable && viewPager != null && viewPager!!.adapter != null && i < viewPager!!.adapter!!.count) {
                    viewPager!!.setCurrentItem(i, true)
                }
            }

            dots!!.add(imageView)
            addView(dot)
        }
    }

    private fun removeDots(count: Int) {
        for (i in 0 until count) {
            removeViewAt(childCount - 1)
            dots!!.removeAt(dots!!.size - 1)
        }
    }

    private fun setUpDotsAnimators() {
        if (viewPager != null && viewPager!!.adapter != null && viewPager!!.adapter!!.count > 0) {
            if (currentPage < dots!!.size) {
                val dot = dots!![currentPage]

                val params = dot.layoutParams as RelativeLayout.LayoutParams
                params.width = dotsSize.toInt()
                dot.layoutParams = params
            }

            currentPage = viewPager!!.currentItem
            if (currentPage >= dots!!.size) {
                currentPage = dots!!.size - 1
                viewPager!!.setCurrentItem(currentPage, false)
            }
            val dot = dots!![currentPage]

            val params = dot.layoutParams as RelativeLayout.LayoutParams
            params.width = (dotsSize * dotsWidthFactor).toInt()
            dot.layoutParams = params

            if (pageChangedListener != null) {
                viewPager!!.removeOnPageChangeListener(pageChangedListener!!)
            }
            setUpOnPageChangedListener()
            viewPager!!.addOnPageChangeListener(pageChangedListener!!)
        }
    }

    private fun setUpOnPageChangedListener() {
        pageChangedListener = object : ViewPager.OnPageChangeListener {
            private var lastPage: Int = 0

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position != currentPage && positionOffset == 0f || currentPage < position) {
                    setDotWidth(dots!![currentPage], dotsSize.toInt())
                    currentPage = position
                }

                if (abs(currentPage - position) > 1) {
                    setDotWidth(dots!![currentPage], dotsSize.toInt())
                    currentPage = lastPage
                }

                var dot = dots!![currentPage]

                var nextDot: ImageView? = null
                if (currentPage == position && currentPage + 1 < dots!!.size) {
                    nextDot = dots!![currentPage + 1]
                } else if (currentPage > position) {
                    nextDot = dot
                    dot = dots!![currentPage - 1]
                }

                val dotWidth =
                    (dotsSize + dotsSize * (dotsWidthFactor - 1) * (1 - positionOffset)).toInt()
                setDotWidth(dot, dotWidth)

                if (nextDot != null) {
                    val nextDotWidth =
                        (dotsSize + dotsSize * (dotsWidthFactor - 1) * positionOffset).toInt()
                    setDotWidth(nextDot, nextDotWidth)
                }

                lastPage = position
            }

            override fun onPageSelected(position: Int) {
                // To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageScrollStateChanged(state: Int) {
                // To change body of created functions use File | Settings | File Templates.
            }

            private fun setDotWidth(dot: ImageView, dotWidth: Int) {
                val dotParams = dot.layoutParams
                dotParams.width = dotWidth
                dot.layoutParams = dotParams
            }
        }
    }

    private fun setUpCircleColors(color: Int) {
        if (dots != null) {
            for (elevationItem in dots!!) {
                (elevationItem.background as GradientDrawable).setColor(color)
            }
        }
    }

    private fun setUpViewPager() {
        if (viewPager!!.adapter != null) {
            viewPager!!.adapter!!.registerDataSetObserver(object : DataSetObserver() {
                override fun onChanged() {
                    super.onChanged()
                    refreshDots()
                }
            })
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (context.resources.displayMetrics.density * dp).toInt()
    }

    /**
    * Users Methods
    **/

    fun setPointsColor(color: Int) {
        setUpCircleColors(color)
    }

    fun setDotsClickable(dotsClickable: Boolean) {
        this.dotsClickable = dotsClickable
    }

    fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        setUpViewPager()
        refreshDots()
    }
}
