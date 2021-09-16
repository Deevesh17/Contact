package com.example.contact.model

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Base64
import com.example.contact.viewmodel.ContactViewModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.PeopleServiceScopes
import com.google.api.services.people.v1.model.Person
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.*

class GoogleContactSync(val context: Context, val viewModel: ContactViewModel?, val user:String) {
    val SCOPES = Arrays.asList(PeopleServiceScopes.CONTACTS_READONLY)
    var HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    var json = GsonFactory.getDefaultInstance()
    private val contactDb = DBHelper(context)

    inner class GoogleContacts: AsyncTask<String, Void, Void>(){
        lateinit var type :String
        lateinit var service : PeopleService
        override fun doInBackground(vararg params: String?): Void? {
            type = params[0].toString()
            if(params[0] == "Sync") {
                try {
                    val sharedPreferences: SharedPreferences = context.getSharedPreferences("com.example.contact.user",
                        Context.MODE_PRIVATE)
                    val user  = sharedPreferences.getString("email","")
                    val credential =  GoogleAccountCredential.usingOAuth2(context,SCOPES);
                    credential.selectedAccountName = user
                    service = PeopleService.Builder(HTTP_TRANSPORT, json, credential)
                            .setApplicationName("Contact")
                            .build()

                    val response = service.people().connections()
                        .list("people/me")
                        .setPersonFields("nicknames,relations,addresses,photos,emailAddresses,phoneNumbers,names")
                        .execute()

                    val connections: List<Person>? = response.connections
                    if (connections != null && connections.size > 0) {
                        for (person in connections) {
                            val url = URL(person.photos.get(0)?.url)
                            val bitMap =
                                BitmapFactory.decodeStream(url.openConnection().getInputStream())
                            val stream = ByteArrayOutputStream()
                            bitMap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                            val byte = stream.toByteArray()
                            val base64image = Base64.encodeToString(byte, Base64.DEFAULT)
                            var address: String? =
                                person.addresses?.get(0)?.city + person.addresses?.get(0)?.country + person.addresses?.get(
                                    0
                                )?.streetAddress
                            if (address == "nullnullnull") address = null
                            if (person.phoneNumbers?.get(0)?.canonicalForm != null) {
                                try {
                                    contactDb.insertuserdata(
                                        person.names?.get(0)?.getDisplayName(),
                                        person.phoneNumbers?.get(0)?.canonicalForm,
                                        base64image,
                                        person.occupations?.get(0)?.value,
                                        person.emailAddresses?.get(0)?.value,
                                        person.relations?.get(0)?.person,
                                        address,
                                        person.nicknames?.get(0)?.value,
                                        null,
                                        null,
                                        user
                                    )
                                } catch (e: Exception) {
                                   println(e)
                                }
                            }
                        }
                    } else {
                        println("No connections found.")
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }
            return null
        }
        override fun onPostExecute(result: Void?) {
            if(type == "Sync"){
                viewModel?.SaveResult?.value = "Success"
            }
        }
    }

}