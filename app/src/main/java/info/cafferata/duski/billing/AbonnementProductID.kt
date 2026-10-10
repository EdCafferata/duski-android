package info.cafferata.duski.billing

/**
 * Subscription-ID's zoals aangemaakt in Play Console > Abonnementen. Jaar en
 * maand zijn twee losse abonnementen (niet twee base plans van één product),
 * zodat oudere app-versies — die alleen het maandabonnement opvragen — er
 * geen last van hebben.
 */
object AbonnementProductID {
    const val PREMIUM_MAANDELIJKS = "premium_maandelijks"
    const val PREMIUM_JAARLIJKS = "premium_jaarlijks"

    /** Volgorde = volgorde op de paywall: jaar eerst (voordeligst), dan maand. */
    val ALLE = listOf(PREMIUM_JAARLIJKS, PREMIUM_MAANDELIJKS)
}
