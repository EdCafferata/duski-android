package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Sussen: het klassieke ouder-"shhh"-geluid — scherp hoogdoorgelaten ruis met
 * een zacht, herhalend ritme, zoals ouders van nature gebruiken om baby's te
 * kalmeren.
 */
class SussenGenerator : GeluidGenerator {
    private var hoogToestand = 0f
    private var faseRitme = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f
        hoogToestand += 0.5f * (wit - hoogToestand)
        val hoog = wit - hoogToestand

        faseRitme += (1.0 / sampleRate).toFloat() * 0.5f
        val ritme = 0.6f + 0.4f * sin(faseRitme * 2f * PI.toFloat())

        return hoog * ritme * 0.55f
    }
}
