package me.wkai.prac_noteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//單選按鈕
@Composable
fun DefaultRadioButton(
	text:String,
	selected:Boolean,
	onSelect:() -> Unit,
	modifier:Modifier = Modifier
) {
	Row(
		modifier = Modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		RadioButton(
			selected = selected,
			onClick = onSelect,
			colors = RadioButtonDefaults.colors( //記得盡量用material
				selectedColor = MaterialTheme.colors.primary,
				unselectedColor = MaterialTheme.colors.onBackground,
			)
		)
		Spacer(modifier = Modifier.width(2.dp))
		Text(text = text, style = MaterialTheme.typography.body1) //記得盡量用material
	}
}