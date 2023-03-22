package com.example.medicmado2.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicmado2.ui.theme.MedicMADO2Theme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import com.example.medicmado2.R
import com.example.medicmado2.ui.components.OnboardComponent
import com.example.medicmado2.ui.theme.primaryColor
import com.example.medicmado2.ui.theme.secondaryColor
import com.google.accompanist.pager.HorizontalPager

/*
Описание: Класс приветственного экрана
Дата создания: 22.03.2023 14:50
Автор: Георгий Хасанов
 */
class OnboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicMADO2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ScreenContent()
                }
            }
        }
    }

    /*
    Описание: Контент приветственного экрана
    Дата создания: 22.03.2023 14:50
    Автор: Георгий Хасанов
     */
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun ScreenContent() {
        val mContext = LocalContext.current
        val shared = this.getSharedPreferences("shared", Context.MODE_PRIVATE)

        var selectedImage by rememberSaveable {
            mutableStateOf(R.drawable.onboard_1)
        }

        val pagerState = rememberPagerState()

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect() {
                when (it) {
                    0 -> selectedImage = R.drawable.onboard_1
                    1 -> selectedImage = R.drawable.onboard_2
                    2 -> selectedImage = R.drawable.onboard_3
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Text(
                    text = if (pagerState.currentPage != 2) "Пропустить" else "Завершить",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = secondaryColor,
                    modifier = Modifier.padding(start = 30.dp).clickable {
                        with(shared.edit()) {
                            putBoolean("isFirstEnter", false)
                            apply()
                        }

                        val intent = Intent(mContext, LoginActivity::class.java)
                        startActivity(intent)
                    }
                )
                Image(
                    painter = painterResource(id = R.drawable.onboard_logo),
                    contentDescription = ""
                )
            }
            HorizontalPager(
                count = 3, state = pagerState,
                modifier = Modifier.widthIn(max = 220.dp)
            ) {
                when (it) {
                    0 -> {
                        OnboardComponent(
                            title = "Анализы",
                            content = "Экспресс сбор и получение проб"
                        )
                    }
                    1 -> {
                        OnboardComponent(
                            title = "Уведомления",
                            content = "Вы быстро узнаете о результатах"
                        )
                    }
                    2 -> {
                        OnboardComponent(
                            title = "Мониторинг",
                            content = "Наши врачи всегда наблюдают за вашими показателями здоровья"
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in 0..2) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(if (pagerState.currentPage == i) secondaryColor else Color.White)
                            .border(1.dp, secondaryColor, CircleShape)
                    )
                }
            }
            Image(
                painter = painterResource(selectedImage),
                contentDescription = "",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.height(210.dp).padding(bottom = 59.dp)
            )
        }
    }
}