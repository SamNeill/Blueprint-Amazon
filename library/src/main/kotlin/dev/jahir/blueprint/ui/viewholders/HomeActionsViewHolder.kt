package dev.jahir.blueprint.ui.viewholders

import android.view.View
import com.afollestad.sectionedrecyclerview.SectionedViewHolder
import com.google.android.material.button.MaterialButton
import dev.jahir.blueprint.R
import dev.jahir.blueprint.data.listeners.HomeItemsListener
import dev.jahir.frames.extensions.context.openLink
import dev.jahir.frames.extensions.context.preferences
import dev.jahir.frames.extensions.context.string
import dev.jahir.frames.extensions.views.context
import dev.jahir.frames.extensions.views.findView
import dev.jahir.frames.extensions.views.gone
import dev.jahir.frames.extensions.views.visible
import dev.jahir.blueprint.ui.activities.BlueprintActivity

class HomeActionsViewHolder(itemView: View) : SectionedViewHolder(itemView) {
    private val shareBtn: View? by itemView.findView(R.id.share_btn)
    private val rateBtn: View? by itemView.findView(R.id.rate_btn)
    private val donateBtn: View? by itemView.findView(R.id.donate_btn)

    fun bind(showDonate: Boolean, listener: HomeItemsListener? = null) {
        (shareBtn as? MaterialButton)?.setSupportAllCaps(!context.preferences.useMaterialYou)
        (rateBtn as? MaterialButton)?.setSupportAllCaps(!context.preferences.useMaterialYou)
        (donateBtn as? MaterialButton)?.setSupportAllCaps(!context.preferences.useMaterialYou)

        val asin = context.string(R.string.amazon_app_store_asin)
        
        shareBtn?.setOnClickListener { listener?.onShareClicked() }
        rateBtn?.setOnClickListener { 
            context.openLink("https://www.amazon.com/dp/$asin")
        }

        // Get donation items from activity
        val donationItems = try {
            (context as? BlueprintActivity)?.getDonationItemsArray() ?: emptyArray()
        } catch (e: Exception) {
            emptyArray()
        }
        
        val shouldShowDonate = showDonate && donationItems.isNotEmpty() && donationItems[0].isNotEmpty()
        
        if (shouldShowDonate) {
            donateBtn?.visible()
            donateBtn?.setOnClickListener { listener?.onDonateClicked() }
        } else {
            donateBtn?.gone()
        }
    }
}
