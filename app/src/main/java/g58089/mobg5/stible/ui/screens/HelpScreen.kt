package g58089.mobg5.stible.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import g58089.mobg5.stible.R

/**
 * Shows the help messages in a [HorizontalPager] with page indicator.
 */
@Composable
fun HelpScreen(modifier: Modifier = Modifier) {
    val messages = stringArrayResource(id = R.array.help_messages)
    val pagerState = rememberPagerState(pageCount = {
        messages.size
    })

    Box(modifier = modifier) {
        // the pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Text(
                text = messages[page],
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        // copied straight from Android Developers
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    // except for the colors which i changed. I need to understand how Material works
                    if (pagerState.currentPage == iteration)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape) // circle :D
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HelpScreenPreview() {
    HelpScreen(Modifier.fillMaxSize())
}