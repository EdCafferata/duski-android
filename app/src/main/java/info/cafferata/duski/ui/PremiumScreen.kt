package info.cafferata.duski.ui

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.cafferata.duski.billing.AbonnementManager

/**
 * Eenvoudig paywall-scherm: 30 dagen gratis, daarna het abonnement. Toont de
 * prijs die Play Billing voor het huidige land teruggeeft.
 */
@Composable
fun PremiumScreen(abonnement: AbonnementManager, onSluiten: () -> Unit) {
    val context = LocalContext.current
    val producten by abonnement.producten
    val laadFout by abonnement.laadFout

    LaunchedEffect(Unit) {
        abonnement.laadProducten()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("🌙", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text("30 dagen gratis", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(
            "Daarna het abonnementstarief. Elk moment op te zeggen.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
        Spacer(Modifier.height(24.dp))

        laadFout?.let {
            Text(it, fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
        }

        producten.forEach { product ->
            val prijs = product.subscriptionOfferDetails
                ?.firstOrNull()
                ?.pricingPhases
                ?.pricingPhaseList
                ?.lastOrNull()
                ?.formattedPrice
                ?: ""
            Button(
                onClick = { (context as? Activity)?.let { abonnement.koop(it, product) } },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Start gratis proefperiode — daarna $prijs/maand")
            }
            Spacer(Modifier.height(8.dp))
        }

        if (producten.isEmpty() && laadFout == null) {
            CircularProgressIndicator()
        }

        Spacer(Modifier.height(24.dp))
        TextButton(onClick = onSluiten) {
            Text("Sluiten")
        }
    }
}
