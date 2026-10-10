package info.cafferata.duski.model

import androidx.annotation.StringRes
import info.cafferata.duski.R

/**
 * De geluidscategorieën, gegroepeerd zoals bij de meeste succesvolle slaap-apps
 * (Noisli, BetterSleep): losse geluidslagen die je samen kan mixen, elk met eigen volume.
 */
enum class GeluidCategorie(@StringRes val titelRes: Int) {
    KLEURRUIS(R.string.categorie_kleurruis),
    NATUUR(R.string.categorie_natuur),
    LICHAAM(R.string.categorie_lichaam),
    OVERIGE(R.string.categorie_overige);

    val opties: List<GeluidOptie>
        get() = when (this) {
            KLEURRUIS -> listOf(
                GeluidOptie("wit", R.string.geluid_wit, "📻", GeluidType.Ruis(RuisKleur.WIT)),
                GeluidOptie("roze", R.string.geluid_roze, "🌸", GeluidType.Ruis(RuisKleur.ROZE)),
                GeluidOptie("bruin", R.string.geluid_bruin, "🟤", GeluidType.Ruis(RuisKleur.BRUIN)),
                GeluidOptie("grijs", R.string.geluid_grijs, "🩶", GeluidType.Ruis(RuisKleur.GRIJS)),
                GeluidOptie("blauw", R.string.geluid_blauw, "🔵", GeluidType.Ruis(RuisKleur.BLAUW)),
            )
            NATUUR -> listOf(
                GeluidOptie("regen", R.string.geluid_regen, "🌧️", GeluidType.Regen),
                GeluidOptie("golven", R.string.geluid_golven, "🌊", GeluidType.Golven),
                GeluidOptie("wind", R.string.geluid_wind, "🍃", GeluidType.Wind),
                GeluidOptie("vuur", R.string.geluid_vuur, "🔥", GeluidType.Vuur),
                GeluidOptie("beek", R.string.geluid_beek, "🏞️", GeluidType.Beek),
            )
            LICHAAM -> listOf(
                GeluidOptie("hartslag", R.string.geluid_hartslag, "❤️", GeluidType.Hartslag),
                GeluidOptie("baarmoeder", R.string.geluid_baarmoeder, "🤰", GeluidType.Baarmoeder),
                GeluidOptie("ademhaling", R.string.geluid_ademhaling, "🫁", GeluidType.Ademhaling),
                GeluidOptie("sussen", R.string.geluid_sussen, "🤫", GeluidType.Sussen),
                GeluidOptie("fohn", R.string.geluid_fohn, "💨", GeluidType.Fohn),
            )
            OVERIGE -> listOf(
                GeluidOptie("klankschaal", R.string.geluid_klankschaal, "🎐", GeluidType.Klankschaal),
                GeluidOptie("ventilator", R.string.geluid_ventilator, "🌀", GeluidType.Ventilator),
                GeluidOptie("trein", R.string.geluid_trein, "🚂", GeluidType.Trein),
                GeluidOptie("klok", R.string.geluid_klok, "🕰️", GeluidType.Klok),
                GeluidOptie("vliegtuigcabine", R.string.geluid_vliegtuigcabine, "✈️", GeluidType.Vliegtuigcabine),
                GeluidOptie("autorijden", R.string.geluid_autorijden, "🚗", GeluidType.Autorijden),
                GeluidOptie("bachprelude", R.string.geluid_bachprelude, "🎹", GeluidType.BachPrelude),
                GeluidOptie("bachair", R.string.geluid_bachair, "🎻", GeluidType.BachAir),
                GeluidOptie("canon", R.string.geluid_canon, "🎼", GeluidType.Canon),
                GeluidOptie("gymnopedie", R.string.geluid_gymnopedie, "🎶", GeluidType.Gymnopedie),
                GeluidOptie("clairdelune", R.string.geluid_clairdelune, "🌙", GeluidType.ClairDeLune),
            )
        }
}

/**
 * Eén losse geluidslaag. Per categorie is precies één geluid altijd gratis (de
 * instap), de rest zit achter Premium — zelfde knip als de iOS-app.
 */
data class GeluidOptie(
    val id: String,
    @StringRes val titelRes: Int,
    val emoji: String,
    val type: GeluidType,
) {
    val isPremium: Boolean get() = id !in GRATIS_GELUIDEN

    companion object {
        val GRATIS_GELUIDEN = setOf("wit", "regen", "hartslag", "klankschaal")
    }
}

sealed class GeluidType {
    data class Ruis(val kleur: RuisKleur) : GeluidType()
    object Regen : GeluidType()
    object Golven : GeluidType()
    object Wind : GeluidType()
    object Vuur : GeluidType()
    object Beek : GeluidType()
    object Hartslag : GeluidType()
    object Baarmoeder : GeluidType()
    object Ademhaling : GeluidType()
    object Sussen : GeluidType()
    object Fohn : GeluidType()
    object Klankschaal : GeluidType()
    object Ventilator : GeluidType()
    object Trein : GeluidType()
    object Klok : GeluidType()
    object Vliegtuigcabine : GeluidType()
    object Autorijden : GeluidType()
    object BachPrelude : GeluidType()
    object BachAir : GeluidType()
    object Canon : GeluidType()
    object Gymnopedie : GeluidType()
    object ClairDeLune : GeluidType()
}

enum class RuisKleur { WIT, ROZE, BRUIN, GRIJS, BLAUW }
