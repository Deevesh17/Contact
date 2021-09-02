package com.example.contact.model

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.util.Base64
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.PeopleServiceScopes
import com.google.api.services.people.v1.model.Person
import java.io.*
import java.net.URL
import java.util.*

class GoogleContactSync(val context: Context,val viewModel: ContactViewModel,val user :String) {
    val SCOPES = Arrays.asList(PeopleServiceScopes.CONTACTS_READONLY)
    val CREDENTIALS_FILE_PATH = "client_secret_653675639171-83k8kcdkaagbc4ukgfltquel4rk3kd3i.apps.googleusercontent.com.json"
    var HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    var json = GsonFactory.getDefaultInstance()
    private val contactDb = DBHelper(context)

    inner class GoogleContacts: AsyncTask<Void, Void, Void>(){
        lateinit var service : PeopleService
        override fun doInBackground(vararg params: Void?): Void? {

            service = PeopleService.Builder(HTTP_TRANSPORT, json, getCredentials(HTTP_TRANSPORT))
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
                    val bitMap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    val stream = ByteArrayOutputStream()
                    bitMap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                    val byte = stream.toByteArray()
                    val base64image = Base64.encodeToString(byte, Base64.DEFAULT)
                    var address  :String?= person.addresses?.get(0)?.city + person.addresses?.get(0)?.country + person.addresses?.get(
                        0
                    )?.streetAddress
                    if( address == "nullnullnull") address = null
                    if (person.phoneNumbers?.get(0)?.canonicalForm != null) {
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
                    }
                }
            } else {
                println("No connections found.")
            }
            return null
        }
        override fun onPostExecute(result: Void?) {
            viewModel.SaveResult.value = "Success"
        }
    }

    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {

        // Load client secrets.
        val `in`: InputStream = context.assets.open(CREDENTIALS_FILE_PATH)
        val clientSecrets = GoogleClientSecrets.load(json, InputStreamReader(`in`))

        // Build flow and trigger user authorization request.

        val tokenFile = File(context.filesDir,"tokens")
        val fileDataStore = FileDataStoreFactory(tokenFile)
        val flow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, json, clientSecrets, SCOPES)
            .setDataStoreFactory(fileDataStore)
            .setAccessType("offline")
            .build()

        val credential: AuthorizationCodeInstalledApp =
            object : AuthorizationCodeInstalledApp(flow, LocalServerReceiver()) {
                @Throws(IOException::class)
                override fun onAuthorization(authorizationUrl: AuthorizationCodeRequestUrl) {
                    val url = authorizationUrl.build()
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(browserIntent)
                }
            }
        val acct = GoogleSignIn.getLastSignedInAccount(context)

        return credential.authorize(acct.id).setAccessToken(acct.idToken)
    }
}