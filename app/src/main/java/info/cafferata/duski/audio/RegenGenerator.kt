package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Regen: hoogdoorgelaten ruis (het "gesis") met willekeurige druppel-transiënten
 * erbovenop, plus een trage volume-ademhaling zodat het niet helemaal statisch klinkt.
 */
class RegenGenerator : GeluidGenerator {
    private var hoogdoorlaatToestand = 0f
    private var druppelEnergie = 0f
    private var faseAdemhaling = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f

        // Eenvoudig eerste-orde hoogdoorlaatfilter voor het "sissende" regengeluid.
        val hoogdoorlaat = wit - hoogdoorlaatToestand
        hoogdoorlaatToestand += 0.15f * hoogdoorlaat

        // Willekeurige druppel-transiënten: af en toe een kort energiestootje.
        if (Random.nextFloat() < 0.0025f) {
            druppelEnergie = Random.nextFloat() * 0.7f + 0.3f
        }
        druppelEnergie *= 0.995f
        val druppels = wit * druppelEnergie * 0.6f

        // Trage amplitude-ademhaling (regenbuien-gevoel).
        faseAdemhaling += (1.0 / sampleRate).toFloat() * 0.07f
        val ademhaling = 0.75f + 0.25f * sin(faseAdemhaling * 2f * PI.toFloat())

        return (hoogdoorlaat * 0.5f + druppels) * ademhaling * 0.5f
    }
}
