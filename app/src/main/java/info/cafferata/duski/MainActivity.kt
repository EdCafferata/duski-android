package info.cafferata.duski

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import info.cafferata.duski.audio.GeluidsMixer
import info.cafferata.duski.audio.SlaapTimer
import info.cafferata.duski.billing.AbonnementManager
import info.cafferata.duski.model.GebruikersVoorkeuren
import info.cafferata.duski.model.LeeftijdsGroep
import info.cafferata.duski.ui.MixerScreen
import info.cafferata.duski.ui.OnboardingScreen
import info.cafferata.duski.ui.PremiumScreen
import info.cafferata.duski.ui.ScreensaverPickerScreen
import info.cafferata.duski.ui.theme.DuskiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GebruikersVoorkeuren.init(applicationContext)

        setContent {
            DuskiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DuskiApp()
                }
            }
        }
    }
}

/**
 * Root-scherm: toont onboarding (leeftijdsgroep-keuze) bij eerste gebruik,
 * daarna het mixer-scherm.
 */
@Composable
private fun DuskiApp() {
    var leeftijdsGroep by remember { mutableStateOf(GebruikersVoorkeuren.leeftijdsGroep) }

    var toontPremium by remember { mutableStateOf(false) }
    var toontScreensaver by remember { mutableStateOf(false) }

    // Systeem-terugknop: één niveau terug binnen de app i.p.v. de Activity
    // (en dus de hele app) te sluiten. Het mixer-scherm is de root — daar
    // blijft de handler uit zodat terug de app nog gewoon sluit. Vanuit
    // "wijzig leeftijdsgroep" gaat terug naar de eerder gekozen groep.
    val opgeslagenGroep = GebruikersVoorkeuren.leeftijdsGroep
    BackHandler(enabled = toontScreensaver || toontPremium || (leeftijdsGroep == null && opgeslagenGroep != null)) {
        when {
            leeftijdsGroep == null -> leeftijdsGroep = opgeslagenGroep
            toontScreensaver -> toontScreensaver = false
            toontPremium -> toontPremium = false
        }
    }

    val groep = leeftijdsGroep
    if (groep == null) {
        OnboardingScreen(onGekozen = { gekozen ->
            GebruikersVoorkeuren.leeftijdsGroep = gekozen
            leeftijdsGroep = gekozen
        })
        return
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val mixer = remember { GeluidsMixer() }
    val slaapTimer = remember { SlaapTimer(scope) }
    val abonnement = remember { AbonnementManager(context) }

    when {
        toontScreensaver -> ScreensaverPickerScreen(onSluiten = { toontScreensaver = false })
        toontPremium -> PremiumScreen(abonnement = abonnement, onSluiten = { toontPremium = false })
        else -> MixerScreen(
            leeftijdsGroep = groep,
            mixer = mixer,
            slaapTimer = slaapTimer,
            abonnement = abonnement,
            onWijzigGroep = { leeftijdsGroep = null },
            onPremium = { toontPremium = true },
            onScreensaver = { toontScreensaver = true },
        )
    }
}
