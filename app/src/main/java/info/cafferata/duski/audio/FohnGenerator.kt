package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Föhn: het motorgezoem (vaste lage toon) plus luchtstroom-ruis van een föhn of
 * stofzuiger — het klassieke "witte-ruis-apparaat"-geluid waarmee veel ouders
 * baby's in slaap sussen.
 */
class FohnGenerator : GeluidGenerator {
    private var faseMotor = 0f
    private var laagdoorlaatToestand = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        faseMotor += (1.0 / sampleRate).toFloat() * 100f
        val motor = sin(faseMotor * 2f * PI.toFloat()) * 0.25f

        val wit = Random.nextFloat() * 2f - 1f
        laagdoorlaatToestand += 0.25f * (wit - laagdoorlaatToestand)
        val luchtstroom = laagdoorlaatToestand * 0.6f

        return motor + luchtstroom
    }
}
