package info.cafferata.duski.audio

/**
 * Geïnspireerd op de beroemde dalende baslijn uit Pachelbels Canon in D —
 * Pachelbel overleed in 1706, dus deze compositie is allang rechtenvrij.
 */
class CanonGenerator : MelodieGenerator(
    noten = run {
        val d3 = 146.83; val a2 = 110.00; val b2 = 123.47; val fis2 = 92.50
        val g2 = 98.00; val d2 = 73.42
        listOf(d3, a2, b2, fis2, g2, d2, g2, a2).map { MelodieNoot(it, 2.0) }
    },
    tempoBpm = 60.0,
)
