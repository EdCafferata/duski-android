package info.cafferata.duski.audio

import info.cafferata.duski.model.RuisKleur
import kotlin.random.Random

/**
 * Genereert witte, roze, bruine, grijze of blauwe ruis. Roze via Paul Kellet's
 * bekende "economy"-filtercascade (publiek algoritme, geen licentie nodig);
 * bruine ruis via een lekkende integrator van witte ruis; grijze ruis via een
 * smal middenband-filter (het gebied waar het gehoor het gevoeligst is); blauwe
 * ruis via een eerste-orde verschil (het spiegelbeeld van bruine ruis: meer
 * energie in de hoge tonen in plaats van de lage).
 */
class RuisGenerator(private val kleur: RuisKleur) : GeluidGenerator {

    // Toestand voor roze ruis (Paul Kellet economy method).
    private var b0 = 0f
    private var b1 = 0f
    private var b2 = 0f

    // Toestand voor bruine ruis (lekkende integrator).
    private var bruinToestand = 0f

    // Toestand voor grijze ruis (bandfilter: hoogdoorlaat gevolgd door laagdoorlaat).
    private var grijsHoogToestand = 0f
    private var grijsLaagToestand = 0f

    // Toestand voor blauwe ruis (eerste-orde verschilfilter).
    private var blauwVorige = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f

        return when (kleur) {
            RuisKleur.WIT -> wit * 0.5f

            RuisKleur.ROZE -> {
                b0 = 0.99765f * b0 + wit * 0.0990460f
                b1 = 0.96300f * b1 + wit * 0.2965164f
                b2 = 0.57000f * b2 + wit * 1.0526913f
                val roze = b0 + b1 + b2 + wit * 0.1848f
                roze * 0.11f
            }

            RuisKleur.BRUIN -> {
                bruinToestand = (bruinToestand + (0.02f * wit)) / 1.02f
                bruinToestand * 3.5f
            }

            RuisKleur.GRIJS -> {
                grijsHoogToestand += 0.35f * (wit - grijsHoogToestand)
                val hoog = wit - grijsHoogToestand
                grijsLaagToestand += 0.5f * (hoog - grijsLaagToestand)
                grijsLaagToestand * 1.8f
            }

            RuisKleur.BLAUW -> {
                val blauw = wit - blauwVorige
                blauwVorige = wit
                blauw * 0.5f
            }
        }
    }
}
