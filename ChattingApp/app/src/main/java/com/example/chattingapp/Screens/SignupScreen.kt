package com.example.chattingapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chattingapp.CheckSignedIn
import com.example.chattingapp.Commonprogressbar
import com.example.chattingapp.DestinationScreen
import com.example.chattingapp.LCViewmodel
import com.example.chattingapp.R
import com.example.chattingapp.navigateTo

@Composable
fun SignupScreen(navController: NavController, vm: LCViewmodel) {


    CheckSignedIn(vm,navController)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var namestate = remember {
                mutableStateOf(TextFieldValue())
            }

            var emailstate = remember {
                mutableStateOf(TextFieldValue())
            }
            var phonenostate = remember {
                mutableStateOf(TextFieldValue())
            }
            var passwordstate = remember {
                mutableStateOf(TextFieldValue())
            }


            val focus= LocalFocusManager.current

            Image(
                painter = painterResource(R.drawable.talk),
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Sign up",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = namestate.value,
                onValueChange = { namestate.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Name") })
            OutlinedTextField(
                value = emailstate.value,
                onValueChange = { emailstate.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Email Id") })
            OutlinedTextField(
                value = phonenostate.value,
                onValueChange = { phonenostate.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Phone number") })
            OutlinedTextField(
                value = passwordstate.value,
                onValueChange = { passwordstate.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Password") })
            Button(onClick =
                {
                vm.signUp(
                    name = namestate.value.text,
                    email =emailstate.value.text,
                    ph_number = phonenostate.value.text,
                    password = passwordstate.value.text
                )
                }) {
                Text(text = "Sign Up", modifier = Modifier.padding(8.dp))
            }

            Text(
                text = "Already a user ? Login - > ",
                modifier = Modifier.padding(15.dp)
                    .clickable{
                        navigateTo(navController, DestinationScreen.Login_obj.route)
                    }
                ,
                fontFamily = FontFamily.Serif,
                fontSize = 19.sp,
                color = Color.DarkGray

            )

        }


    }

    if(vm.inprocess.value){
        Commonprogressbar()
    }
}