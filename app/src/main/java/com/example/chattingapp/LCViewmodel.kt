package com.example.chattingapp

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.chattingapp.Data.Event
import com.example.chattingapp.Data.USER_NODE
import com.example.chattingapp.Data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LCViewmodel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {



    //important variables

    var inprocess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signin = mutableStateOf(false)

    var userData =mutableStateOf<UserData?>(null)
    init {
        val currentUser=auth.currentUser
        signin.value=currentUser !=null
        currentUser?.uid?.let{
            getUserData(it)
        }

    }



    //sign up function
    fun signUp(name: String, email: String, ph_number: String, password: String) {
        Log.d("TAG", "signUp: Called")

        inprocess.value = true
        if(name.isEmpty() or ph_number.isEmpty() or email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please Fill All the Details")
        }

        //Checking that is the number already exist ? or not
      db.collection(USER_NODE).whereEqualTo("number",ph_number).get().addOnSuccessListener{

          //if the user contact number is not in DB
          if(it.isEmpty){

              auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                  //sign up is successful
                  if (task.isSuccessful) {
                      Log.d("tag", "signup: Successful")
                      signin.value=true
                      createOrupdateProfile(name,ph_number)
                      inprocess.value = false
                  } else {
                      handleException(task.exception, customMessage = "Signup Failed")
                      Log.d("tag", "signUp: Failed")
                      inprocess.value = false
                  }
              }
          }
          //if number already exist
          else{
              handleException(customMessage = "Number Already Exists")
          }
      }



    }


    //Login function
    fun loginIn(email: String,password: String){

        //if email or pass is empty
        if(email.isEmpty() or password.isEmpty()){
            handleException(customMessage = " Please fill all the Fields")
            return
        }
        //if email or pass is not empty
        else{
            inprocess.value=true

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    //if sign in is successful
                    if (it.isSuccessful){
                        signin.value=true
                        inprocess.value=false
                        //get the currently signed in user from firebase auth
                        //safely accesses the user's UID and execute getuserdata() fun if the user is not null.

                        auth.currentUser?.uid?.let{
                            getUserData(it)
                        }
                    }



                    else{
                        handleException(exception = it.exception, customMessage = "Login Failed")
                    }
                }
        }
    }

    //create profile
    fun createOrupdateProfile(name: String?=null,ph_number: String?=null,imageurl: String?=null){

        val uid=auth.currentUser?.uid
        val userData= UserData(
            userId=uid,
            name=name?:userData.value?.name,
            number = ph_number?:userData.value?.number,
            imageurl=imageurl?:userData.value?.imageurl

        )

        uid?.let{
            inprocess.value=true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {

                if(it.exists()){
                    //u[date user data
                }
                else{
                    db.collection(USER_NODE).document(uid).set(userData)
                    inprocess.value=false
                    getUserData(uid)
                }
            }
                .addOnFailureListener {
                    handleException(it, customMessage = "Cannot Retrieve User")
                }
        }
    }

    //fetching user data
        private fun getUserData(uid: String) {
        inprocess.value=true
        db.collection(USER_NODE).document(uid).addSnapshotListener {
            value,error->
            if (error!=null){
                handleException(error,"Cannot Retrieve user")
            }
            if (value !=null){
                var user= value.toObject(UserData::class.java)
                userData.value=user
                inprocess.value=false
            }
        }

    }


    //handle exception
    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("SignupExecption", "handleException: ", exception)
        exception?.printStackTrace()
        val errormsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errormsg else customMessage
        eventMutableState.value = Event(message)
        inprocess.value = false
    }






}

