package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Tikkende klok: een regelmatig tik-tak-ritme (1 seconde per tik) zoals een
 * mechanische wandklok — voor wie graag bij een vertrouwd, voorspelbaar ritme
 * inslaapt.
 */
class KlokGenerator : GeluidGenerator {
    private var tijdSindsTik = 0.0
    private var tikIsTak = false
    private val tikInterval = 1.0

    override fun volgendeSample(sampleRate: Double): Float {
        tijdSindsTik += 1.0 / sampleRate
        if (tijdSindsTik >= tikInterval) {
            tijdSindsTik -= tikInterval
            tikIsTak = !tikIsTak
        }

        val frequentie = if (tikIsTak) 1400.0 else 1000.0
        val envelope = exp(-tijdSindsTik * 90)
        if (envelope <= 0.001) return 0f
        val toon = sin(2 * PI * frequentie * tijdSindsTik)

        return (toon * envelope).toFloat() * 0.4f
    }
}
