package online.nyam.hikka.ui.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import online.nyam.hikka.ui.theme.HikkaTheme
import online.nyam.hikka.util.toDp
import kotlin.time.Duration.Companion.seconds

@Composable
fun CollapsibleText(
    content: String,
    style: TextStyle,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    expandedLines: Int = Int.MAX_VALUE,
    collapsedLines: Int = 5,
    onOverflow: (() -> Unit)? = null,
    animationDuration: Int = 400
) {
    val maxLines =
        when {
            expanded -> expandedLines
            else -> collapsedLines
        }

    val measurer = rememberTextMeasurer()

    BoxWithConstraints(
        modifier = modifier
    ) {
        val measurement =
            remember(content, style, maxLines, constraints) {
                measurer
                    .measure(
                        content,
                        style,
                        maxLines = maxLines,
                        constraints = constraints
                    )
            }

        if (measurement.didOverflowHeight) {
            onOverflow?.invoke()
        }

        val measuredHeight = measurement.size.height

        val displayHeight by animateIntAsState(
            targetValue = measuredHeight,
            label = "Height Animation",
            animationSpec = tween(animationDuration)
        )

        Text(
            content,
            style = style,
            color = color,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(displayHeight.toDp()),
            overflow = if (displayHeight != measuredHeight) TextOverflow.Clip else TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCollapsibleText() {
    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = expanded) {
        delay(1.seconds.inWholeMilliseconds)
        expanded = !expanded
    }

    Box(Modifier.fillMaxSize()) {
        Row(Modifier.align(Alignment.TopCenter)) {
            HikkaTheme {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    shadowElevation = 8.dp,
                    tonalElevation = 4.dp,
                    modifier =
                        Modifier
                            .padding(10.dp)
                            .weight(1f)
                ) {
                    CollapsibleText(
                        modifier = Modifier.padding(8.dp),
                        content = (1..10).joinToString("\n") { "Line $it" },
                        style = MaterialTheme.typography.bodyMedium,
                        expanded = expanded
                    )
                }
            }

            HikkaTheme(darkTheme = true) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    shadowElevation = 8.dp,
                    tonalElevation = 4.dp,
                    modifier =
                        Modifier
                            .padding(10.dp)
                            .weight(1f)
                ) {
                    CollapsibleText(
                        modifier = Modifier.padding(8.dp),
                        content = (1..10).joinToString("\n") { "Line $it" },
                        style = MaterialTheme.typography.bodyMedium,
                        expanded = expanded
                    )
                }
            }
        }
        Row(Modifier.align(Alignment.BottomCenter)) {
            HikkaTheme(dynamicColor = false) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    shadowElevation = 8.dp,
                    tonalElevation = 4.dp,
                    modifier =
                        Modifier
                            .padding(10.dp)
                            .weight(1f)
                ) {
                    CollapsibleText(
                        modifier = Modifier.padding(8.dp),
                        content = (1..10).joinToString("\n") { "Line $it" },
                        style = MaterialTheme.typography.bodyMedium,
                        expanded = expanded
                    )
                }
            }

            HikkaTheme(dynamicColor = false, darkTheme = true) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    shadowElevation = 8.dp,
                    tonalElevation = 4.dp,
                    modifier =
                        Modifier
                            .padding(10.dp)
                            .weight(1f)
                ) {
                    CollapsibleText(
                        modifier = Modifier.padding(8.dp),
                        content = (1..10).joinToString("\n") { "Line $it" },
                        style = MaterialTheme.typography.bodyMedium,
                        expanded = expanded
                    )
                }
            }
        }
    }
}
