package info.cafferata.duski.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.cafferata.duski.audio.GeluidsMixer
import info.cafferata.duski.audio.SlaapTimer
import info.cafferata.duski.billing.AbonnementManager
import info.cafferata.duski.model.GeluidCategorie
import info.cafferata.duski.model.GeluidOptie
import info.cafferata.duski.model.LeeftijdsGroep

/**
 * Hoofdscherm: kies losse geluidslagen en mix ze — zelfde interactiepatroon
 * als de meeste succesvolle slaap-apps (Noisli, BetterSleep).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MixerScreen(
    leeftijdsGroep: LeeftijdsGroep,
    mixer: GeluidsMixer,
    slaapTimer: SlaapTimer,
    abonnement: AbonnementManager,
    onWijzigGroep: () -> Unit,
    onPremium: () -> Unit,
    onScreensaver: () -> Unit,
) {
    var toontVeiligheidsBanner by remember { mutableStateOf(true) }
    val actieveOptieId = mixer.actieveOptieId.value
    val heeftPremium by abonnement.heeftPremium

    LaunchedEffect(leeftijdsGroep) {
        mixer.stelMaximaalVolumeIn(leeftijdsGroep.maximaalVolume)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${leeftijdsGroep.emoji} ${leeftijdsGroep.titel}", fontSize = 18.sp) },
                navigationIcon = {
                    if (!heeftPremium) {
                        Text(
                            "Premium",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable(onClick = onPremium),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                actions = {
                    Text(
                        "Wijzig",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                mixer.stopAlles()
                                onWijzigGroep()
                            },
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
            item {
                Spacer(Modifier.height(12.dp))
                if (leeftijdsGroep == LeeftijdsGroep.BABY) {
                    AnimatedVisibility(visible = toontVeiligheidsBanner) {
                        VeiligheidsBanner(onVerlopen = { toontVeiligheidsBanner = false })
                    }
                }
            }

            items(GeluidCategorie.entries) { categorie ->
                Column {
                    Text(categorie.titel, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        categorie.opties.forEach { optie ->
                            GeluidTegel(optie = optie, mixer = mixer)
                        }
                    }
                }
            }

            item {
                SlaapTimerKaart(
                    timer = slaapTimer,
                    mixer = mixer,
                    actieveOptieId = actieveOptieId,
                    toontScreensaverKnop = actieveOptieId != null,
                    onScreensaver = onScreensaver,
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

/** Eén tegel voor een geluidslaag: aan/uit + volumeslider zodra actief. */
@Composable
private fun GeluidTegel(optie: GeluidOptie, mixer: GeluidsMixer) {
    val actief = mixer.isActief(optie)
    val breedte = 104.dp

    Column(
        modifier = Modifier
            .width(breedte)
            .background(
                if (actief) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                else MaterialTheme.colorScheme.surface,
                RoundedCornerShape(14.dp),
            )
            .clickable { mixer.schakel(optie) }
            .padding(horizontal = 4.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(optie.emoji, fontSize = 24.sp)
        Spacer(Modifier.height(4.dp))
        Text(optie.titel, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1)

        if (actief) {
            val volume = mixer.volumes[optie.id] ?: 0.7f
            Slider(
                value = volume,
                onValueChange = { mixer.zetVolume(optie, it) },
                modifier = Modifier.width(breedte - 14.dp).padding(top = 6.dp),
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary),
            )
        }
    }
}
