package me.wkai.prac_noteapp.feature_note.presentation.notes

import me.wkai.prac_noteapp.feature_note.domain.model.Note
import me.wkai.prac_noteapp.feature_note.domain.util.NoteOrder

//note頁事件
sealed class NotesEvent {
	data class Order(val noteOrder:NoteOrder) : NotesEvent()
	data class DeleteNote(val note:Note) : NotesEvent()
	object RestoreNote : NotesEvent()
	object ToggleOrderSection : NotesEvent()
}
