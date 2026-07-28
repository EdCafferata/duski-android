package info.cafferata.duski.audio

/**
 * Geïnspireerd op de gebroken-akkoord-figuur uit Bachs Prelude in C majeur
 * (BWV 846, Wohltemperierte Klavier I) — Bach overleed in 1750, dus deze
 * compositie is allang rechtenvrij.
 */
class BachPreludeGenerator : MelodieGenerator(
    noten = run {
        val c4 = 261.63; val e4 = 329.63; val g4 = 392.00; val c5 = 523.25; val e5 = 659.25
        listOf(c4, e4, g4, c5, e5, g4, c5, e5).map { MelodieNoot(it, 0.5) }
    },
    tempoBpm = 66.0,
)
