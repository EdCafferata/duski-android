package info.cafferata.duski.model

import androidx.compose.ui.graphics.Color

/** Eén kiesbare screensaver-animatie binnen een categorie. */
data class ScreensaverOptie(
    val id: String,
    val titel: String,
    val emoji: String,
    val type: ScreensaverType,
)

sealed class ScreensaverType {
    data class Animatie(val config: ScreensaverConfig) : ScreensaverType()
    object SchapenTellen : ScreensaverType()
}

/**
 * Bepaalt hoe een screensaver-animatie beweegt en oogt — puur Compose-animatie
 * (tijd-gedreven + emoji), geen video- of mediabestanden van derden nodig
 * (zelfde rechtenvrije aanpak als de audio-generators).
 */
data class ScreensaverConfig(
    val elementEmoji: String,
    val achtergrondBoven: Color,
    val achtergrondOnder: Color,
    val beweging: ScreensaverBeweging,
    val aantalElementen: Int,
    val snelheid: Double,
)

enum class ScreensaverBeweging { GLIJDEN_ZIJWAARTS, VALLEN_OMLAAG, DRIJVEN_OMHOOG, TWINKELEN, DEINEN }

/**
 * Screensaver-opties per geluidscategorie — elke categorie krijgt een eigen,
 * herkenbare set animaties die aansluiten bij de geluiden erin.
 */
val GeluidCategorie.screensaverOpties: List<ScreensaverOptie>
    get() = when (this) {
        GeluidCategorie.KLEURRUIS -> listOf(
            ScreensaverOptie("tv-sneeuw", "Tv-sneeuw", "📺", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "▫️",
                achtergrondBoven = Color(0xFF262626), achtergrondOnder = Color(0xFF0D0D0D),
                beweging = ScreensaverBeweging.TWINKELEN, aantalElementen = 40, snelheid = 30.0
            ))),
            ScreensaverOptie("bloesem", "Bloesem", "🌸", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🌸",
                achtergrondBoven = Color(0xFF4D1A33), achtergrondOnder = Color(0xFF140814),
                beweging = ScreensaverBeweging.DRIJVEN_OMHOOG, aantalElementen = 14, snelheid = 8.0
            ))),
            ScreensaverOptie("aarde", "Aarde", "🟤", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🟤",
                achtergrondBoven = Color(0xFF33210F), achtergrondOnder = Color(0xFF0F0A05),
                beweging = ScreensaverBeweging.DEINEN, aantalElementen = 12, snelheid = 6.0
            ))),
            ScreensaverOptie("mist", "Mist", "🌫️", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🌫️",
                achtergrondBoven = Color(0xFF383838), achtergrondOnder = Color(0xFF0F0F0F),
                beweging = ScreensaverBeweging.GLIJDEN_ZIJWAARTS, aantalElementen = 10, snelheid = 12.0
            ))),
            ScreensaverOptie("bubbels", "Bubbels", "🔵", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🔵",
                achtergrondBoven = Color(0xFF0D1A4D), achtergrondOnder = Color(0xFF05081A),
                beweging = ScreensaverBeweging.DRIJVEN_OMHOOG, aantalElementen = 18, snelheid = 10.0
            ))),
        )
        GeluidCategorie.NATUUR -> listOf(
            ScreensaverOptie("regen", "Regen", "🌧️", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "💧",
                achtergrondBoven = Color(0xFF14202E), achtergrondOnder = Color(0xFF05080F),
                beweging = ScreensaverBeweging.VALLEN_OMLAAG, aantalElementen = 30, snelheid = 1.2
            ))),
            ScreensaverOptie("golven", "Golven", "🌊", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🌊",
                achtergrondBoven = Color(0xFF0A2E47), achtergrondOnder = Color(0xFF030D1A),
                beweging = ScreensaverBeweging.DEINEN, aantalElementen = 8, snelheid = 5.0
            ))),
            ScreensaverOptie("wind", "Wind", "🍃", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🍃",
                achtergrondBoven = Color(0xFF1A3323), achtergrondOnder = Color(0xFF080F0A),
                beweging = ScreensaverBeweging.GLIJDEN_ZIJWAARTS, aantalElementen = 14, snelheid = 26.0
            ))),
            ScreensaverOptie("kampvuur", "Kampvuur", "🔥", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "✨",
                achtergrondBoven = Color(0xFF331405), achtergrondOnder = Color(0xFF0D0503),
                beweging = ScreensaverBeweging.DRIJVEN_OMHOOG, aantalElementen = 16, snelheid = 16.0
            ))),
            ScreensaverOptie("beek", "Beek", "🏞️", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "💧",
                achtergrondBoven = Color(0xFF0F2929), achtergrondOnder = Color(0xFF050D0F),
                beweging = ScreensaverBeweging.GLIJDEN_ZIJWAARTS, aantalElementen = 12, snelheid = 34.0
            ))),
        )
        GeluidCategorie.LICHAAM -> listOf(
            ScreensaverOptie("hartslag", "Hartslag", "❤️", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "❤️",
                achtergrondBoven = Color(0xFF330810), achtergrondOnder = Color(0xFF0D0205),
                beweging = ScreensaverBeweging.TWINKELEN, aantalElementen = 1, snelheid = 20.0
            ))),
            ScreensaverOptie("geborgenheid", "Geborgenheid", "🤰", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🌕",
                achtergrondBoven = Color(0xFF2E1A0D), achtergrondOnder = Color(0xFF0F0805),
                beweging = ScreensaverBeweging.DEINEN, aantalElementen = 1, snelheid = 4.0
            ))),
            ScreensaverOptie("ademhaling", "Ademhaling", "🫁", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "⚪️",
                achtergrondBoven = Color(0xFF0F1F29), achtergrondOnder = Color(0xFF050A0F),
                beweging = ScreensaverBeweging.TWINKELEN, aantalElementen = 1, snelheid = 6.0
            ))),
            ScreensaverOptie("sussen", "Sussen", "🤫", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "💤",
                achtergrondBoven = Color(0xFF1A1A33), achtergrondOnder = Color(0xFF080814),
                beweging = ScreensaverBeweging.DEINEN, aantalElementen = 6, snelheid = 5.0
            ))),
            ScreensaverOptie("luchtstroom", "Luchtstroom", "💨", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "💨",
                achtergrondBoven = Color(0xFF2E2E2E), achtergrondOnder = Color(0xFF0D0D0D),
                beweging = ScreensaverBeweging.GLIJDEN_ZIJWAARTS, aantalElementen = 10, snelheid = 40.0
            ))),
        )
        GeluidCategorie.OVERIGE -> listOf(
            ScreensaverOptie("schaapjes", "Schaapjes tellen", "🐑", ScreensaverType.SchapenTellen),
            ScreensaverOptie("sterrenhemel", "Sterrenhemel", "✨", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "✨",
                achtergrondBoven = Color(0xFF0A0A24), achtergrondOnder = Color(0xFF03030A),
                beweging = ScreensaverBeweging.TWINKELEN, aantalElementen = 40, snelheid = 20.0
            ))),
            ScreensaverOptie("kaarsvlam", "Kaarsvlam", "🕯️", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🕯️",
                achtergrondBoven = Color(0xFF241405), achtergrondOnder = Color(0xFF080503),
                beweging = ScreensaverBeweging.DEINEN, aantalElementen = 1, snelheid = 3.0
            ))),
            ScreensaverOptie("treinraam", "Treinraam", "🚂", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "🌲",
                achtergrondBoven = Color(0xFF0F1A29), achtergrondOnder = Color(0xFF05080D),
                beweging = ScreensaverBeweging.GLIJDEN_ZIJWAARTS, aantalElementen = 10, snelheid = 60.0
            ))),
            ScreensaverOptie("wolkendek", "Wolkendek", "✈️", ScreensaverType.Animatie(ScreensaverConfig(
                elementEmoji = "☁️",
                achtergrondBoven = Color(0xFF4D73A6), achtergrondOnder = Color(0xFF14243D),
                beweging = ScreensaverBeweging.GLIJDEN_ZIJWAARTS, aantalElementen = 8, snelheid = 14.0
            ))),
        )
    }
