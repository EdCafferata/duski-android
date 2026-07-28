package info.cafferata.duski.model

/**
 * De geluidscategorieën, gegroepeerd zoals bij de meeste succesvolle slaap-apps
 * (Noisli, BetterSleep): losse geluidslagen die je samen kan mixen, elk met eigen volume.
 */
enum class GeluidCategorie(val titel: String) {
    KLEURRUIS("Ruis"),
    NATUUR("Natuur"),
    LICHAAM("Lichaam & baby"),
    OVERIGE("Overige");

    val opties: List<GeluidOptie>
        get() = when (this) {
            KLEURRUIS -> listOf(
                GeluidOptie("wit", "Witte ruis", "📻", GeluidType.Ruis(RuisKleur.WIT)),
                GeluidOptie("roze", "Roze ruis", "🌸", GeluidType.Ruis(RuisKleur.ROZE)),
                GeluidOptie("bruin", "Bruine ruis", "🟤", GeluidType.Ruis(RuisKleur.BRUIN)),
                GeluidOptie("grijs", "Grijze ruis", "🩶", GeluidType.Ruis(RuisKleur.GRIJS)),
                GeluidOptie("blauw", "Blauwe ruis", "🔵", GeluidType.Ruis(RuisKleur.BLAUW)),
            )
            NATUUR -> listOf(
                GeluidOptie("regen", "Regen", "🌧️", GeluidType.Regen),
                GeluidOptie("golven", "Golven", "🌊", GeluidType.Golven),
                GeluidOptie("wind", "Wind", "🍃", GeluidType.Wind),
                GeluidOptie("vuur", "Kampvuur", "🔥", GeluidType.Vuur),
                GeluidOptie("beek", "Beek", "🏞️", GeluidType.Beek),
            )
            LICHAAM -> listOf(
                GeluidOptie("hartslag", "Hartslag", "❤️", GeluidType.Hartslag),
                GeluidOptie("baarmoeder", "Baarmoedergeluiden", "🤰", GeluidType.Baarmoeder),
                GeluidOptie("ademhaling", "Ademhaling", "🫁", GeluidType.Ademhaling),
                GeluidOptie("sussen", "Sussen (shhh)", "🤫", GeluidType.Sussen),
                GeluidOptie("fohn", "Föhn", "💨", GeluidType.Fohn),
            )
            OVERIGE -> listOf(
                GeluidOptie("klankschaal", "Klankschaal", "🎐", GeluidType.Klankschaal),
                GeluidOptie("ventilator", "Ventilator", "🌀", GeluidType.Ventilator),
                GeluidOptie("trein", "Trein", "🚂", GeluidType.Trein),
                GeluidOptie("klok", "Tikkende klok", "🕰️", GeluidType.Klok),
                GeluidOptie("vliegtuigcabine", "Vliegtuigcabine", "✈️", GeluidType.Vliegtuigcabine),
                GeluidOptie("autorijden", "Autorijden", "🚗", GeluidType.Autorijden),
                GeluidOptie("bachprelude", "Bach – Prelude in C", "🎹", GeluidType.BachPrelude),
                GeluidOptie("bachair", "Bach – Air", "🎻", GeluidType.BachAir),
                GeluidOptie("canon", "Pachelbel – Canon in D", "🎼", GeluidType.Canon),
                GeluidOptie("gymnopedie", "Satie – Gymnopédie", "🎶", GeluidType.Gymnopedie),
                GeluidOptie("clairdelune", "Debussy – Clair de Lune", "🌙", GeluidType.ClairDeLune),
            )
        }
}

/** Eén losse, mixbare geluidslaag. */
data class GeluidOptie(
    val id: String,
    val titel: String,
    val emoji: String,
    val type: GeluidType,
)

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
