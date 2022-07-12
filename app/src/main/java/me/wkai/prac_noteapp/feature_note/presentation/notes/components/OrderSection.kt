package me.wkai.prac_noteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.wkai.prac_noteapp.feature_note.domain.util.NoteOrder
import me.wkai.prac_noteapp.feature_note.domain.util.OrderType

//排序區塊
@Composable
fun OrderSection(
	modifier:Modifier = Modifier,
	noteOrder:NoteOrder = NoteOrder.Date(OrderType.Descending),
	onOrderChange:(NoteOrder) -> Unit,
) {
	Column(modifier = modifier) {

		//3個排序按鈕_by標題|by日期|by顏色
		Row(modifier = Modifier.fillMaxWidth()) {
			DefaultRadioButton(
				text = "Title",
				selected = noteOrder is NoteOrder.Title,
				onSelect = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) }
			)
			Spacer(modifier = Modifier.width(8.dp))
			DefaultRadioButton(
				text = "Date",
				selected = noteOrder is NoteOrder.Date,
				onSelect = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) }
			)
			Spacer(modifier = Modifier.width(8.dp))
			DefaultRadioButton(
				text = "Color",
				selected = noteOrder is NoteOrder.Color,
				onSelect = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) }
			)
		}

		//2個排序方式_by升序|by降序
		Row(modifier = Modifier.fillMaxWidth()) {
			DefaultRadioButton(
				text = "Ascending",
				selected = noteOrder.orderType is OrderType.Ascending,
				onSelect = { onOrderChange(noteOrder.copy(OrderType.Ascending)) }
			)
			Spacer(modifier = Modifier.width(8.dp))
			DefaultRadioButton(
				text = "Descending",
				selected = noteOrder.orderType is OrderType.Descending,
				onSelect = { onOrderChange(noteOrder.copy(OrderType.Descending)) }
			)
		}
	}
}