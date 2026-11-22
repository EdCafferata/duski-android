package info.cafferata.duski.audio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat
import info.cafferata.duski.MainActivity
import info.cafferata.duski.R

/**
 * Foreground service (type mediaPlayback) die alleen draait zolang er geluid speelt.
 * Zonder deze service mag Android het proces stoppen zodra het scherm vergrendelt
 * of de app op de achtergrond staat — en dan stopt het geluid midden in de nacht (#6).
 * De audio zelf blijft in [GeluidsMixer]; de service houdt het proces op voorgrond-prioriteit
 * en toont de verplichte melding "Duski speelt".
 */
class AfspeelService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTIE_STOP) {
            bijStoppen?.invoke()
            stopSelf()
            return START_NOT_STICKY
        }
        val melding = maakMelding()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(MELDING_ID, melding, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(MELDING_ID, melding)
        }
        return START_NOT_STICKY
    }

    /** App weggeveegd uit recente apps → geluid stoppen, geen spookgeluid zonder scherm. */
    override fun onTaskRemoved(rootIntent: Intent?) {
        bijStoppen?.invoke()
        stopSelf()
    }

    private fun maakMelding(): Notification {
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(KANAAL_ID, getString(R.string.melding_kanaal), NotificationManager.IMPORTANCE_LOW)
                    .apply { setShowBadge(false) }
            )
        }
        val openApp = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val stop = PendingIntent.getService(
            this, 1, Intent(this, AfspeelService::class.java).setAction(ACTIE_STOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(this, KANAAL_ID)
                      else @Suppress("DEPRECATION") Notification.Builder(this)
        return builder
            .setSmallIcon(R.drawable.ic_melding)
            .setContentTitle(getString(R.string.melding_titel))
            .setContentText(getString(R.string.melding_tekst))
            .setContentIntent(openApp)
            .setOngoing(true)
            .addAction(Notification.Action.Builder(null, getString(R.string.stop), stop).build())
            .build()
    }

    companion object {
        private const val KANAAL_ID = "afspelen"
        private const val MELDING_ID = 1
        private const val ACTIE_STOP = "info.cafferata.duski.STOP"

        /** Wordt door de app gezet: stopt mixer + timer (via meldingsknop of wegvegen). */
        @Volatile var bijStoppen: (() -> Unit)? = null

        fun start(context: Context) {
            ContextCompat.startForegroundService(context, Intent(context, AfspeelService::class.java))
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, AfspeelService::class.java))
        }
    }
}
