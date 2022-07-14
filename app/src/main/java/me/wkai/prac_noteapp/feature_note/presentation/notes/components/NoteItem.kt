package me.wkai.prac_noteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import me.wkai.prac_noteapp.feature_note.domain.model.Note

//單個筆記
@Composable
fun NoteItem(
	note:Note,
	modifier:Modifier = Modifier,
	cornerRadius:Dp = 10.dp,  //圓角
	cutCornerSize:Dp = 30.dp, //裁角
	onDeleteClick:() -> Unit,
) {
	Box(
		modifier = modifier
	) {
		//畫背景, Canvas=畫布
		Canvas(modifier = Modifier.matchParentSize()) {
			//畫出範圍:左上->裁角1->裁角2->右下->左下->左上
			val clipPath = Path().apply {
				lineTo(size.width - cutCornerSize.toPx(), 0f) //裁角左上
				lineTo(size.width, cutCornerSize.toPx())           //裁角右下
				lineTo(size.width, size.height)                    //右下
				lineTo(0f, size.height)                         //左下
				close()
			}

			clipPath(clipPath) {
				//背景(圓角矩形)
				drawRoundRect(
					color = Color(note.color),
					size = size,
					cornerRadius = CornerRadius(cornerRadius.toPx())
				)
				//深色部分(圓角矩形)
				drawRoundRect(
					color = Color(
						ColorUtils.blendARGB(note.color, 0x000000, 0.2f) //加深
					),
					topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
					size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
					cornerRadius = CornerRadius(cornerRadius.toPx())
				)
			}
		}

		//內容:標題&內文
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
				.padding(end = 32.dp) //note內部右邊留點區域
		) {
			Text(
				text = note.title,
				style = MaterialTheme.typography.h6,
				color = MaterialTheme.colors.onSurface,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis //溢出時使用"..."
			)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = note.content,
				style = MaterialTheme.typography.body1,
				color = MaterialTheme.colors.onSurface,
				maxLines = 10,
				overflow = TextOverflow.Ellipsis
			)
		}

		//刪除按鈕(右下)
		IconButton(
			onClick = onDeleteClick,
			modifier = Modifier.align(Alignment.BottomEnd)
		) {
			Icon(
				imageVector = Icons.Default.Delete,
				contentDescription = "Delete note",
				tint = MaterialTheme.colors.onSurface //onSurface:用於顯示在表面顏色頂部的文本和圖標的顏色。
			)
		}
	}
}