package me.wkai.prac_noteapp.feature_note.presentation.notes

import me.wkai.prac_noteapp.feature_note.domain.model.Note
import me.wkai.prac_noteapp.feature_note.domain.util.NoteOrder
import me.wkai.prac_noteapp.feature_note.domain.util.OrderType

//包裝類: note頁viewModel中會用到的排序變數, 因操作有關連, 故包裝再一起
data class NoteState(
	val notes: List<Note> = emptyList(),
	val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
	val isOrderSectionVisible:Boolean = false
)
