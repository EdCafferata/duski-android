package info.cafferata.duski.model

import androidx.annotation.StringRes
import info.cafferata.duski.R

/**
 * De kant-en-klare "Voor jou"-mix bovenaan het hoofdscherm: één tik en er speelt meteen
 * iets dat past bij de leeftijdsgroep. Altijd gratis, ook met premium-lagen
 * (verbeterplan nov 2026, fase 2). Zelfde mixen als de iOS-app.
 */
data class VoorJouMix(
    @StringRes val titelRes: Int,
    @StringRes val uitlegRes: Int,
    val emoji: String,
    val lagen: List<Pair<String, Float>>,
) {
    companion object {
        const val ID = "voorjou"

        fun voor(groep: LeeftijdsGroep): VoorJouMix = when (groep) {
            LeeftijdsGroep.BABY -> VoorJouMix(R.string.voorjou_baby_titel, R.string.voorjou_baby_uitleg, "🤰",
                listOf("baarmoeder" to 0.6f, "sussen" to 0.3f))
            LeeftijdsGroep.KIND -> VoorJouMix(R.string.voorjou_kind_titel, R.string.voorjou_kind_uitleg, "🎠",
                listOf("speeldoos" to 0.6f, "regen" to 0.25f))
            LeeftijdsGroep.TIENER -> VoorJouMix(R.string.voorjou_tiener_titel, R.string.voorjou_tiener_uitleg, "🟤",
                listOf("bruin" to 0.6f, "regen" to 0.3f))
            LeeftijdsGroep.VOLWASSENE -> VoorJouMix(R.string.voorjou_volwassene_titel, R.string.voorjou_volwassene_uitleg, "⛈️",
                listOf("onweer" to 0.7f, "bruin" to 0.2f))
            LeeftijdsGroep.OUDERE -> VoorJouMix(R.string.voorjou_oudere_titel, R.string.voorjou_oudere_uitleg, "🌙",
                listOf("clairdelune" to 0.7f, "regen" to 0.2f))
        }
    }
}
