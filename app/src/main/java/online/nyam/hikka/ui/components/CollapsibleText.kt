package online.nyam.hikka.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import online.nyam.hikka.ui.theme.HikkaTheme
import online.nyam.hikka.util.toDp
import kotlin.time.Duration.Companion.seconds

@Composable
fun animateHeight(
    targetValue: Int,
    animationDuration: Int
): State<Int> =
    animateIntAsState(
        targetValue = targetValue,
        label = "Height Animation",
        animationSpec = tween(animationDuration, easing = LinearOutSlowInEasing)
    )

@Suppress("unused")
class MeasureScope(
    private val measurer: TextMeasurer,
    private val style: TextStyle,
    private val maxLines: Int,
    private val constraints: Constraints
) {
    fun measure(text: String): TextLayoutResult = measurer.measure(text, style = style, maxLines = maxLines, constraints = constraints)

    fun measure(text: AnnotatedString): TextLayoutResult =
        measurer.measure(text, style = style, maxLines = maxLines, constraints = constraints)
}

@Composable
private fun CollapsibleText(
    expanded: Boolean,
    measure: MeasureScope.() -> TextLayoutResult,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    expandedLines: Int = Int.MAX_VALUE,
    collapsedLines: Int = 5,
    animationDuration: Int = 400,
    textComposable: @Composable (Modifier, TextStyle, TextOverflow) -> Unit
) {
    val maxLines =
        when {
            expanded -> expandedLines
            else -> collapsedLines
        }

    BoxWithConstraints(modifier) {
        val result =
            MeasureScope(rememberTextMeasurer(), style, maxLines, constraints).run { measure() }

        val displayHeight by animateHeight(
            targetValue = result.size.height,
            animationDuration = animationDuration
        )

        textComposable(
            Modifier.height(displayHeight.toDp()),
            style,
            if (displayHeight !=
                result.size.height
            ) {
                TextOverflow.Clip
            } else {
                TextOverflow.Ellipsis
            }
        )
    }
}

@Composable
fun CollapsibleText(
    content: String,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    collapsedLines: Int = 5,
    expandedLines: Int = Int.MAX_VALUE,
    animationDuration: Int = 400
) {
    CollapsibleText(
        modifier = modifier,
        style = style,
        expanded = expanded,
        measure = { measure(content) },
        collapsedLines = collapsedLines,
        expandedLines = expandedLines,
        animationDuration = animationDuration
    ) { innerModifier, innerStyle, overflow ->
        Text(
            text = content,
            modifier = innerModifier,
            style = innerStyle,
            overflow = overflow
        )
    }
}

@Composable
fun CollapsibleText(
    content: AnnotatedString,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    collapsedLines: Int = 5,
    expandedLines: Int = Int.MAX_VALUE,
    animationDuration: Int = 400
) {
    CollapsibleText(
        modifier = modifier,
        style = style,
        expanded = expanded,
        measure = { measure(content) },
        collapsedLines = collapsedLines,
        expandedLines = expandedLines,
        animationDuration = animationDuration
    ) { innerModifier, innerStyle, overflow ->
        Text(
            text = content,
            modifier = innerModifier,
            style = innerStyle,
            overflow = overflow
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
