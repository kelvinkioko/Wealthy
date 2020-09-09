package com.money.budget.wealthy.ui.onboarding

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import com.money.budget.wealthy.R
import kotlinx.android.synthetic.main.onboarding_item_welcome.view.onboardingImage
import kotlinx.android.synthetic.main.onboarding_item_welcome.view.onboardingItemDescription
import kotlinx.android.synthetic.main.onboarding_item_welcome.view.onboardingItemTitle

class WelcomePagerAdapter : PagerAdapter() {

    private var context: Context? = null
    private var welcomeTitle: Array<String>? = null
    private var welcomeDescription: Array<String>? = null
    private var items: List<WelcomeItem>? = null

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        context = container.context
        val item = LayoutInflater.from(container.context).inflate(R.layout.onboarding_item_welcome, container, false)

        items = mutableListOf(
            WelcomeItem("Random on-boarding title", "Here below is where we will have our on-boarding description.", R.drawable.onboarding_icon_intro_loan),
            WelcomeItem("Random on-boarding title", "Here below is where we will have our on-boarding description", R.drawable.onboarding_icon_intro_loan),
            WelcomeItem("Random on-boarding title", "Here below is where we will have our on-boarding description", R.drawable.onboarding_icon_intro_loan)
        )

        val img = item.onboardingImage
        val cartTitle = item.onboardingItemTitle
        val cardDescription = item.onboardingItemDescription

        img.setImageResource(items!![position].image)
        cartTitle.text = items!![position].title
        cardDescription.text = items!![position].description

        container.addView(item)

        return item
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return 3
    }

    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
        container.removeView(`object` as View)
    }

    data class WelcomeItem(
        val title: String,
        val description: String,
        val image: Int
    )
}
