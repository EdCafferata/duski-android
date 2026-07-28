package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Klankschaal: additieve synthese van een grondtoon + boventonen (licht verstemd
 * voor het karakteristieke "zwevende" klankschaal-timbre), die na een zachte
 * aanslag langzaam wegsterft en zichzelf daarna opnieuw aanslaat.
 */
class KlankschaalGenerator : GeluidGenerator {
    private val grondtoon = 136.1 // traditionele "Om"-frequentie
    private val boventoonVerhoudingen = doubleArrayOf(1.0, 2.005, 2.995, 4.02)
    private val boventoonSterktes = doubleArrayOf(1.0, 0.5, 0.3, 0.15)

    private var faseSindsAanslag = 0.0
    private var volgendeAanslagOver = 0.0
    private val oscillatorFases = DoubleArray(boventoonVerhoudingen.size)

    override fun volgendeSample(sampleRate: Double): Float {
        val dt = 1.0 / sampleRate
        faseSindsAanslag += dt
        volgendeAanslagOver -= dt

        if (volgendeAanslagOver <= 0) {
            faseSindsAanslag = 0.0
            volgendeAanslagOver = Random.nextDouble(9.0, 14.0)
        }

        val envelope = exp(-faseSindsAanslag * 0.35)

        var signaal = 0.0
        for (index in boventoonVerhoudingen.indices) {
            oscillatorFases[index] += grondtoon * boventoonVerhoudingen[index] * dt
            if (oscillatorFases[index] > 1) oscillatorFases[index] -= 1
            signaal += sin(2 * PI * oscillatorFases[index]) * boventoonSterktes[index]
        }

        return (signaal * envelope * 0.15).toFloat()
    }
}
