package info.cafferata.duski.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import info.cafferata.duski.model.GeluidOptie
import kotlin.concurrent.thread
import kotlin.math.min

/**
 * Speelt de gekozen geluidslaag af: een aparte afspeel-thread trekt de
 * generator sample-voor-sample leeg en schrijft naar een streaming AudioTrack.
 * 100% on-device gegenereerd, geen audiobestanden nodig.
 *
 * Net als in de iOS-versie is slechts één geluid tegelijk actief: een nieuwe
 * keuze stopt eerst de vorige, zodat altijd de laatste keuze overblijft.
 */
class GeluidsMixer {
    private val sampleRate = 44100
    private var audioTrack: AudioTrack? = null
    private var afspeelThread: Thread? = null
    @Volatile private var actieveGenerator: GeluidGenerator? = null
    @Volatile private var moetStoppen = false

    private val _actieveOptieId = mutableStateOf<String?>(null)
    val actieveOptieId: State<String?> = _actieveOptieId

    val volumes = mutableStateMapOf<String, Float>()

    private var maximaalVolume = 1.0f

    fun stelMaximaalVolumeIn(waarde: Float) {
        maximaalVolume = waarde
        for ((id, volume) in volumes) {
            volumes[id] = min(volume, waarde)
        }
    }

    fun zetVolume(optie: GeluidOptie, waarde: Float) {
        volumes[optie.id] = min(waarde, maximaalVolume)
    }

    fun isActief(optie: GeluidOptie): Boolean = _actieveOptieId.value == optie.id

    /** Slechts één geluid tegelijk actief: een nieuwe keuze stopt eerst de vorige. */
    fun schakel(optie: GeluidOptie) {
        if (isActief(optie)) {
            stopAlles()
        } else {
            stopAlles()
            startLaag(optie)
        }
    }

    private fun startLaag(optie: GeluidOptie) {
        val generator = GeluidGeneratorFabriek.maak(optie.type)
        if (volumes[optie.id] == null) {
            volumes[optie.id] = min(0.7f, maximaalVolume)
        }

        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_FLOAT,
        ).coerceAtLeast(4096)

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack = track
        actieveGenerator = generator
        moetStoppen = false
        _actieveOptieId.value = optie.id

        track.play()
        afspeelThread = thread(name = "duski-audio") {
            val buffer = FloatArray(1024)
            while (!moetStoppen) {
                val volume = volumes[optie.id] ?: 0f
                for (i in buffer.indices) {
                    buffer[i] = generator.volgendeSample(sampleRate.toDouble()) * volume
                }
                track.write(buffer, 0, buffer.size, AudioTrack.WRITE_BLOCKING)
            }
        }
    }

    fun stopAlles() {
        moetStoppen = true
        afspeelThread?.join(200)
        afspeelThread = null
        audioTrack?.let {
            it.stop()
            it.release()
        }
        audioTrack = null
        actieveGenerator = null
        _actieveOptieId.value = null
    }
}
