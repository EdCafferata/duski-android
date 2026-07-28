package info.cafferata.duski.model

import android.content.Context
import android.content.SharedPreferences

/** Bewaart de leeftijdsgroep-keuze (onboarding) in SharedPreferences. */
object GebruikersVoorkeuren {
    private const val PREFS_NAME = "duski-voorkeuren"
    private const val LEEFTIJDSGROEP_KEY = "duski-leeftijdsgroep"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var leeftijdsGroep: LeeftijdsGroep?
        get() = prefs.getString(LEEFTIJDSGROEP_KEY, null)?.let { waarde ->
            runCatching { LeeftijdsGroep.valueOf(waarde) }.getOrNull()
        }
        set(value) {
            prefs.edit().putString(LEEFTIJDSGROEP_KEY, value?.name).apply()
        }
}
