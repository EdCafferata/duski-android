package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Ventilator: motorzoem plus het ritmische "wiek-effect" van ronddraaiende
 * ventilatorbladen — net als Föhn een klassiek witte-ruis-apparaat-geluid, maar
 * dieper en met een tragere, mechanische puls.
 */
class VentilatorGenerator : GeluidGenerator {
    private var faseMotor = 0f
    private var faseWiek = 0f
    private var laagdoorlaatToestand = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        faseMotor += (1.0 / sampleRate).toFloat() * 60f
        val motor = sin(faseMotor * 2f * PI.toFloat()) * 0.15f

        faseWiek += (1.0 / sampleRate).toFloat() * 7f
        val wiek = 0.7f + 0.3f * sin(faseWiek * 2f * PI.toFloat())

        val wit = Random.nextFloat() * 2f - 1f
        laagdoorlaatToestand += 0.2f * (wit - laagdoorlaatToestand)

        return (motor + laagdoorlaatToestand * 0.5f) * wiek
    }
}
