package dev.jahir.blueprint.app

import android.os.Bundle
import android.util.Log
import com.amazon.device.iap.model.Product
import com.github.javiersantos.piracychecker.PiracyChecker
import dev.jahir.blueprint.R
import dev.jahir.blueprint.app.billing.AmazonIAPManager
import dev.jahir.blueprint.app.billing.AmazonBillingCallback
import dev.jahir.blueprint.ui.activities.BottomNavigationBlueprintActivity
import dev.jahir.frames.extensions.context.toast

class MainActivity : BottomNavigationBlueprintActivity(), AmazonBillingCallback {

    private var productsAvailable = false
    private lateinit var amazonIAPManager: AmazonIAPManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amazonIAPManager = AmazonIAPManager(this, this)
        amazonIAPManager.loadProducts()
    }

    override fun onProductsLoaded(products: List<Product>) {
        Log.d("MainActivity", "Products loaded: ${products.size}")
        invalidateOptionsMenu()
    }

    override fun onPurchaseSuccess(sku: String) {
        toast("Thank you for your purchase!")
    }

    override fun onBillingError(error: String) {
        toast(error)
    }

    override fun onProductsAvailable(available: Boolean) {
        productsAvailable = available
        invalidateOptionsMenu()
    }

    override fun onBillingClientReady() {
        super.onBillingClientReady()
        if (productsAvailable) {
            invalidateOptionsMenu()
        }
    }

    override fun getDonationItemsIds(): List<String> {
        return listOf("coffee", "pizza", "burger", "meal")
    }

    override val billingEnabled = true

    fun onDonateItemClicked(sku: String) {
        amazonIAPManager.purchase(sku)
    }

    override fun amazonInstallsEnabled(): Boolean = false
    override fun checkLPF(): Boolean = true
    override fun checkStores(): Boolean = true
    override val isDebug: Boolean = BuildConfig.DEBUG

    override fun getLicKey(): String? = null

    override fun getLicenseChecker(): PiracyChecker? {
        destroyChecker() // Important
        return null // Just for CI purposes
    }

    override fun defaultTheme(): Int = R.style.MyApp_Default
    override fun amoledTheme(): Int = R.style.MyApp_Default_Amoled

    override fun defaultMaterialYouTheme(): Int = R.style.MyApp_Default_MaterialYou
    override fun amoledMaterialYouTheme(): Int = R.style.MyApp_Default_Amoled_MaterialYou
}

