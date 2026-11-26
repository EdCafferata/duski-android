package info.cafferata.duski.audio

/** Speelt meerdere generators tegelijk, elk op een vast volume (voor de "Voor jou"-mix). */
class MixGenerator(private val lagen: List<Pair<GeluidGenerator, Float>>) : GeluidGenerator {
    override fun volgendeSample(sampleRate: Double): Float {
        var som = 0f
        for ((generator, volume) in lagen) som += generator.volgendeSample(sampleRate) * volume
        return som
    }
}
