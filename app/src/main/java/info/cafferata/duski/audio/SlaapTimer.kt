package info.cafferata.duski.audio

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Sleeptimer: telt af en dooft de actieve geluidslaag in de laatste 30 seconden
 * geleidelijk uit (zelfde patroon als Noisli/Sleep Cycle), in plaats van abrupt
 * te stoppen.
 */
class SlaapTimer(private val scope: CoroutineScope) {
    private val _resterendeSeconden = mutableStateOf<Int?>(null)
    val resterendeSeconden: State<Int?> = _resterendeSeconden

    private var job: Job? = null
    private var volumeBijStartUitfaden: Float = 0f
    private val uitfadeDuur = 30

    val isActief: Boolean get() = _resterendeSeconden.value != null

    fun start(minuten: Int, mixer: GeluidsMixer, optieId: String) {
        stop()
        _resterendeSeconden.value = minuten * 60
        job = scope.launch {
            while (true) {
                delay(1000)
                tik(mixer, optieId)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        _resterendeSeconden.value = null
    }

    private fun tik(mixer: GeluidsMixer, optieId: String) {
        val resterend = _resterendeSeconden.value ?: return
        val nieuw = resterend - 1
        _resterendeSeconden.value = nieuw

        if (nieuw <= 0) {
            mixer.stopAlles()
            stop()
            return
        }

        if (nieuw == uitfadeDuur) {
            volumeBijStartUitfaden = mixer.volumes[optieId] ?: 0f
        }

        if (nieuw < uitfadeDuur) {
            val factor = nieuw.toFloat() / uitfadeDuur.toFloat()
            mixer.volumes[optieId] = volumeBijStartUitfaden * factor
        }
    }
}
