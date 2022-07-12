package me.wkai.prac_noteapp.feature_note.domain.use_case

//定義 此noteApp 要用到的方法
// 並將這些方法, 包成class複寫invoke, 來方便使用
data class NoteUseCases(
	val getNotes:GetNotes,
	val deleteNote:DeleteNote,
	val addNote:AddNote,
	val getNote:GetNote,
)