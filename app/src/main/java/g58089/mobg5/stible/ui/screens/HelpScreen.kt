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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.outer_padding))
    ) {
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            Text(
                text = messages[page],
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // copied straight from https://developer.android.com/jetpack/compose/layouts/pager
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(dimensionResource(id = R.dimen.inner_padding)),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.surfaceTint
                    else MaterialTheme.colorScheme.surfaceVariant
                Box(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.pill_padding))
                        .clip(CircleShape)
                        .background(color)
                        .size(dimensionResource(id = R.dimen.pill_size))
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