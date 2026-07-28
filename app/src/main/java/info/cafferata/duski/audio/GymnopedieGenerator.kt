package info.cafferata.duski.audio

/**
 * Geïnspireerd op het herkenbare, zwevende akkoordenpatroon uit Saties
 * Gymnopédie nr. 1 — Satie overleed in 1925, dus deze compositie is allang
 * rechtenvrij.
 */
class GymnopedieGenerator : MelodieGenerator(
    noten = run {
        val g3 = 196.00; val d4 = 293.66; val a3 = 220.00; val b3 = 246.94; val fis4 = 369.99
        listOf(
            MelodieNoot(g3, 1.0),
            MelodieNoot(d4, 1.0),
            MelodieNoot(b3, 1.0),
            MelodieNoot(a3, 1.0),
            MelodieNoot(d4, 1.0),
            MelodieNoot(fis4, 1.0),
            MelodieNoot(g3, 3.0),
        )
    },
    tempoBpm = 72.0,
)
