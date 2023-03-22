package com.example.medicmado2.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicmado2.ui.theme.primaryColor
import com.example.medicmado2.ui.theme.primaryDisabledColor
import com.example.medicmado2.ui.theme.strokeColor
import com.example.medicmado2.R
import com.example.medicmado2.ui.theme.inputColor

/*
Описание: Кнопка приложения
Дата создания: 22.03.2023 15:25
Автор: Георгий Хасанов
 */
@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(backgroundColor = primaryColor, disabledBackgroundColor = primaryDisabledColor),
    fontSize: TextUnit = 17.sp,
    fontWeight: FontWeight = FontWeight.W600,
    color: Color = Color.White,
    borderStroke: BorderStroke = BorderStroke(0.dp, strokeColor),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: () -> Unit
) {
    Button(
        border = borderStroke,
        colors = colors,
        enabled = enabled,
        contentPadding = contentPadding,
        elevation = ButtonDefaults.elevation(0.dp),
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color
        )
    }
}

/*
Описание: Кнопка приложения
Дата создания: 22.03.2023 15:25
Автор: Георгий Хасанов
 */
@Composable
fun AppBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = inputColor,
        elevation = 0.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Icon(painter = painterResource(
            id = R.drawable.ic_back),
            contentDescription = "",
            modifier = Modifier.padding(6.dp)
        )
    }
}