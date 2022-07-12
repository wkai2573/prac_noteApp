package me.wkai.prac_noteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.wkai.prac_noteapp.ui.theme.*

@Entity
data class Note(
	val title:String,
	val content:String,
	val timestamp:Long, //時間戳
	val color:Int,
	@PrimaryKey val id:Int? = null
) {

	//靜態變數(與Room無關)
	companion object {
		//筆記顏色
		val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
	}

}

//自訂異常
class InvalidNoteException(message:String) : Exception(message)