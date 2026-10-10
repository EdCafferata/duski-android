package info.cafferata.duski.model

import androidx.annotation.StringRes
import info.cafferata.duski.R

/**
 * Leeftijdsgroepen zoals onderscheiden in AASM/WHO/Sleep Foundation-richtlijnen.
 * Bepaalt welke content en veiligheidsgrenzen (bv. volumelimiet bij Baby) worden getoond.
 */
enum class LeeftijdsGroep(@StringRes val titelRes: Int, val emoji: String) {
    BABY(R.string.leeftijd_baby, "👶"),
    KIND(R.string.leeftijd_kind, "🧒"),
    TIENER(R.string.leeftijd_tiener, "🧑"),
    VOLWASSENE(R.string.leeftijd_volwassene, "🧑"),
    OUDERE(R.string.leeftijd_oudere, "🧓");

    /**
     * Baby-modus: onderzoek toont dat alle geteste witte-ruismachines op vol volume
     * de ziekenhuis-veilige geluidsniveaus overschreden — daarom een harde limiet.
     */
    val maximaalVolume: Float
        get() = if (this == BABY) 0.5f else 1.0f
}
