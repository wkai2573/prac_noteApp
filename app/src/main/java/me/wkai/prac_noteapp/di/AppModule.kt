package me.wkai.prac_noteapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.wkai.prac_noteapp.feature_note.data.data_source.NoteDatabase
import me.wkai.prac_noteapp.feature_note.data.repository.NoteRepository
import me.wkai.prac_noteapp.feature_note.domain.use_case.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	//生成db
	@Provides
	@Singleton
	fun provideNoteDatabase(app:Application):NoteDatabase {
		return Room.databaseBuilder(
			app,
			NoteDatabase::class.java,
			NoteDatabase.DATABASE_NAME
		).build()
	}

	//生成repository
	@Provides
	@Singleton
	fun provideNoteRepository(db:NoteDatabase):NoteRepository {
		return NoteRepository(db.noteDao)
	}

	//(重點)生成 note使用cases
	// 參考檔案:AddEditNoteViewModel.kt, NotesViewModel.kt, NoteUseCases.kt, AppModule.kt
	// (fun左邊圖示可以直接到關聯檔案)
	@Provides
	@Singleton
	fun provideNoteUseCases(repository:NoteRepository):NoteUseCases {
		return NoteUseCases(
			getNotes = GetNotes(repository),
			deleteNote = DeleteNote(repository),
			addNote = AddNote(repository),
			getNote = GetNote(repository)
		)
	}
}