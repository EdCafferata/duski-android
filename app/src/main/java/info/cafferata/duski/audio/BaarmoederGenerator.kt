package info.cafferata.duski.audio

import kotlin.random.Random

/**
 * Baarmoedergeluiden: een gedempte, laagdoorgelaten "whoosh" (vruchtwater/
 * bloedstroom) met een moederhartslag erdoorheen — de intra-uteriene
 * geluidsomgeving die witte-ruismachines voor baby's proberen na te bootsen.
 */
class BaarmoederGenerator : GeluidGenerator {
    private var laagdoorlaatToestand = 0f
    private val hartslag = HartslagGenerator(slagenPerMinuut = 78.0)

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f

        // Zwaar gedempt: alleen de allerlaagste frequenties komen door.
        laagdoorlaatToestand += 0.03f * (wit - laagdoorlaatToestand)
        val whoosh = laagdoorlaatToestand * 1.5f

        val hart = hartslag.volgendeSample(sampleRate) * 0.4f

        return whoosh * 0.7f + hart
    }
}
