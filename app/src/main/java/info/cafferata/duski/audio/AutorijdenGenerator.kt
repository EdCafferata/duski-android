package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Autorijden: het gelijkmatige motorgebrom en bandengeruis van een rijdende
 * auto op de snelweg, met af en toe een korte hobbel/voegovergang — een
 * bekend geluid waar veel mensen (en baby's) van in slaap vallen.
 */
class AutorijdenGenerator : GeluidGenerator {
    private var faseMotor = 0f
    private var wegToestand = 0f
    private var hobbelEnergie = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        faseMotor += (1.0 / sampleRate).toFloat() * 45f
        val motor = sin(faseMotor * 2f * PI.toFloat()) * 0.2f

        val wit = Random.nextFloat() * 2f - 1f
        wegToestand += 0.1f * (wit - wegToestand)
        val weggeluid = wegToestand * 0.5f

        if (Random.nextFloat() < 0.0008f) {
            hobbelEnergie = Random.nextFloat() * 0.4f + 0.3f
        }
        hobbelEnergie *= 0.97f
        val hobbel = wit * hobbelEnergie * 0.3f

        return motor + weggeluid + hobbel
    }
}
