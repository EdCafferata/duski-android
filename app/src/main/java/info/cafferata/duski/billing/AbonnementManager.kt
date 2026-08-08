package info.cafferata.duski.billing

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.ProductDetails

/**
 * Play Billing-wrapper (Android-equivalent van de StoreKit 2 AbonnementManager
 * op iOS): laadt het abonnement, koopt het, en houdt bij of Premium actief is.
 * 30 dagen gratis via een introductory offer op het abonnement zelf
 * (geconfigureerd in Play Console > Abonnementen), daarna de ingestelde prijs.
 */
class AbonnementManager(context: Context) : PurchasesUpdatedListener {
    private val _producten = mutableStateOf<List<ProductDetails>>(emptyList())
    val producten: State<List<ProductDetails>> = _producten

    private val _heeftPremium = mutableStateOf(false)
    val heeftPremium: State<Boolean> = _heeftPremium

    private val _laadFout = mutableStateOf<String?>(null)
    val laadFout: State<String?> = _laadFout

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            com.android.billingclient.api.PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    laadProducten()
                    werkAbonnementStatusBij()
                } else {
                    _laadFout.value = result.debugMessage
                }
            }

            override fun onBillingServiceDisconnected() {
                // Volgende actie van de gebruiker triggert een herverbinding via startConnection.
            }
        })
    }

    fun laadProducten() {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(AbonnementProductID.PREMIUM_MAANDELIJKS)
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { result, queryProductDetailsResult ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                _producten.value = queryProductDetailsResult.productDetailsList
            } else {
                _laadFout.value = result.debugMessage
            }
        }
    }

    fun koop(activity: Activity, product: ProductDetails) {
        val aanbiedingToken = product.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: return

        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(product)
            .setOfferToken(aanbiedingToken)
            .build()

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParams))
            .build()

        billingClient.launchBillingFlow(activity, flowParams)
    }

    fun werkAbonnementStatusBij() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { result, purchases ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                _laadFout.value = result.debugMessage
                return@queryPurchasesAsync
            }
            val actief = purchases.any { purchase ->
                purchase.products.contains(AbonnementProductID.PREMIUM_MAANDELIJKS) &&
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            }
            _heeftPremium.value = actief

            for (purchase in purchases) {
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                    val acknowledgeParams = com.android.billingclient.api.AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(acknowledgeParams) { _ -> }
                }
            }
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            werkAbonnementStatusBij()
        } else if (result.responseCode != BillingClient.BillingResponseCode.USER_CANCELED) {
            _laadFout.value = result.debugMessage
        }
    }
}
