package me.wkai.prac_noteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.wkai.prac_noteapp.feature_note.domain.model.Note
import me.wkai.prac_noteapp.feature_note.domain.use_case.NoteUseCases
import me.wkai.prac_noteapp.feature_note.domain.util.NoteOrder
import me.wkai.prac_noteapp.feature_note.domain.util.OrderType
import javax.inject.Inject

//note排序頁_VM
@HiltViewModel
class NotesViewModel @Inject constructor(
	private val noteUseCases:NoteUseCases
) : ViewModel() {

	private val _state = mutableStateOf(NoteState())
	val state : State<NoteState> = _state

	private var recentlyDeletedNote: Note? = null //最新刪除的note

	private var getNotesJob: Job? = null //執行取得notes中的job

	//初始化:取得notes(日期降序)
	init {
		getNotes(NoteOrder.Date(OrderType.Descending))
	}
	

	fun onEvent(event:NotesEvent) {
		when(event) {
			is NotesEvent.Order -> { //排序
				//排序類型已相同, 不做事
				if(state.value.noteOrder::class == event.noteOrder::class &&
						state.value.noteOrder.orderType == event.noteOrder.orderType) {
					return
				}
				//排序
				getNotes(event.noteOrder)
			}
			is NotesEvent.DeleteNote -> { //刪除note
				viewModelScope.launch {
					noteUseCases.deleteNote(event.note)
					recentlyDeletedNote = event.note
				}
			}
			is NotesEvent.RestoreNote -> { //恢復note
				viewModelScope.launch {
					noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
					recentlyDeletedNote = null
				}
			}
			is NotesEvent.ToggleOrderSection -> {
				_state.value = state.value.copy(
					isOrderSectionVisible = !state.value.isOrderSectionVisible
				)
			}
		}
	}

	//flow onEach:迴圈,並可在裡面延遲操作(ex.delay(300))
	//launchIn: 指定在哪個線程執行
	private fun getNotes(noteOrder:NoteOrder) {
		getNotesJob?.cancel() //若有之前的job,取消之前的job
		getNotesJob = noteUseCases.getNotes(noteOrder)
			.onEach { notes->
				_state.value = _state.value.copy(
					notes = notes,
					noteOrder = noteOrder
				)
			}
			.launchIn(viewModelScope)
	}
}