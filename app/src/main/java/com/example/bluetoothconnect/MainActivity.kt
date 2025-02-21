package com.example.bluetoothconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bluetoothconnect.services.BluetoothHelper
import com.example.bluetoothconnect.ui.theme.BluetoothConnectTheme
import com.example.bluetoothconnect.utils.BluetoothPermissions
import com.example.bluetoothconnect.views.BluetoothScreen

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothHelper: BluetoothHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bluetoothHelper = BluetoothHelper(this)
        setContent {
            BluetoothConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "bluetoothScreen") {
                        composable(
                            route = "bluetoothScreen"
                        ){
                            BluetoothScreen(bluetoothHelper, innerPadding)
                        }


                    }

                }
            }
            if (!BluetoothPermissions.hasPermissions(this)) {
                BluetoothPermissions.requestPermissions(this, 1)
            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BluetoothConnectTheme {
        Greeting("Android")
    }
}