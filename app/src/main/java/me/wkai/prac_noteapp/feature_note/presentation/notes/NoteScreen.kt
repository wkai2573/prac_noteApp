package me.wkai.prac_noteapp.feature_note.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.wkai.prac_noteapp.feature_note.domain.util.NoteOrder
import me.wkai.prac_noteapp.feature_note.presentation.notes.components.NoteItem
import me.wkai.prac_noteapp.feature_note.presentation.notes.components.OrderSection
import me.wkai.prac_noteapp.feature_note.presentation.util.Screen

@Composable
fun NotesScreen(
	navController:NavController,
	viewModel:NotesViewModel = hiltViewModel()
) {

	val state = viewModel.state.value
	val scaffoldState = rememberScaffoldState() //鷹架state
	val scope = rememberCoroutineScope()

	Scaffold(
		//浮動按鈕
		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					//點擊進入 編輯頁(不帶參數,預設帶-1)
					navController.navigate(Screen.AddEditNoteScreen.route)
				},
				backgroundColor = MaterialTheme.colors.primary
			) {
				Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
			}
		},
		scaffoldState = scaffoldState
	) {
		//鷹架中間內容
		Column(
			modifier = Modifier.fillMaxSize().padding(16.dp)
		) {

			//大標題區塊
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween, //平均分配(左右不留空,因只有2組件所以會置左&置右)
				verticalAlignment = Alignment.CenterVertically, //垂直置中
			) {
				Text(text = "Your note", style = MaterialTheme.typography.h4)
				//收合排序按鈕
				IconButton(
					onClick = {
						viewModel.onEvent(NotesEvent.ToggleOrderSection)
					},
				) {
					Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
				}
			}

			//排序區塊(+收合動畫)
			AnimatedVisibility(
				visible = state.isOrderSectionVisible,
				enter = fadeIn() + slideInVertically(),
				exit = fadeOut() + slideOutVertically(),
			) {
				OrderSection(
					modifier = Modifier.fillMaxWidth(),
					noteOrder = state.noteOrder,
					onOrderChange = {
						viewModel.onEvent(NotesEvent.Order(it))
					}
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			//note列表區塊
			LazyColumn(modifier = Modifier.fillMaxSize()) {
				items(state.notes) { note ->
					NoteItem(
						note = note,
						modifier = Modifier
							.fillMaxWidth()
							.clickable {
								//點擊進入 編輯頁(帶參數)
								navController.navigate(
									Screen.AddEditNoteScreen.route +
										"?noteId=${note.id}&noteColor=${note.color}"
								)
							},
						onDeleteClick = {
							viewModel.onEvent(NotesEvent.DeleteNote(note))
							//小吃提示
							scope.launch {
								val result = scaffoldState.snackbarHostState.showSnackbar(
									message = "Note deleted",
									actionLabel = "Undo" //小吃右邊的按鈕_重做
								)
								if (result == SnackbarResult.ActionPerformed) {
									viewModel.onEvent(NotesEvent.RestoreNote)
								}
							}
						}
					)
					Spacer(modifier = Modifier.height(16.dp))
				}
			}

		}
	}
}