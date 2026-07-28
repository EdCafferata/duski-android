package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Wind: breedbandruis met een trage, onregelmatige sterkte-modulatie (vlagen),
 * iets hoger gefilterd dan golven zodat het als "lucht" en niet als "water" klinkt.
 */
class WindGenerator : GeluidGenerator {
    private var bandToestand = 0f
    private var vlaagFase = 0f
    private var vlaagSterkte = 0.6f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f
        bandToestand += 0.09f * (wit - bandToestand)

        vlaagFase += (1.0 / sampleRate).toFloat() * 0.05f
        if (Random.nextFloat() < 0.001f) {
            vlaagSterkte = Random.nextFloat() * 0.7f + 0.3f
        }
        val vlaag = 0.5f + 0.5f * sin(vlaagFase * 2f * PI.toFloat()) * vlaagSterkte

        return bandToestand * (0.4f + 0.6f * vlaag)
    }
}
