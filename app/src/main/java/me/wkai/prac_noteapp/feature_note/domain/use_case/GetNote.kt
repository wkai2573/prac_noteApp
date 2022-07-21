package me.wkai.prac_noteapp.feature_note.domain.use_case

import me.wkai.prac_noteapp.feature_note.data.repository.NoteRepository
import me.wkai.prac_noteapp.feature_note.domain.model.Note

class GetNote(
	private val repository:NoteRepository
) {

	suspend operator fun invoke(id:Int):Note? {
		return repository.getNoteById(id)
	}
}