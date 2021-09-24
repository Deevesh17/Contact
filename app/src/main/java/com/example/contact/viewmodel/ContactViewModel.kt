package com.example.contact.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contact.model.*
import com.example.contact.musicroomdb.Audio
import java.util.*

class ContactViewModel(private val ctx : Context) : ViewModel() {
    private val checkPassword: CheckPassword = CheckPassword(ctx)
    var contactDataLive : MutableLiveData<String> = MutableLiveData()
    var passwordResult: MutableLiveData<String> = MutableLiveData<String>()
    var SaveResult: MutableLiveData<String> = MutableLiveData<String>()
    var weatherResponce: MutableLiveData<String> = MutableLiveData<String>()
    var searchList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var contactList: MutableLiveData<ArrayList<ContactData>> = MutableLiveData()
    var audioCountResult : MutableLiveData<String> = MutableLiveData()
    var audiofile : MutableLiveData<List<Audio>> = MutableLiveData()

    fun setValutodata(title : String):Boolean{
        contactDataLive.value = title
        return true
    }

    fun setAudioCount(viewModel: ContactViewModel){
        val audioFileModel = AudioFileModel(ctx,viewModel)
        audioFileModel.getCountOfFileCoroutine()
    }
    fun setAudioFile(viewModel: ContactViewModel){
        val audioFileModel = AudioFileModel(ctx,viewModel)
        audioFileModel.getAudioFile()
    }
//    SETDATA FUNCTION OF LOGIN ACTIVITY
    fun setPasswordResult(email : String,Originalpassword : String){
        val loginPasswordModel = LoginPasswordModel(ctx)
        loginPasswordModel.CreateGoogleAccount(email)
        val result = loginPasswordModel.checkLoginPassword(email,Originalpassword)
        passwordResult.value = result
    }
    fun setCreateGoogleAccount(email : String){
        val loginPasswordModel = LoginPasswordModel(ctx)
        loginPasswordModel.CreateGoogleAccount(email)
    }
    fun setCreateFacebookAccount(email : String){
        val loginPasswordModel = LoginPasswordModel(ctx)
        loginPasswordModel.CreateFacebookAccount(email)
    }


    //    SETDATA FUNCTION OF SIGNUP ACTIVITY
    fun setSignupData(name :String, email : String, number : String, password : String, confirmPassword : String ){
        val signupPasswordModel = SignupModel(ctx)
        val result = signupPasswordModel.checkSignupPasswordAndWriteDB(name,email, number, password, confirmPassword)
        passwordResult.value = result
    }

//    SETDATA FUNCTION OF FORGET ACTIVITY
    fun setForgetData(email : String,mobileNumber : String){
        val fogetPasswordModel = ForgetPasswordModel(ctx)
        val result = fogetPasswordModel.checkForgetPasswordUser(email, mobileNumber)
        passwordResult.value = result
    }

//    SETDATA FUNCTION OF NEWPASSWORD FRAGMENT
    fun setNewPassword(email : String,password: String,confirmPassword: String){
        val result = checkPassword.createNewPassword(email,password, confirmPassword)
        passwordResult.value = result
    }
    fun setChangePassword(oldPassword: String,email : String,password: String,confirmPassword: String){
        val result = checkPassword.changeNewPassword(oldPassword,email,password, confirmPassword)
        passwordResult.value = result
    }

//    SETDATA FUNCTION OF GET DATA FROM DB
    fun setDbData(type : String, selectedList :ArrayList<ContactData>, signinUser : String,title: ContactViewModel){
        val contact = ContactdataRetrival(type,selectedList,signinUser,ctx,title)
        contact.getAndDeleteContactFromDB()
    }

//    SETDATA FUNCTION FOR SELECT CONTACT ACTIVITY
    fun setImportData( title: ContactViewModel,type : String, selectedList :ArrayList<ContactData>, signinUser :String){
        val import = ContactImportModule(ctx, title,selectedList,signinUser)
        import.ContactTask().execute(type)
    }

//    SETDATA FUNCTION FOR WEATHER API RESPONCE
    fun setWeatherrResopnse(distric : String,viewModel: ContactViewModel,user :String){
        val response = WeatherResponceModel(ctx)
        response.getWeather(distric,viewModel,user)
    }

//  SETDATA FUNCTION FOR RECENT SEARCH
    fun setRecentSearch(user :String){
        val response = RecentSearchModel(ctx)
        searchList.value = response.getSeachList(user)
    }

//    SETDATA FUNCTION FOR RECENT SEARCH
    fun setGoogleSync(user :String,viewModel: ContactViewModel){
        val response = GoogleContactSync(ctx,viewModel,user)
        response.GoogleContacts().execute("Sync")
    }

    //  SETDATA FUNCTION FOR ABOUT USER
    fun setAboutUser(user :String){
        val result = AboutUserModel(ctx)
        SaveResult.value = result.getUserDetails(user)
    }

//    Deleting all the user data

//    fun setDeleteResult(){
//        val result = DeleteUserDetails(ctx)
//        deleteResult.value = result.deleteContactFromDB()
//    }

}