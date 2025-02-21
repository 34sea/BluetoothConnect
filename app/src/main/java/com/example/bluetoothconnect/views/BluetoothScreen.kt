package com.example.bluetoothconnect.views

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bluetoothconnect.services.BluetoothHelper
import com.example.bluetoothconnect.utils.BluetoothPermissions


@Composable
fun BluetoothScreen(bluetoothHelper: BluetoothHelper, paddingValues: PaddingValues) {
    var isBluetoothEnabled by remember { mutableStateOf(bluetoothHelper.isBluetoothEnabled()) }
    var pairedDevices by remember { mutableStateOf(emptyList<BluetoothDevice>()) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Bluetooth App")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (!BluetoothPermissions.hasPermissions(context)) {
                Toast.makeText(context, "Permissões não concedidas!", Toast.LENGTH_SHORT).show()
            } else {
                pairedDevices = bluetoothHelper.getPairedDevices()
            }
        }) {
            Text(text = "Listar Dispositivos Pareados")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pairedDevices) { device ->
                DeviceItem(device, bluetoothHelper)
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(device: BluetoothDevice, bluetoothHelper: BluetoothHelper) {
    var socket by remember { mutableStateOf<BluetoothSocket?>(null) }
    var receivedData by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (BluetoothPermissions.hasPermissions(context)) {
                    socket = bluetoothHelper.connectToDevice(device)
                    Toast.makeText(
                        context,
                        "Conectado a ${device.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "Permissões não concedidas!", Toast.LENGTH_SHORT).show()
                }
            }
            .padding(8.dp)
    ) {
        Text(text = device.name ?: "Dispositivo Desconhecido")
        Text(text = device.address)

        Button(onClick = {
            bluetoothHelper.sendData(socket, "Hello Device")
        }) {
            Text(text = "Enviar Dados")
        }

        Button(onClick = {

            receivedData = bluetoothHelper.receiveData(socket) ?: "Erro ao receber"
        }) {
            Text(text = "Receber Dados")
        }

        Text(text = "Recebido: $receivedData")
    }
}



