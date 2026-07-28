package info.cafferata.duski.audio

import kotlin.math.exp
import kotlin.random.Random

/**
 * Trein: het ritmische "tsjoek-tsjoek" van wielen over railstoten, met een
 * laagfrequent motorgerommel eronder — het monotone treinreisgeluid waarvan
 * bekend is dat het mensen in slaap sust.
 */
class TreinGenerator : GeluidGenerator {
    private var laagdoorlaatToestand = 0f
    private var tijdSindsTik = 0.0
    private val tikInterval = 0.55

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f
        laagdoorlaatToestand += 0.05f * (wit - laagdoorlaatToestand)

        tijdSindsTik += 1.0 / sampleRate
        if (tijdSindsTik >= tikInterval) {
            tijdSindsTik -= tikInterval
        }
        val envelope = exp(-tijdSindsTik * 25).toFloat()
        val tik = wit * envelope * 0.6f

        return laagdoorlaatToestand * 0.8f + tik
    }
}
