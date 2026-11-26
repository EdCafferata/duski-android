package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.sin

/**
 * Speeldoos met slaapliedjes: Brahms' Wiegenlied en "Altijd is Kortjakje ziek" /
 * "Twinkle Twinkle" — allang rechtenvrij. Elke noot is een kort tokkeltje dat vanzelf
 * uitklinkt (grondtoon + snel wegstervende boventonen). Zelfde melodie en klank als iOS.
 */
class SpeeldoosGenerator : GeluidGenerator {
    private val maxStemmen = 6
    private val freq = DoubleArray(maxStemmen)
    private val fase = DoubleArray(maxStemmen)
    private val leeftijd = DoubleArray(maxStemmen) { 99.0 }
    private var volgendeStem = 0

    private val noten: List<MelodieNoot>
    private val secondenPerTel = 60.0 / 66.0
    private var notenIndex = 0
    private var tijdTotVolgende = 0.0

    init {
        val c5 = 523.25; val d5 = 587.33; val e5 = 659.25; val f5 = 698.46; val g5 = 783.99
        val a5 = 880.00; val b5 = 987.77; val c6 = 1046.50; val g4 = 392.00; val c4 = 261.63
        fun n(f: Double, t: Double) = MelodieNoot(f, t)
        val wiegenlied = listOf(
            n(e5, .5), n(e5, .5), n(g5, 2.0), n(e5, .5), n(e5, .5), n(g5, 2.0),
            n(e5, .5), n(g5, .5), n(c6, 1.0), n(b5, 1.5), n(a5, .5), n(a5, 1.0), n(g5, 2.0),
            n(d5, .5), n(e5, .5), n(f5, 2.0), n(d5, .5), n(e5, .5), n(f5, 2.0),
            n(d5, .5), n(f5, .5), n(b5, .5), n(a5, .5), n(g5, 1.0), n(b5, 1.0), n(c6, 3.0),
            n(c5, .5), n(c5, .5), n(c6, 2.0), n(a5, .5), n(f5, .5), n(g5, 2.0),
            n(e5, .5), n(c5, .5), n(f5, 1.0), n(g5, 1.0), n(a5, 1.0), n(g5, 2.0),
            n(c5, .5), n(c5, .5), n(c6, 2.0), n(a5, .5), n(f5, .5), n(g5, 2.0),
            n(e5, .5), n(c5, .5), n(f5, 1.0), n(e5, 1.0), n(d5, 1.0), n(c5, 3.0),
            n(0.0, 3.0),
        )
        val zin1 = listOf(n(c5, 1.0), n(c5, 1.0), n(g5, 1.0), n(g5, 1.0), n(a5, 1.0), n(a5, 1.0), n(g5, 2.0),
            n(f5, 1.0), n(f5, 1.0), n(e5, 1.0), n(e5, 1.0), n(d5, 1.0), n(d5, 1.0), n(c5, 2.0))
        val zin2 = listOf(n(g5, 1.0), n(g5, 1.0), n(f5, 1.0), n(f5, 1.0), n(e5, 1.0), n(e5, 1.0), n(d5, 2.0))
        val kortjakje = zin1 + zin2 + zin2 + zin1 + listOf(n(g4, 1.0), n(c4, 2.0), n(0.0, 3.0))
        noten = wiegenlied + kortjakje
    }

    override fun volgendeSample(sampleRate: Double): Float {
        val dt = 1.0 / sampleRate
        tijdTotVolgende -= dt
        if (tijdTotVolgende <= 0) {
            val noot = noten[notenIndex]
            notenIndex = (notenIndex + 1) % noten.size
            tijdTotVolgende += noot.tellen * secondenPerTel
            if (noot.frequentie > 0) {
                freq[volgendeStem] = noot.frequentie; fase[volgendeStem] = 0.0; leeftijd[volgendeStem] = 0.0
                volgendeStem = (volgendeStem + 1) % maxStemmen
            }
        }
        var som = 0.0
        for (i in 0 until maxStemmen) {
            val t = leeftijd[i]
            if (t > 5.0) continue
            leeftijd[i] = t + dt
            fase[i] += freq[i] * dt
            if (fase[i] >= 1.0) fase[i] -= 1.0 // fase binnen 0..<1 houden
            val p = 2 * PI * fase[i]
            val aanslag = min(t / 0.004, 1.0)
            som += aanslag * (sin(p) * exp(-t / 1.1) + sin(2 * p) * 0.30 * exp(-t / 0.35) + sin(3 * p) * 0.10 * exp(-t / 0.15))
        }
        return (som * 0.16).toFloat()
    }
}
