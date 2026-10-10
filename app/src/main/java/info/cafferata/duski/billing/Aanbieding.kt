package info.cafferata.duski.billing

import com.android.billingclient.api.ProductDetails

/**
 * Het aanbod dat we voor één abonnement tonen en verkopen: bij voorkeur het
 * aanbod met gratis proefperiode (Play geeft dat alleen terug als de gebruiker
 * er recht op heeft), anders het gewone base plan.
 */
data class Aanbieding(
    val product: ProductDetails,
    val offerToken: String,
    /** Terugkerende prijs na een eventuele proefperiode, bv. "€ 9,99". */
    val prijs: String,
    val heeftGratisProef: Boolean,
) {
    val isJaarabonnement: Boolean get() = product.productId == AbonnementProductID.PREMIUM_JAARLIJKS
}

fun ProductDetails.besteAanbieding(): Aanbieding? {
    val aanbiedingen = subscriptionOfferDetails ?: return null
    fun heeftProef(a: ProductDetails.SubscriptionOfferDetails) =
        a.pricingPhases.pricingPhaseList.any { it.priceAmountMicros == 0L }

    val gekozen = aanbiedingen.firstOrNull(::heeftProef) ?: aanbiedingen.firstOrNull() ?: return null
    val prijs = gekozen.pricingPhases.pricingPhaseList.lastOrNull()?.formattedPrice ?: return null
    return Aanbieding(this, gekozen.offerToken, prijs, heeftProef(gekozen))
}
