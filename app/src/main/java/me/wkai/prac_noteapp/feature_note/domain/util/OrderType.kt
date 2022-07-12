package me.wkai.prac_noteapp.feature_note.domain.util

//定義排序方式
sealed class OrderType {
	object Ascending: OrderType() //升序
	object Descending: OrderType() //降序
}
