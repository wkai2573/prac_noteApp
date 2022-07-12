package me.wkai.prac_noteapp.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

//編輯note頁_事件
sealed class AddEditNoteEvent {
	//編輯標題&內文、切換焦點、切換顏色、儲存
	data class EnteredTitle(val value:String) : AddEditNoteEvent()
	data class ChangeTitleFocus(val focusState:FocusState) : AddEditNoteEvent()
	data class EnteredContent(val value:String) : AddEditNoteEvent()
	data class ChangeContentFocus(val focusState:FocusState) : AddEditNoteEvent()
	data class ChangeColor(val color:Int) : AddEditNoteEvent()
	object SaveNote : AddEditNoteEvent()
}