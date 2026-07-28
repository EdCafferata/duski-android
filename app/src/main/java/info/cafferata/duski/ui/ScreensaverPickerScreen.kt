package info.cafferata.duski.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.cafferata.duski.model.GeluidCategorie
import info.cafferata.duski.model.ScreensaverOptie
import info.cafferata.duski.model.ScreensaverType
import info.cafferata.duski.model.screensaverOpties

/**
 * Screensaver-kiezer: zelfde schermopbouw als het geluidsmixer-scherm
 * (categorieën met horizontaal scrollende tegels), maar dan gevuld met
 * screensaver-animaties in plaats van geluiden.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreensaverPickerScreen(onSluiten: () -> Unit) {
    var gekozenOptie by remember { mutableStateOf<ScreensaverOptie?>(null) }

    if (gekozenOptie != null) {
        when (val type = gekozenOptie!!.type) {
            is ScreensaverType.SchapenTellen -> SheepScreensaverScreen(onSluiten = { gekozenOptie = null })
            is ScreensaverType.Animatie -> AnimatedScreensaverScreen(config = type.config, onSluiten = { gekozenOptie = null })
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Screensaver") },
                navigationIcon = {
                    Text(
                        "Terug",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable(onClick = onSluiten),
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            item { Spacer(Modifier.height(12.dp)) }
            items(GeluidCategorie.entries) { categorie ->
                Column {
                    Text(categorie.titel, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        categorie.screensaverOpties.forEach { optie ->
                            ScreensaverTegel(optie = optie, onKies = { gekozenOptie = optie })
                        }
                    }
                }
            }
        }
    }
}

/** Eén tegel voor een screensaver-optie — zelfde vormgeving als de geluidstegels. */
@Composable
private fun ScreensaverTegel(optie: ScreensaverOptie, onKies: () -> Unit) {
    val breedte = 104.dp
    Column(
        modifier = Modifier
            .width(breedte)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .clickable(onClick = onKies)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(optie.emoji, fontSize = 24.sp)
        Spacer(Modifier.height(4.dp))
        Text(optie.titel, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}
