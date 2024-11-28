package dev.jahir.blueprint.app.billing

import com.amazon.device.iap.model.Product

interface AmazonBillingCallback {
    fun onProductsLoaded(products: List<Product>)
    fun onPurchaseSuccess(sku: String)
    fun onBillingError(error: String)
    fun onProductsAvailable(available: Boolean)
} 