package me.wkai.prac_noteapp.feature_note.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.wkai.prac_noteapp.feature_note.domain.model.Note

@Dao
interface NoteDao {

	//用Flow可進行許多額外處理
	// 1.可使用 foreach, map, filter 等常用方法
	// 2.使用 .launchIn(viewModelScope) 可決定要用的線程(scope)
	// 3.查詢時儲存Job, 每次查詢時取消前一個Job, 保持線程的處理乾淨
	@Query("SELECT * FROM note")
	fun getNotes():Flow<List<Note>>

	//因為直接返回Note, 所以使用suspend
	@Query("SELECT * FROM note WHERE id = :id")
	suspend fun getNoteById(id:Int):Note?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertNote(note:Note)

	@Delete
	suspend fun deleteNote(note:Note)

}