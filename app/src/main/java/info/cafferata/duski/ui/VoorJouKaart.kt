package info.cafferata.duski.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.cafferata.duski.R
import info.cafferata.duski.audio.GeluidsMixer
import info.cafferata.duski.model.LeeftijdsGroep
import info.cafferata.duski.model.VoorJouMix

/** Grote kaart bovenaan het hoofdscherm met de "Voor jou"-mix van de leeftijdsgroep. */
@Composable
fun VoorJouKaart(groep: LeeftijdsGroep, mixer: GeluidsMixer) {
    val mix = VoorJouMix.voor(groep)
    val speelt = mixer.actieveOptieId.value == VoorJouMix.ID
    val groot = groep == LeeftijdsGroep.OUDERE
    val titel = stringResource(mix.titelRes)
    val actieLabel = stringResource(if (speelt) R.string.voorjou_stop else R.string.voorjou_speel, titel)
    val accent = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (speelt) accent.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                RoundedCornerShape(20.dp),
            )
            .clickable { if (speelt) mixer.stopAlles() else mixer.speelVoorJou(mix) }
            .semantics { contentDescription = actieLabel }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(mix.emoji, fontSize = if (groot) 44.sp else 36.sp)
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    stringResource(R.string.voorjou).uppercase(),
                    color = accent, fontWeight = FontWeight.Bold, fontSize = if (groot) 15.sp else 12.sp,
                )
                Text(
                    stringResource(R.string.gratis),
                    fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(accent.copy(alpha = 0.18f), RoundedCornerShape(50))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                )
            }
            Text(titel, fontWeight = FontWeight.Bold, fontSize = if (groot) 22.sp else 17.sp)
            Text(
                stringResource(mix.uitlegRes),
                fontSize = if (groot) 17.sp else 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
            )
        }
        Box(
            modifier = Modifier.size(if (groot) 64.dp else 52.dp).background(accent, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                if (speelt) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(if (groot) 34.dp else 28.dp),
            )
        }
    }
}
