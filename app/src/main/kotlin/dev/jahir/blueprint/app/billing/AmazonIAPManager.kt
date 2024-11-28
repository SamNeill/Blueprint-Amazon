package dev.jahir.blueprint.app.billing

import android.util.Log
import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.*
import dev.jahir.frames.ui.activities.base.BaseBillingActivity
import dev.jahir.blueprint.R

class AmazonIAPManager(
    private val activity: BaseBillingActivity<*>,
    private val callback: AmazonBillingCallback
) : PurchasingListener {

    companion object {
        private const val TAG = "AmazonIAP"
    }

    private var productDataMap: Map<String, Product> = emptyMap()
    private var userId: String? = null

    init {
        PurchasingService.registerListener(activity.applicationContext, this)
        Log.d(TAG, "Initializing Amazon IAP")
        PurchasingService.getUserData()
    }

    fun loadProducts() {
        val items = activity.getDonationItemsIds()
        Log.d(TAG, "Loading products: $items")
        if (items.isNotEmpty()) {
            PurchasingService.getProductData(items.toSet())
        }
    }

    override fun onProductDataResponse(response: ProductDataResponse) {
        Log.d(TAG, "Product data response: ${response.requestStatus}")
        when (response.requestStatus) {
            ProductDataResponse.RequestStatus.SUCCESSFUL -> {
                productDataMap = response.productData
                val products = response.productData.values.map { product ->
                    Log.d(TAG, """
                        Product Details:
                        SKU: ${product.sku}
                        Title: ${product.title}
                        Description: ${product.description}
                        Price: ${product.price}
                        Type: ${product.productType}
                        """.trimIndent())
                    product
                }
                
                // Log any unavailable SKUs
                val unavailableSkus = response.unavailableSkus
                if (unavailableSkus.isNotEmpty()) {
                    Log.w(TAG, "Unavailable SKUs: $unavailableSkus")
                }
                
                callback.onProductsAvailable(products.isNotEmpty())
                callback.onProductsLoaded(products)
            }
            else -> {
                Log.e(TAG, "Failed to get product data: ${response.requestStatus}")
                callback.onProductsAvailable(false)
                callback.onBillingError("Failed to load products")
            }
        }
    }

    fun purchase(sku: String) {
        if (userId == null) {
            Log.e(TAG, "Cannot purchase - user not initialized")
            callback.onBillingError("Purchase failed - Please try again")
            return
        }
        
        if (!productDataMap.containsKey(sku)) {
            Log.e(TAG, "Cannot purchase - SKU not found: $sku")
            callback.onBillingError("Product not available")
            return
        }

        Log.d(TAG, "Initiating purchase for SKU: $sku")
        PurchasingService.purchase(sku)
    }

    override fun onUserDataResponse(response: UserDataResponse) {
        Log.d(TAG, "User data response: ${response.requestStatus}")
        when (response.requestStatus) {
            UserDataResponse.RequestStatus.SUCCESSFUL -> {
                val userData = response.userData
                userId = userData.userId
                Log.d(TAG, """
                    User Data:
                    User ID: $userId
                    Marketplace: ${userData.marketplace}
                    """.trimIndent())
                loadProducts()
                PurchasingService.getPurchaseUpdates(false)
            }
            UserDataResponse.RequestStatus.NOT_SUPPORTED -> {
                Log.e(TAG, "User data not supported")
                callback.onBillingError("IAP not supported on this device")
            }
            else -> {
                Log.e(TAG, "Failed to get user data: ${response.requestStatus}")
                callback.onBillingError("Failed to initialize billing")
            }
        }
    }

    override fun onPurchaseResponse(response: PurchaseResponse) {
        Log.d(TAG, "Purchase response: ${response.requestStatus}")
        when (response.requestStatus) {
            PurchaseResponse.RequestStatus.SUCCESSFUL -> {
                val receipt = response.receipt
                if (receipt.isCanceled) {
                    Log.d(TAG, "Purchase was cancelled: ${receipt.sku}")
                    return
                }
                Log.d(TAG, "Purchase successful: ${receipt.sku}")
                callback.onPurchaseSuccess(receipt.sku)
                // Notify Amazon that we've fulfilled the purchase
                PurchasingService.notifyFulfillment(
                    receipt.receiptId,
                    FulfillmentResult.FULFILLED
                )
            }
            PurchaseResponse.RequestStatus.ALREADY_PURCHASED -> {
                Log.d(TAG, "Item already purchased")
                callback.onBillingError("You already own this item")
            }
            PurchaseResponse.RequestStatus.INVALID_SKU -> {
                Log.e(TAG, "Invalid SKU")
                callback.onBillingError("Invalid product")
            }
            PurchaseResponse.RequestStatus.FAILED -> {
                Log.e(TAG, "Purchase failed")
                callback.onBillingError("Purchase failed")
            }
            else -> {
                Log.e(TAG, "Unknown purchase response: ${response.requestStatus}")
                callback.onBillingError("Unknown error occurred")
            }
        }
    }

    override fun onPurchaseUpdatesResponse(response: PurchaseUpdatesResponse) {
        Log.d(TAG, "Purchase updates response: ${response.requestStatus}")
        when (response.requestStatus) {
            PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL -> {
                // Handle receipts from previous purchases
                response.receipts.forEach { receipt ->
                    Log.d(TAG, "Previous purchase: ${receipt.sku}")
                    if (!receipt.isCanceled) {
                        callback.onPurchaseSuccess(receipt.sku)
                        PurchasingService.notifyFulfillment(
                            receipt.receiptId,
                            FulfillmentResult.FULFILLED
                        )
                    }
                }
                
                // Check if more purchase updates are available
                if (response.hasMore()) {
                    PurchasingService.getPurchaseUpdates(false)
                }
            }
            PurchaseUpdatesResponse.RequestStatus.FAILED -> {
                Log.e(TAG, "Failed to get purchase updates")
                callback.onBillingError("Failed to restore purchases")
            }
            else -> Log.e(TAG, "Unknown purchase updates response: ${response.requestStatus}")
        }
    }

    fun restorePurchases() {
        Log.d(TAG, "Restoring purchases")
        PurchasingService.getPurchaseUpdates(true)
    }

    fun getProductPrice(sku: String): String? {
        return productDataMap[sku]?.price
    }

    fun isProductPurchased(sku: String): Boolean {
        // Implementation depends on how you track purchased items
        // You might want to maintain a set of purchased SKUs
        return false // TODO: Implement purchase status tracking
    }

    // Add method to check if products are available
    fun hasProducts(): Boolean {
        return productDataMap.isNotEmpty()
    }
} 