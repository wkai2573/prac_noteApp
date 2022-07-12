package me.wkai.prac_noteapp.feature_note.presentation.add_edit_note

//包裝類, 有關聯的變數包在一起
data class NoteTextFieldState(
	val text:String = "",
	val hint:String = "",
	val isHintVisible:Boolean = true
)
