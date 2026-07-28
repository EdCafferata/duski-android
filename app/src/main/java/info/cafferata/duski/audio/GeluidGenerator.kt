package info.cafferata.duski.audio

import info.cafferata.duski.model.GeluidType

/**
 * Elke geluidslaag genereert zijn eigen audio sample-voor-sample — 100% zelf
 * geprogrammeerd, geen opnames of samples van derden nodig (geen rechtenrisico).
 */
interface GeluidGenerator {
    fun volgendeSample(sampleRate: Double): Float
}

/** Bouwt de juiste generator voor een gegeven geluidstype. */
object GeluidGeneratorFabriek {
    fun maak(type: GeluidType): GeluidGenerator = when (type) {
        is GeluidType.Ruis -> RuisGenerator(type.kleur)
        GeluidType.Regen -> RegenGenerator()
        GeluidType.Golven -> GolvenGenerator()
        GeluidType.Wind -> WindGenerator()
        GeluidType.Vuur -> VuurGenerator()
        GeluidType.Beek -> BeekGenerator()
        GeluidType.Hartslag -> HartslagGenerator()
        GeluidType.Baarmoeder -> BaarmoederGenerator()
        GeluidType.Ademhaling -> AdemhalingGenerator()
        GeluidType.Sussen -> SussenGenerator()
        GeluidType.Fohn -> FohnGenerator()
        GeluidType.Klankschaal -> KlankschaalGenerator()
        GeluidType.Ventilator -> VentilatorGenerator()
        GeluidType.Trein -> TreinGenerator()
        GeluidType.Klok -> KlokGenerator()
        GeluidType.Vliegtuigcabine -> VliegtuigcabineGenerator()
        GeluidType.Autorijden -> AutorijdenGenerator()
        GeluidType.BachPrelude -> BachPreludeGenerator()
        GeluidType.BachAir -> BachAirGenerator()
        GeluidType.Canon -> CanonGenerator()
        GeluidType.Gymnopedie -> GymnopedieGenerator()
        GeluidType.ClairDeLune -> ClairDeLuneGenerator()
    }
}
