package info.cafferata.duski.audio

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.tanh
import kotlin.random.Random

/**
 * Onweer: zachte regen met af en toe een verre donderslag — traag aanzwellend en lang
 * uitrommelend laag gerommel (sterk laagdoorgelaten bruine ruis). Geen harde klappen.
 * Zelfde algoritme als de iOS-versie.
 */
class OnweerGenerator : GeluidGenerator {
    private val regen = RegenGenerator()
    private var bruin = 0f
    private var laag1 = 0f
    private var laag2 = 0f
    private var tijdTotDonder = 6.0
    private var tijdInDonder = -1.0
    private var donderDuur = 6.0
    private var donderSterkte = 1f
    private var flakkerFase = 0f

    override fun volgendeSample(sampleRate: Double): Float {
        val dt = 1.0 / sampleRate
        val wit = Random.nextFloat() * 2f - 1f

        if (tijdInDonder < 0) {
            tijdTotDonder -= dt
            if (tijdTotDonder <= 0) {
                tijdInDonder = 0.0
                donderDuur = 5.0 + Random.nextDouble() * 4.0
                donderSterkte = 0.6f + Random.nextFloat() * 0.4f
            }
        }

        var donder = 0f
        if (tijdInDonder >= 0) {
            tijdInDonder += dt
            val aanzet = min(tijdInDonder / 0.6, 1.0)
            val uitsterf = exp(-max(tijdInDonder - 0.6, 0.0) / (donderDuur / 3))
            flakkerFase += dt.toFloat() * 1.7f
            if (flakkerFase >= 1f) flakkerFase -= 1f // fase binnen 0..<1 houden
            val rommel = 0.7f + 0.3f * sin(flakkerFase * 2f * PI.toFloat())

            bruin = (bruin + 0.02f * wit) / 1.02f
            laag1 += 0.02f * (bruin - laag1)
            laag2 += 0.02f * (laag1 - laag2)
            donder = laag2 * 6f * (aanzet * uitsterf).toFloat() * rommel * donderSterkte

            if (tijdInDonder >= donderDuur) {
                tijdInDonder = -1.0
                tijdTotDonder = 15.0 + Random.nextDouble() * 25.0
            }
        }
        // Zachte begrenzing: ook als regen en donder samen pieken, nooit hard clippen.
        return tanh(regen.volgendeSample(sampleRate) * 0.8f + donder)
    }
}
