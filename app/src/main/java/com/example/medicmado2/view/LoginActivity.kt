package com.example.medicmado2.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.example.medicmado2.ui.theme.MedicMADO2Theme
import com.example.medicmado2.R
import com.example.medicmado2.ui.components.AppButton
import com.example.medicmado2.ui.components.AppTextField
import com.example.medicmado2.ui.theme.descriptionColor
import com.example.medicmado2.ui.theme.strokeColor
import com.example.medicmado2.ui.theme.textColor
import com.example.medicmado2.viewmodel.LoginViewModel

/*
Описание: Класс экрана входа в аккаунт
Дата создания: 22.03.2023 14:50
Автор: Георгий Хасанов
 */
class LoginActivity : ComponentActivity() {
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
    Описание: Контент экрана входа в аккаунт
    Дата создания: 22.03.2023 14:50
    Автор: Георгий Хасанов
     */
    @Composable
    fun ScreenContent() {
        val mContext = LocalContext.current
        val shared = this.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        var emailText by rememberSaveable { mutableStateOf("") }
        var isEnabled by rememberSaveable { mutableStateOf(false) }

        var isAlertVisible by rememberSaveable { mutableStateOf(false) }
        var isLoading by rememberSaveable { mutableStateOf(false) }

        val responseCode by viewModel.responseCode.observeAsState()
        LaunchedEffect(responseCode) {
            if (responseCode == 200) {
                val intent = Intent(mContext, CodeActivity::class.java)
                intent.putExtra("email", emailText)

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
            ) {
                Image(painter = painterResource(
                    id = R.drawable.ic_emojies),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Добро пожаловать!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Войдите, чтобы пользоваться функциями приложения",
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                "Вход по E-mail",
                fontSize = 14.sp,
                color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            AppTextField(
                value = emailText,
                onValueChange = {
                    emailText = it

                    isEnabled = emailText.isNotEmpty()
                },
                placeholder = {
                    Text(
                        text = "example@mail.ru",
                        color = Color.Gray,
                        fontSize = 15.sp,
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            AppButton(
                text = "Далее",
                enabled = isEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (Regex("^[a-zA-Z0-9]*@[a-zA-Z0-9]*\\.[a-zA-Z]{2,}$").matches(emailText)) {
                    viewModel.sendCode(emailText)

                    isLoading = true
                } else {
                    Toast.makeText(mContext, "Неправильный формат Email!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 56.dp)
            ) {
                Text(
                    text = "Или войдите с помощью",
                    fontSize = 15.sp,
                    color = descriptionColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                AppButton(
                    text = "Войти с Яндекс",
                    fontWeight = FontWeight.W500,
                    color = Color.Black,
                    contentPadding = PaddingValues(18.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    borderStroke = BorderStroke(1.dp, strokeColor),
                    modifier = Modifier.fillMaxWidth()
                ) {

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