package me.wkai.prac_noteapp.feature_note.presentation.add_edit_note

import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.wkai.prac_noteapp.feature_note.domain.model.InvalidNoteException
import me.wkai.prac_noteapp.feature_note.domain.model.Note
import me.wkai.prac_noteapp.feature_note.domain.use_case.NoteUseCases
import javax.inject.Inject

//編輯note頁_VM
@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
	private val noteUseCases:NoteUseCases,
	savedStateHandle:SavedStateHandle, //導航參數
) : ViewModel() {

	//目前noteId (null則新增)
	private var currentNoteId:Int? = null


	//標題
	private val _noteTitle = mutableStateOf(
		NoteTextFieldState(
			hint = "Enter title..."
		)
	)
	val noteTitle:State<NoteTextFieldState> = _noteTitle

	//內文
	private val _noteContent = mutableStateOf(
		NoteTextFieldState(
			hint = "Enter some content"
		)
	)
	val noteContent:State<NoteTextFieldState> = _noteContent

	//顏色
	private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
	val noteColor:State<Int> = _noteColor


	//事件流: 該事件不保持,而是發生時就執行一次, 使用MutableSharedFlow
	// 例如: 旋轉方向後, 標題&內文&顏色 將會保持, 而小吃提示應只跳出一次
	private val _eventFlow = MutableSharedFlow<UiEvent>()
	val eventFlow = _eventFlow.asSharedFlow()

	//初始化 = 進入編輯頁時
	// (當非-1) 設定目前的noteId = 點開的note,
	init {
		savedStateHandle.get<Int>("noteId")?.let { noteId ->
			if (noteId != -1) {
				viewModelScope.launch {
					//用noteId 找 note
					// 然後賦予初始值:id,標題,內文,顏色
					noteUseCases.getNote(noteId)?.also { note ->
						currentNoteId = note.id
						_noteTitle.value = noteTitle.value.copy(
							text = note.title,
							isHintVisible = false,
						)
						_noteContent.value = noteContent.value.copy(
							text = note.content,
							isHintVisible = false,
						)
						_noteColor.value = note.color
					}
				}
			}
		}
	}


	//事件
	fun onEvent(event:AddEditNoteEvent) {
		when (event) {
			is AddEditNoteEvent.EnteredTitle -> {
				_noteTitle.value = _noteTitle.value.copy(text = event.value)
			}
			is AddEditNoteEvent.ChangeTitleFocus -> {
				_noteTitle.value = _noteTitle.value.copy(
					//取消提示 = 當沒顯示 & 沒文字
					isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
				)
			}
			is AddEditNoteEvent.EnteredContent -> {
				_noteContent.value = _noteContent.value.copy(text = event.value)
			}
			is AddEditNoteEvent.ChangeContentFocus -> {
				_noteContent.value = _noteContent.value.copy(
					isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
				)
			}
			is AddEditNoteEvent.ChangeColor -> {
				_noteColor.value = event.color
			}
			is AddEditNoteEvent.SaveNote -> {
				viewModelScope.launch {
					try {
						noteUseCases.addNote(
							Note(
								title = noteTitle.value.text,
								content = noteContent.value.text,
								timestamp = System.currentTimeMillis(),
								color = noteColor.value,
								id = currentNoteId,
							)
						)
						//執行單次事件:儲存note
						_eventFlow.emit(UiEvent.SaveNote)
					} catch (e:InvalidNoteException) {
						//執行單次事件:秀錯誤訊息
						_eventFlow.emit(UiEvent.ShowSnackbar(message = e.message ?: "Couldn't save note"))
					}
				}
			}
		}
	}

	//ui事件集合
	sealed class UiEvent {
		data class ShowSnackbar(val message:String) : UiEvent()
		object SaveNote : UiEvent()
	}

}