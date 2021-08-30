package app.tryout.gtestapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.tryout.gtestapp.R
import app.tryout.gtestapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.gSignButton.setOnClickListener { startGLogin() }
    }

    private fun startGLogin() {
        val intent = GmailLoginUtils.getGmailLoginIntent(this)
        startActivityForResult(intent, REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE) {
            val authResult = GoogleSignIn.getSignedInAccountFromIntent(data)
            println("Got in on-activity-result: $authResult, data : ${authResult.result}")
        }
        else super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val REQ_CODE = 1001
    }
}


object GmailLoginUtils {
    fun getGmailLoginIntent(activity: Activity): Intent {
        val gso = GoogleSignInOptions.Builder()
            .requestProfile()
            .requestEmail()
            .requestScopes(
                Scope("https://www.googleapis.com/auth/gmail.labels"),
                Scope("https://www.googleapis.com/auth/gmail.readonly"),
                Scope("https://www.googleapis.com/auth/contacts.readonly")
            )
            .requestServerAuthCode("634808117148-a85ajome7p76vr03a1qgo3sinurrg013.apps.googleusercontent.com", true)
            .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        if (GoogleSignIn.getLastSignedInAccount(activity) != null)
            googleSignInClient.signOut()
        return googleSignInClient.signInIntent
    }
}