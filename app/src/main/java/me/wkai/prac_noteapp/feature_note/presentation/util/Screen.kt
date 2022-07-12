package me.wkai.prac_noteapp.feature_note.presentation.util

//導航:各頁面集合
sealed class Screen(val route:String) {
	object NotesScreen : Screen("notes_screen")
	object AddEditNoteScreen : Screen("add_edit_note_screen")
}
