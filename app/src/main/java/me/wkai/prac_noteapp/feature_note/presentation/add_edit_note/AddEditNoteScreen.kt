package me.wkai.prac_noteapp.feature_note.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.wkai.prac_noteapp.feature_note.domain.model.Note
import me.wkai.prac_noteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField

//編輯note頁面
@Composable
fun AddEditNoteScreen(
	navController:NavController,
	noteColor:Int,
	viewModel:AddEditNoteViewModel = hiltViewModel()
) {

	val titleState = viewModel.noteTitle.value
	val contentState = viewModel.noteContent.value

	val scaffoldState = rememberScaffoldState() //鷹架state
	val scope = rememberCoroutineScope()

	//note背景動畫: 切換顏色時,顏色漸變
	val noteBackgroundAnimatable = remember {
		Animatable(
			Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
		)
	}

	//發射效果(一次觸發執行一次)
	LaunchedEffect(key1 = true) {
		viewModel.eventFlow.collectLatest { event ->
			when (event) {
				//小吃提示
				is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
					scaffoldState.snackbarHostState.showSnackbar(message = event.message)
				}
				//儲存返回上頁
				is AddEditNoteViewModel.UiEvent.SaveNote -> {
					navController.navigateUp()
				}
			}
		}
	}

	//鷹架
	Scaffold(
		floatingActionButton = {
			FloatingActionButton(
				onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
				backgroundColor = MaterialTheme.colors.primary
			) {
				Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
			}
		},
		scaffoldState = scaffoldState
	) {
		//應架中間內容
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(noteBackgroundAnimatable.value)
				.padding(16.dp)
		) {
			//選顏色區塊
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
			) {
				Note.noteColors.forEach { color ->
					val colorInt = color.toArgb()
					Box(
						modifier = Modifier
							.size(50.dp)
							.shadow(15.dp, CircleShape)
							.clip(CircleShape) //裁剪成圓形
							.background(color)
							.border(
								width = 3.dp,
								color = if (viewModel.noteColor.value == colorInt) Color.Black else Color.Transparent,
								shape = CircleShape
							)
							.clickable {
								//點擊切換顏色: 動畫 & 改顏色
								scope.launch {
									noteBackgroundAnimatable.animateTo(
										targetValue = Color(colorInt),
										animationSpec = tween(durationMillis = 500)
									)
								}
								viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
							}
					)
				}
			}
			Spacer(modifier = Modifier.height(16.dp))

			//輸入框:標題
			TransparentHintTextField(
				text = titleState.text,
				hint = titleState.hint,
				onValueChange = {
					viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
				},
				onFocusChange = {
					viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
				},
				isHintVisible = titleState.isHintVisible,
				singleLine = true,
				textStyle = MaterialTheme.typography.h5,
			)
			Spacer(modifier = Modifier.height(16.dp))

			//輸入框:內文
			TransparentHintTextField(
				text = contentState.text,
				hint = contentState.hint,
				onValueChange = {
					viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
				},
				onFocusChange = {
					viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
				},
				isHintVisible = contentState.isHintVisible,
				textStyle = MaterialTheme.typography.body1,
				modifier = Modifier.fillMaxHeight()
			)

		}
	}

}