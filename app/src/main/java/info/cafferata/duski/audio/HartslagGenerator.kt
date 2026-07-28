package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Hartslag: het klassieke "lub-dub"-tweeklankritme, rond rusthartslag (65 bpm) —
 * exact het tempo dat onderzoek als meest ontspannend voor slaapmuziek aanwijst.
 */
class HartslagGenerator(private val slagenPerMinuut: Double = 65.0) : GeluidGenerator {
    private var faseInCyclus = 0.0

    override fun volgendeSample(sampleRate: Double): Float {
        val cyclusDuur = 60.0 / slagenPerMinuut
        faseInCyclus += 1.0 / sampleRate
        if (faseInCyclus >= cyclusDuur) {
            faseInCyclus -= cyclusDuur
        }

        val lub = puls(faseInCyclus, 0.0, 1.0)
        val dub = puls(faseInCyclus, cyclusDuur * 0.18, 0.6)

        return ((lub + dub) * 0.8).toFloat()
    }

    /** Eén "bonk": een korte laagfrequente toon met snelle exponentiële uitsterving. */
    private fun puls(fase: Double, start: Double, sterkte: Double): Double {
        val tijdSindsStart = fase - start
        if (tijdSindsStart < 0 || tijdSindsStart >= 0.12) return 0.0
        val envelope = exp(-tijdSindsStart * 40)
        val toon = sin(2 * PI * 55 * tijdSindsStart)
        return toon * envelope * sterkte
    }
}
