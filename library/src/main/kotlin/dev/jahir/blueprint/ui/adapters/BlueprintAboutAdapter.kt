package dev.jahir.blueprint.ui.adapters

import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.imageview.ShapeableImageView
import dev.jahir.blueprint.R
import dev.jahir.frames.data.models.AboutItem
import dev.jahir.frames.extensions.views.context

class BlueprintAboutAdapter(
    private val aboutItem: AboutItem
) : RecyclerView.Adapter<BlueprintAboutAdapter.AboutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder =
        AboutViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_about, parent, false)
        )

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bind(aboutItem)
    }

    override fun getItemCount(): Int = 1

    class AboutViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val photo: ShapeableImageView? = view.findViewById(R.id.photo)
        private val name: TextView? = view.findViewById(R.id.name)
        private val description: TextView? = view.findViewById(R.id.description)
        private val socialLinks: ChipGroup? = view.findViewById(R.id.social_links)
        private val footerDescription: TextView? = view.findViewById(R.id.footer_description)

        private val Float.dpToPx: Float
            get() = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this,
                itemView.context.resources.displayMetrics
            )

        private fun getIconForButton(buttonText: String): Int {
            return when (buttonText.lowercase()) {
                "github" -> R.drawable.ic_github
                "instagram" -> R.drawable.ic_instagram
                "twitter" -> R.drawable.ic_twitter
                "linkedin" -> R.drawable.ic_linkedin
                "telegram" -> R.drawable.ic_telegram
                "email" -> R.drawable.ic_email
                "youtube" -> R.drawable.ic_youtube
                "facebook" -> R.drawable.ic_facebook
                "website" -> R.drawable.ic_website
                "reddit" -> R.drawable.ic_reddit
                "pinterest" -> R.drawable.ic_pinterest
                "gumroad" -> R.drawable.ic_gumroad
                "buymeacoffee" -> R.drawable.ic_buymeacoffee
                "bsky" -> R.drawable.ic_bsky
                "threads" -> R.drawable.ic_threads
                "mastodon" -> R.drawable.ic_mastodon
                "discord" -> R.drawable.ic_discord
                else -> 0
            }
        }

        fun bind(item: AboutItem) {
            try {
                // Try to load as resource ID
                item.photoUrl?.let { photoUrl ->
                    val resourceId = itemView.context.resources.getIdentifier(
                        photoUrl.replace("drawable/", ""),
                        "drawable",
                        "dev.jahir.blueprint.app"
                    )
                    if (resourceId != 0) {
                        photo?.setImageResource(resourceId)
                    } else {
                        // Fallback to URL loading if not a resource
                        photo?.load(photoUrl) {
                            crossfade(true)
                            placeholder(dev.jahir.blueprint.R.drawable.ic_profile_placeholder)
                            error(dev.jahir.blueprint.R.drawable.ic_profile_placeholder)
                            fallback(dev.jahir.blueprint.R.drawable.ic_profile_placeholder)
                        }
                    }
                } ?: run {
                    // If photoUrl is null, use placeholder
                    photo?.setImageResource(dev.jahir.blueprint.R.drawable.ic_profile_placeholder)
                }
            } catch (e: Exception) {
                // Fallback to placeholder if anything goes wrong
                photo?.setImageResource(dev.jahir.blueprint.R.drawable.ic_profile_placeholder)
            }
            
            name?.text = item.name
            description?.text = item.description
            
            socialLinks?.removeAllViews()
            item.links.forEach { (buttonText, link) ->
                val chip = Chip(itemView.context, null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action).apply {
                    chipIcon = getIconForButton(buttonText).takeIf { it != 0 }?.let {
                        itemView.context.getDrawable(it)
                    }
                    isChipIconVisible = true
                    chipIconSize = 24f.dpToPx
                    iconEndPadding = 0f
                    iconStartPadding = 0f
                    textStartPadding = 0f
                    textEndPadding = 0f
                    chipMinHeight = 48f.dpToPx
                    chipStartPadding = 12f.dpToPx
                    chipEndPadding = 12f.dpToPx
                    text = ""  // Remove text
                    setOnClickListener {
                        try {
                            itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                socialLinks?.addView(chip)
            }

            // Set footer description
            try {
                val appContext = itemView.context.packageManager
                    .getResourcesForApplication("dev.jahir.blueprint.app")
                val footerResId = appContext.getIdentifier(
                    "about_footer_description",
                    "string",
                    "dev.jahir.blueprint.app"
                )
                if (footerResId != 0) {
                    footerDescription?.text = android.text.Html.fromHtml(
                        appContext.getString(footerResId),
                        android.text.Html.FROM_HTML_MODE_COMPACT
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
} 