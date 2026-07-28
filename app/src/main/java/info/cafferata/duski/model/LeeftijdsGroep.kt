package info.cafferata.duski.model

/**
 * Leeftijdsgroepen zoals onderscheiden in AASM/WHO/Sleep Foundation-richtlijnen.
 * Bepaalt welke content en veiligheidsgrenzen (bv. volumelimiet bij Baby) worden getoond.
 */
enum class LeeftijdsGroep(val titel: String, val emoji: String) {
    BABY("Baby (0-1 jaar)", "👶"),
    KIND("Kind (1-12 jaar)", "🧒"),
    TIENER("Tiener (13-17 jaar)", "🧑"),
    VOLWASSENE("Volwassene (18-64 jaar)", "🧑"),
    OUDERE("Oudere (65+ jaar)", "🧓");

    /**
     * Baby-modus: onderzoek toont dat alle geteste witte-ruismachines op vol volume
     * de ziekenhuis-veilige geluidsniveaus overschreden — daarom een harde limiet.
     */
    val maximaalVolume: Float
        get() = if (this == BABY) 0.5f else 1.0f
}
