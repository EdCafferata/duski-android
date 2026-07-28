package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Vliegtuigcabine: het diepe, zeer stabiele motorgerommel van een cabine op
 * kruishoogte — extreem laagdoorgelaten ruis met nauwelijks modulatie, bekend
 * als een van de meest gebruikte "in-slaap-sussende" geluiden.
 */
class VliegtuigcabineGenerator : GeluidGenerator {
    private var laagdoorlaatToestand = 0f
    private var faseTrilling = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val wit = Random.nextFloat() * 2f - 1f
        laagdoorlaatToestand += 0.015f * (wit - laagdoorlaatToestand)

        faseTrilling += (1.0 / sampleRate).toFloat() * 0.03f
        val trilling = 0.95f + 0.05f * sin(faseTrilling * 2f * PI.toFloat())

        return laagdoorlaatToestand * 2.2f * trilling
    }
}
