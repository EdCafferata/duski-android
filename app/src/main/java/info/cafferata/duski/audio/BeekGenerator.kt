package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Beek: middenband-gefilterde ruis met een snel, "borrelend" dubbel-sinusritme
 * erbovenop — het kabbelen van stromend water over stenen, lichter en sneller
 * dan de trage golfbeweging van golven.
 */
class BeekGenerator : GeluidGenerator {
    private var bandToestand = 0f
    private var fase1 = 0f
    private var fase2 = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f
        bandToestand += 0.22f * (wit - bandToestand)

        val dt = (1.0 / sampleRate).toFloat()
        fase1 += dt * 3.1f
        fase2 += dt * 4.7f
        val kabbel = 0.5f + 0.25f * sin(fase1 * 2f * PI.toFloat()) + 0.25f * sin(fase2 * 2f * PI.toFloat())

        return bandToestand * (0.4f + 0.6f * kabbel) * 0.6f
    }
}
