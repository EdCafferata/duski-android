package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin

/** Eén losse muzieknoot binnen een melodie: frequentie in Hz (0 = rust/stilte)
 * en duur in tellen (kwartnoot = 1.0). */
data class MelodieNoot(val frequentie: Double, val tellen: Double)

/**
 * Speelt een vaste reeks noten af als zachte, ronde sinustonen (grondtoon +
 * zwakke boventoon) — eigen additieve synthese, geen samples of opnames van
 * derden. Basis voor arrangementen geïnspireerd op allang auteursrechtvrije
 * klassieke stukken (componist meer dan 70 jaar overleden), zoals Bach,
 * Pachelbel, Satie en Debussy.
 */
open class MelodieGenerator(
    private val noten: List<MelodieNoot>,
    private val tempoBpm: Double,
) : GeluidGenerator {
    private var notenIndex = 0
    private var tijdInNoot = 0.0
    private var fase = 0.0

    override fun volgendeSample(sampleRate: Double): Float {
        if (noten.isEmpty()) return 0f
        val dt = 1.0 / sampleRate
        val secondenPerTel = 60.0 / tempoBpm

        var noot = noten[notenIndex]
        var duur = noot.tellen * secondenPerTel

        tijdInNoot += dt
        if (tijdInNoot >= duur) {
            tijdInNoot -= duur
            notenIndex = (notenIndex + 1) % noten.size
            noot = noten[notenIndex]
            duur = noot.tellen * secondenPerTel
        }

        val attack = min(tijdInNoot / 0.05, 1.0)
        val release = min((duur - tijdInNoot) / maxOf(duur * 0.6, 0.001), 1.0)
        val envelope = min(attack, release)

        if (noot.frequentie <= 0) return 0f
        fase += noot.frequentie * dt
        if (fase > 1) fase -= 1

        val grondtoon = sin(2 * PI * fase)
        val boventoon = sin(2 * PI * fase * 2) * 0.25
        return ((grondtoon + boventoon) * envelope * 0.18).toFloat()
    }
}
