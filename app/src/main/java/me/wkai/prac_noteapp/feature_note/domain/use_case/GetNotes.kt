package me.wkai.prac_noteapp.feature_note.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.wkai.prac_noteapp.feature_note.data.repository.NoteRepository
import me.wkai.prac_noteapp.feature_note.domain.model.Note
import me.wkai.prac_noteapp.feature_note.domain.util.NoteOrder
import me.wkai.prac_noteapp.feature_note.domain.util.OrderType

//定義noteRepoImpl各種用法 (ex.不直接在noteRepoImpl 實作排序, 而是將排序等邏輯 另外寫在此use_case)
class GetNotes(
	private val repository:NoteRepository
) {

	//複寫運算子
	operator fun invoke(
		noteOrder:NoteOrder = NoteOrder.Date(OrderType.Descending)
	):Flow<List<Note>> {
		return repository.getNotes().map { notes ->
			when (noteOrder.orderType) {
				is OrderType.Ascending -> {
					when (noteOrder) {
						is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
						is NoteOrder.Date -> notes.sortedBy { it.timestamp }
						is NoteOrder.Color -> notes.sortedBy { it.color }
					}
				}
				is OrderType.Descending -> {
					when (noteOrder) {
						is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
						is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
						is NoteOrder.Color -> notes.sortedByDescending { it.color }
					}
				}
			}
		}
	}
}