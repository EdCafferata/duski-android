package info.cafferata.duski.audio

import kotlin.random.Random

/**
 * Kampvuur/haard: laagfrequent gerommel (het "vuur") met willekeurige knetter-
 * transiënten erbovenop (brandend hout).
 */
class VuurGenerator : GeluidGenerator {
    private var laagdoorlaatToestand = 0f
    private var knetterEnergie = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f
        laagdoorlaatToestand += 0.06f * (wit - laagdoorlaatToestand)

        if (Random.nextFloat() < 0.0012f) {
            knetterEnergie = Random.nextFloat() * 0.5f + 0.5f
        }
        knetterEnergie *= 0.98f
        val knetter = wit * knetterEnergie * 0.7f

        return laagdoorlaatToestand * 0.9f + knetter
    }
}
