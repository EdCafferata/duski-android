package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

/**
 * Golven: laagdoorgelaten ruis met een trage, golvende amplitude — het
 * aanrollen-en-terugtrekken-ritme van oceaangolven op een strand.
 */
class GolvenGenerator : GeluidGenerator {
    private var laagdoorlaatToestand = 0f
    private var faseGolf = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f

        // Laagdoorlaatfilter voor een dieper, doffer geluid dan regen.
        laagdoorlaatToestand += 0.04f * (wit - laagdoorlaatToestand)

        // Golfritme: een aanrollende golf duurt ongeveer 6-8 seconden.
        faseGolf += (1.0 / sampleRate).toFloat() * 0.14f
        val golf = 0.5f + 0.5f * sin(faseGolf * 2f * PI.toFloat())
        // Golven rollen sneller aan dan ze terugtrekken — asymmetrische curve.
        val asymmetrisch = golf.toDouble().pow(1.6).toFloat()

        return laagdoorlaatToestand * (0.3f + 0.7f * asymmetrisch)
    }
}
