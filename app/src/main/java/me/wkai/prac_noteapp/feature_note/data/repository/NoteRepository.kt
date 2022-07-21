package me.wkai.prac_noteapp.feature_note.data.repository

import kotlinx.coroutines.flow.Flow
import me.wkai.prac_noteapp.feature_note.data.data_source.NoteDao
import me.wkai.prac_noteapp.feature_note.domain.model.Note

//interface_NoteRepository的實例
class NoteRepository(
	private val dao: NoteDao
) {

	fun getNotes():Flow<List<Note>> {
		//如果 本地資料(room) 與 遠端資料(api), 在這裡進行邏輯處理
		return dao.getNotes()
	}

	suspend fun getNoteById(id:Int):Note? {
		return dao.getNoteById(id)
	}

	suspend fun insertNote(note:Note) {
		dao.insertNote(note)
	}

	suspend fun deleteNote(note:Note) {
		dao.deleteNote(note)
	}
}


/*
//interface_NoteRepository的實例
class NoteRepositoryImpl(
	private val dao: NoteDao
) :NoteRepository {

	override fun getNotes():Flow<List<Note>> {
		//如果 本地資料(room) 與 遠端資料(api), 在這裡進行邏輯處理
		return dao.getNotes()
	}

	override suspend fun getNoteById(id:Int):Note? {
		return dao.getNoteById(id)
	}

	override suspend fun insertNote(note:Note) {
		dao.insertNote(note)
	}

	override suspend fun deleteNote(note:Note) {
		dao.deleteNote(note)
	}
}
*/