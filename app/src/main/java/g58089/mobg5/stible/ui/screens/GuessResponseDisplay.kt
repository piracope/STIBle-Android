package g58089.mobg5.stible.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.East
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.North
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material.icons.rounded.NorthWest
import androidx.compose.material.icons.rounded.South
import androidx.compose.material.icons.rounded.SouthEast
import androidx.compose.material.icons.rounded.SouthWest
import androidx.compose.material.icons.rounded.West
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import g58089.mobg5.stible.R
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.ui.theme.STIBleGreen
import g58089.mobg5.stible.ui.theme.STIBleYellow
import g58089.mobg5.stible.ui.theme.light_onSTIBleGreen
import java.util.Locale

/**
 * Displays a [GuessResponse] to the player.
 *
 * Format :
 * Guessed stop name | Percentage indicators (squares) | Distance | Direction
 *
 * @param guessResponse the [GuessResponse] to display, or `null` for a blank row
 */
@Composable
fun GuessRow(guessResponse: GuessResponse?, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(vertical = dimensionResource(R.dimen.guess_row_padding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.guess_row_padding))
    ) {

        GuessRowTextCell(
            guessResponse?.stopName, Modifier.fillMaxWidth(0.4f)
        )
        GuessRowPercentageSquares(
            guessResponse?.proximityPecentage,
            Modifier.size(dimensionResource(R.dimen.guess_row_height))
        )

        val distanceText =
            guessResponse?.distance?.let { String.format(Locale.ENGLISH, "%.1fkm", it) }
        GuessRowTextCell(
            text = distanceText, modifier = Modifier.weight(1f)
        )

        val colorDirection = getDirectionBackgroundColor(guessResponse)
        val vector = guessResponse?.directionEmoji?.let { emojiToIcon(it) }
        GuessRowIcon(
            icon = vector,
            bgColor = colorDirection,
            fgColor = if (colorDirection == STIBleGreen) light_onSTIBleGreen else MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(dimensionResource(R.dimen.guess_row_height))
        )
    }
}

/**
 * Displays a text in a cell of a [GuessRow].
 *
 * Should be used in a [GuessRow]
 */
@Composable
private fun GuessRowTextCell(text: String?, modifier: Modifier = Modifier) {
    val color =
        if (text == null) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.secondary
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
            .background(color)
            .height(dimensionResource(R.dimen.guess_row_height))

    ) {
        text?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
            )
        }
    }
}

/**
 * Displays the proximity percentage as a list of 5 squares.
 *
 * Green square : 20% of proximity
 * Yellow square : 10% of proximity
 * Black square : 0% of proximity
 */
@Composable
private fun GuessRowPercentageSquares(proximityPercentage: Double?, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.guess_row_padding))) {
        val nbOfGreen: Int = proximityPercentage?.times(100)?.div(20)?.toInt() ?: 0
        val nbOfYellow: Int = proximityPercentage?.times(100)?.rem(20)?.div(10)?.toInt() ?: 0
        repeat(5) { sqNb ->
            val color: Color = if (sqNb < nbOfGreen) STIBleGreen
            else if (sqNb < nbOfGreen + nbOfYellow) STIBleYellow
            // if there are 2 green 1 yellow, yellow starts at 3
            else MaterialTheme.colorScheme.surfaceVariant
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                    .background(color)
            )
        }
    }
}

@Composable
private fun GuessRowIcon(
    icon: ImageVector?, bgColor: Color, fgColor: Color, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
            .background(bgColor)
    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = it.name, tint = fgColor)
        }
    }
}

/**
 * Small little helper for the direction background color.
 *
 * - No [GuessResponse] was passed : usual gray
 * - Non-winning guess : blue
 * - Winning guess : green
 *
 * @param guess the [GuessResponse] of this guess row, null if the row is empty
 */
@Composable
private fun getDirectionBackgroundColor(guess: GuessResponse?): Color {
    if (guess == null) {
        return MaterialTheme.colorScheme.surfaceVariant
    }

    if (guess.directionEmoji == "✅") { // doesn't display on my machine but it's a checkmark
        return STIBleGreen
    }

    return MaterialTheme.colorScheme.primaryContainer
}

/**
 * Converts a direction emoji to the corresponding Material Design icon.
 */
private fun emojiToIcon(emoji: String): ImageVector {
    return when (emoji) {
        "➡️" -> Icons.Rounded.East
        "↗️" -> Icons.Rounded.NorthEast
        "⬆️" -> Icons.Rounded.North
        "↖️" -> Icons.Rounded.NorthWest
        "⬅️" -> Icons.Rounded.West
        "↙️" -> Icons.Rounded.SouthWest
        "⬇️" -> Icons.Rounded.South
        "↘️" -> Icons.Rounded.SouthEast
        "✅" -> Icons.Rounded.Check
        else -> Icons.Rounded.Error // should never happen
    }
}
