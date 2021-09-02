package com.example.contact.model

import android.graphics.Bitmap

data class ContactData(var contactId : Int,var name : String?, var number : String?, var profile : Bitmap? = null,
                       var company: String? = null,var email: String? = null,var contactgroup: String? = null,
                       var address : String? = null,var nickname: String? = null,var website: String? = null,var notes: String? = null)
