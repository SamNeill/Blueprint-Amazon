package dev.jahir.blueprint.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.jahir.blueprint.BuildConfig
import dev.jahir.blueprint.R
import dev.jahir.blueprint.data.BlueprintPreferences
import dev.jahir.blueprint.ui.decorations.RecyclerViewGridSpacingDecoration
import dev.jahir.frames.extensions.context.findView
import dev.jahir.frames.extensions.context.string
import dev.jahir.frames.extensions.context.stringArray
import dev.jahir.frames.extensions.views.tint
import dev.jahir.frames.ui.activities.base.BaseThemedActivity
import dev.jahir.frames.data.models.AboutItem
import dev.jahir.blueprint.ui.adapters.BlueprintAboutAdapter
import android.widget.TextView
import android.util.TypedValue
import androidx.core.content.ContextCompat

class BlueprintAboutActivity : BaseThemedActivity<BlueprintPreferences>() {
    
    override val preferences: BlueprintPreferences by lazy { BlueprintPreferences(this) }
    
    private val creditsRecyclerView: RecyclerView? by findView(R.id.credits_rv)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blueprint_about)

        val toolbar: Toolbar? by findView(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }
        
        toolbar?.let {
            val titleTextView = TextView(this).apply {
                text = string(R.string.about)
                textSize = 20f
                setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline6)
                gravity = android.view.Gravity.CENTER
                
                // Get the default text color from the theme
                val typedValue = TypedValue()
                theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
                setTextColor(ContextCompat.getColor(context, typedValue.resourceId))
            }
            
            val params = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.CENTER
            }
            
            it.addView(titleTextView, params)
            it.tint()
        }
        
        setupCredits()
    }
    
    private fun setupCredits() {
        // Get the resource IDs from the app package
        val appContext = packageManager.getResourcesForApplication("dev.jahir.blueprint.app")
        
        val photos = appContext.getStringArray(
            appContext.getIdentifier("credits_photos", "array", "dev.jahir.blueprint.app")
        )
        val titles = appContext.getStringArray(
            appContext.getIdentifier("credits_titles", "array", "dev.jahir.blueprint.app")
        )
        val descriptions = appContext.getStringArray(
            appContext.getIdentifier("credits_descriptions", "array", "dev.jahir.blueprint.app")
        )
        val buttons = appContext.getStringArray(
            appContext.getIdentifier("credits_buttons", "array", "dev.jahir.blueprint.app")
        )
        val links = appContext.getStringArray(
            appContext.getIdentifier("credits_links", "array", "dev.jahir.blueprint.app")
        )
        
        val linksMap = ArrayList<Pair<String, String>>()
        for (i in buttons.indices) {
            // Only add links that are not empty or blank
            if (!links[i].isNullOrBlank()) {
                linksMap.add(Pair(buttons[i], links[i]))
            }
        }
        
        val credit = AboutItem(
            name = titles[0],
            description = descriptions[0],
            photoUrl = photos[0],
            links = linksMap
        )
        
        creditsRecyclerView?.layoutManager = LinearLayoutManager(this)
        creditsRecyclerView?.adapter = BlueprintAboutAdapter(credit)
        creditsRecyclerView?.addItemDecoration(
            RecyclerViewGridSpacingDecoration(1, 8)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) supportFinishAfterTransition()
        return super.onOptionsItemSelected(item)
    }
}
