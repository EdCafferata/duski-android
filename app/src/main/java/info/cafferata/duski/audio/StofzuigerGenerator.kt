package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Stofzuiger: hoger en voller dan de föhn — motortoon met boventonen, een licht fluitende
 * luchtstroom in het midden en een brede ruisdeken eronder. Zelfde algoritme als iOS.
 */
class StofzuigerGenerator : GeluidGenerator {
    private var faseMotor = 0f
    private var faseZweef = 0f
    private var laag = 0f
    private var band1 = 0f
    private var band2 = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val dt = (1.0 / sampleRate).toFloat()
        val tweePi = 2f * PI.toFloat()

        faseZweef += dt * 0.05f
        if (faseZweef >= 1f) faseZweef -= 1f // fase binnen 0..<1 houden
        val toon = 165f * (1f + 0.01f * sin(faseZweef * tweePi))

        faseMotor += dt * toon
        if (faseMotor >= 1f) faseMotor -= 1f // fase binnen 0..<1 houden
        val p = faseMotor * tweePi
        val motor = sin(p) * 0.10f + sin(2 * p) * 0.06f + sin(3 * p) * 0.03f

        val wit = Random.nextFloat() * 2f - 1f
        laag += 0.35f * (wit - laag)
        band1 += 0.5f * (wit - band1)
        val hoog = wit - band1
        band2 += 0.45f * (hoog - band2)

        return motor + laag * 0.45f + band2 * 0.35f
    }
}
