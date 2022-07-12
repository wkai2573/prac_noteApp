package me.wkai.prac_noteapp.feature_note.domain.util

//定義排序類型
sealed class NoteOrder(val orderType:OrderType) {
	class Title(orderType:OrderType) : NoteOrder(orderType)
	class Date(orderType:OrderType) : NoteOrder(orderType)
	class Color(orderType:OrderType) : NoteOrder(orderType)

	//此fun, 讓 切換排序方式(orderType)時保持排序類型(noteOrder) 更加方便
	fun copy(orderType:OrderType):NoteOrder {
		return when(this) {
			is Title -> Title(orderType)
			is Date -> Date(orderType)
			is Color -> Color(orderType)
		}
	}
}
