package info.cafferata.duski.audio

/**
 * Geïnspireerd op het zachte openingsmotief van Debussy's Clair de Lune —
 * Debussy overleed in 1918, dus deze compositie is inmiddels ruim
 * rechtenvrij.
 */
class ClairDeLuneGenerator : MelodieGenerator(
    noten = run {
        val des4 = 277.18; val as3 = 207.65; val ges3 = 185.00; val des3 = 138.59; val f3 = 174.61
        listOf(
            MelodieNoot(des4, 2.0),
            MelodieNoot(as3, 1.0),
            MelodieNoot(ges3, 1.0),
            MelodieNoot(f3, 2.0),
            MelodieNoot(des3, 2.0),
        )
    },
    tempoBpm = 48.0,
)
