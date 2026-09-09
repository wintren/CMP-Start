package com.template.launch.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.template.app.App
import com.template.app.navigation.Navigator
import com.template.app.navigation.destinationOf
import com.template.app.navigation.stackFor
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Before the first composition, so the saved session does not overwrite the link.
        openLink(intent)
        setContent { App() }
    }

    /** The activity is `singleTop` by default here only for a warm launch; this covers it. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        openLink(intent)
    }

    /** `<applicationId>://forecast/42` — the host is the first route segment, the path the rest. */
    private fun openLink(intent: Intent?) {
        val link = intent?.data ?: return
        val route = link.host.orEmpty() + link.path.orEmpty()
        destinationOf(route)?.let { navigator.restore(stackFor(it)) }
    }
}
