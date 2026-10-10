package info.cafferata.duski.ui

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.cafferata.duski.R
import info.cafferata.duski.billing.AbonnementManager

private const val VOORWAARDEN_URL = "https://cafferata.info/duski/voorwaarden.html"
private const val PRIVACY_URL = "https://cafferata.info/duski/privacy.html"

/**
 * Paywall-scherm: jaar- en maandabonnement, met de prijs die Play Billing voor
 * het huidige land teruggeeft. Sluit vanzelf zodra Premium actief is.
 */
@Composable
fun PremiumScreen(abonnement: AbonnementManager, onSluiten: () -> Unit) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val producten by abonnement.producten
    val laadFout by abonnement.laadFout
    val heeftPremium by abonnement.heeftPremium

    LaunchedEffect(Unit) { abonnement.laadProducten() }
    LaunchedEffect(heeftPremium) { if (heeftPremium) onSluiten() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("🌙", fontSize = 40.sp)
        Text(
            stringResource(R.string.paywall_titel),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            stringResource(R.string.paywall_sub),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("✓  " + stringResource(R.string.voordeel_alle), fontSize = 15.sp)
            Text("✓  " + stringResource(R.string.voordeel_geen_account), fontSize = 15.sp)
        }

        laadFout?.let {
            Text(
                stringResource(R.string.niet_beschikbaar),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }

        producten.forEach { aanbieding ->
            PremiumProductKaart(aanbieding) {
                (context as? Activity)?.let { abonnement.koop(it, aanbieding) }
            }
        }

        if (producten.isEmpty() && laadFout == null) {
            CircularProgressIndicator()
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                stringResource(R.string.gebruiksvoorwaarden),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { uriHandler.openUri(VOORWAARDEN_URL) },
            )
            Text(
                stringResource(R.string.privacybeleid),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { uriHandler.openUri(PRIVACY_URL) },
            )
        }

        TextButton(onClick = onSluiten) {
            Text(stringResource(R.string.sluiten))
        }
        Spacer(Modifier.height(8.dp))
    }
}
