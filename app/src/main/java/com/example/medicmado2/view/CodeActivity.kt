package com.example.medicmado2.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.example.medicmado2.PasswordActivity
import com.example.medicmado2.ui.components.AppBackButton
import com.example.medicmado2.ui.components.AppTextField
import com.example.medicmado2.ui.theme.MedicMADO2Theme
import com.example.medicmado2.ui.theme.descriptionColor
import com.example.medicmado2.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

/*
Описание: Класс экрана проверки кода
Дата создания: 22.03.2023 14:50
Автор: Георгий Хасанов
 */
class CodeActivity : ComponentActivity() {
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
    Описание: Контент экрана проверки кода
    Дата создания: 22.03.2023 14:50
    Автор: Георгий Хасанов
     */
    @Composable
    fun ScreenContent() {
        val mContext = LocalContext.current
        val shared = this.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        var code1 by rememberSaveable { mutableStateOf("") }
        var code2 by rememberSaveable { mutableStateOf("") }
        var code3 by rememberSaveable { mutableStateOf("") }
        var code4 by rememberSaveable { mutableStateOf("") }

        var timeLeft by rememberSaveable { mutableStateOf(60) }
        LaunchedEffect(timeLeft) {
            delay(1000)

            if (timeLeft > 0) {
                timeLeft -= 1
            } else {
                timeLeft = 60
                viewModel.sendCode(intent.getStringExtra("email").toString())
            }
        }

        var isAlertVisible by rememberSaveable { mutableStateOf(false) }
        var isLoading by rememberSaveable { mutableStateOf(false) }

        val token by viewModel.token.observeAsState()
        LaunchedEffect(token) {
            if (token != null) {
                with(shared.edit()) {
                    putString("token", token)
                    apply()
                }

                val intent = Intent(mContext, PasswordActivity::class.java)
                startActivity(intent)
            }

            isLoading = false
        }


        val errorMessage by viewModel.errorMessage.observeAsState()
        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                isAlertVisible = true
            }

            isLoading = false
        }

        Scaffold(
            topBar = {
                AppBackButton(modifier = Modifier.padding(start = 20.dp, top = 24.dp)) {
                    onBackPressed()
                }
            }
        ) { paddingValues ->  
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = "Введите код из E-mail",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W600,
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppTextField(
                            value = code1,
                            onValueChange = {
                                 if (it.length <= 1) {
                                     code1 = it
                                 }
                            },
                            contentPadding = PaddingValues(0.dp),
                            textStyle = TextStyle(fontSize = 20.sp, lineHeight = 0.sp, textAlign = TextAlign.Center),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        AppTextField(
                            value = code2,
                            onValueChange = {
                                if (it.length <= 1) {
                                    code2 = it
                                }
                            },
                            contentPadding = PaddingValues(0.dp),
                            textStyle = TextStyle(fontSize = 20.sp, lineHeight = 0.sp, textAlign = TextAlign.Center),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        AppTextField(
                            value = code3,
                            onValueChange = {
                                if (it.length <= 1) {
                                    code3 = it
                                }
                            },
                            contentPadding = PaddingValues(0.dp),
                            textStyle = TextStyle(fontSize = 20.sp, lineHeight = 0.sp, textAlign = TextAlign.Center),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        AppTextField(
                            value = code4,
                            onValueChange = {
                                if (it.length <= 1) {
                                    code4 = it
                                }

                                if (it.length == 1) {
                                    viewModel.checkCode(intent.getStringExtra("email").toString(), "$code1$code2$code3$code4")
                                    isLoading = true
                                }
                            },
                            contentPadding = PaddingValues(0.dp),
                            textStyle = TextStyle(fontSize = 20.sp, lineHeight = 0.sp, textAlign = TextAlign.Center),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Отправить код повторно можно будет через $timeLeft секунд",
                        fontSize = 15.sp,
                        color = descriptionColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 250.dp)
                    )
                }
            }
        }

        if (isLoading) {
            Dialog(onDismissRequest = {}) {
                CircularProgressIndicator()
            }
        }

        if (isAlertVisible) {
            AlertDialog(
                onDismissRequest = { isAlertVisible = false },
                title = { Text("Ошибка") },
                text = { Text(text = viewModel.errorMessage.value.toString()) },
                buttons = {
                    TextButton(onClick = { isAlertVisible = false }) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }
}