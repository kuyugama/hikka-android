package online.nyam.hikka.ui.lists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.Pager
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> VerticalPagedGrid(
    pager: Pager<Int, T>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    itemPlaceholder: (@Composable () -> Unit)? = null,
    itemContent: @Composable (T) -> Unit
) {
    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey(key)) {
            val item = lazyPagingItems[it]

            if (item != null) {
                itemContent(item)
            } else {
                itemPlaceholder?.invoke()
            }
        }
    }
}
