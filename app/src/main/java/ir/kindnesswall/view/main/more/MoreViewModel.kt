package ir.kindnesswall.view.main.more

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.repository.UserRepo

class MoreViewModel(
    private val userRepo: UserRepo
):ViewModel() {
    @SuppressLint("CommitPrefEdits")
    fun NumberStatus(view : View){

        // if (UserInfoPref.bearerToken.length!=0){
        //     var shPref: SharedPreferences = view.context.getSharedPreferences(UserInfoPref.MyPref, Context.MODE_PRIVATE);
        //     when (view.id){
        //         R.id.more_none->
        //         {
        //             with (shPref.edit()) {
        //                 putString(UserInfoPref.keyName, "none")
        //                 apply()
        //             }
        //         }
        //         R.id.more_charity->{
        //             with (shPref.edit()) {
        //                 putString(UserInfoPref.keyName, "charity")
        //                 apply()
        //             }
        //         }
        //         R.id.more_all->{
        //             with (shPref.edit()) {
        //                 putString(UserInfoPref.keyName, "all")
        //                 apply()
        //             }
        //
        //         }
        //     }
        // }

    }

}