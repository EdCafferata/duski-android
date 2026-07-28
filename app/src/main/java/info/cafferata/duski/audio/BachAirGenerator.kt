package info.cafferata.duski.audio

/**
 * Geïnspireerd op de rustige melodielijn van Bachs "Air" (Orchestral Suite
 * nr. 3, bekend als "Air on the G String") — allang rechtenvrij.
 */
class BachAirGenerator : MelodieGenerator(
    noten = run {
        val c4 = 261.63; val d4 = 293.66; val e4 = 329.63; val f4 = 349.23
        val b3 = 246.94
        listOf(
            MelodieNoot(c4, 2.0),
            MelodieNoot(b3, 1.0),
            MelodieNoot(c4, 1.0),
            MelodieNoot(d4, 2.0),
            MelodieNoot(e4, 2.0),
            MelodieNoot(f4, 1.0),
            MelodieNoot(e4, 1.0),
            MelodieNoot(d4, 2.0),
            MelodieNoot(c4, 4.0),
        )
    },
    tempoBpm = 50.0,
)
