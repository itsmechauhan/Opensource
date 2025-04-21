package com.example.chattingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chattingapp.Screens.ChatlistScreen
import com.example.chattingapp.Screens.LoginScreen
import com.example.chattingapp.Screens.SignupScreen
import com.example.chattingapp.ui.theme.ChattingAppTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route: String){
    object Signup_obj: DestinationScreen("signup")
    object Login_obj: DestinationScreen("login")
    object Profile_obj : DestinationScreen("profile")
    object Chatlist_obj: DestinationScreen("chatlist")
//    object signup: DestinationScreen("signup")

    object singleChat : DestinationScreen("singleChat/{childid}"){
        fun createRoute(id : String)= "Singlechat()/$id"
    }
    object singleStatus : DestinationScreen("singleStatus/{userid}"){
        fun createRoute(userid : String)= "Singlechat()/$userid"
    }


}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChattingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color= MaterialTheme.colorScheme.background

                ){
                    ChatAppnavigation()
                }


            }
        }
    }


    @Composable
    fun ChatAppnavigation() {
        val navigationController = rememberNavController()

        var vm=hiltViewModel<LCViewmodel>()
        NavHost(
            navController = navigationController,
            startDestination = DestinationScreen.Signup_obj.route
        )
        {

            composable(DestinationScreen.Signup_obj.route) {
                SignupScreen(navigationController,vm)

            }

            composable(DestinationScreen.Login_obj.route) {
                LoginScreen(vm,navigationController)
            }
            composable(DestinationScreen.Chatlist_obj.route) {
                ChatlistScreen()
            }

        }



    }
}
