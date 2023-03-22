package com.example.medicmado2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicmado2.ui.theme.descriptionColor
import com.example.medicmado2.ui.theme.onboardTitleColor
import com.google.accompanist.pager.ExperimentalPagerApi

/*
Описание: Компонент приветсвенного экрана
Дата создания: 22.03.2023 15:00
Автор: Георгий Хасанов
 */
@Composable
fun OnboardComponent(
    title: String,
    content: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 220.dp)
    ) {
        Text(
            title,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            color = onboardTitleColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(29.dp))
        Text(
            content,
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
            color = descriptionColor,
            textAlign = TextAlign.Center
        )
    }
}