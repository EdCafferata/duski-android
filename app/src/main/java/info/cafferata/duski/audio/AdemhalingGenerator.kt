package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

/**
 * Ademhaling: laagdoorgelaten ruis (de "lucht" van in- en uitademen) met een
 * trage amplitude-envelope die het inademen-uitademen-ritme volgt (~6 seconden
 * per cyclus, een rustig ademtempo). Inademen bouwt iets sneller op dan
 * uitademen afbouwt — asymmetrisch, net als een echte ademhaling.
 */
class AdemhalingGenerator : GeluidGenerator {
    private var laagdoorlaatToestand = 0f
    private var faseCyclus = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f
        laagdoorlaatToestand += 0.08f * (wit - laagdoorlaatToestand)

        faseCyclus += (1.0 / sampleRate).toFloat() * 0.17f
        val raw = 0.5f + 0.5f * sin(faseCyclus * 2f * PI.toFloat())
        val envelope = raw.toDouble().pow(1.4).toFloat()

        return laagdoorlaatToestand * (0.2f + 0.8f * envelope) * 0.7f
    }
}
