package com.example.bluetoothconnect.services

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import com.example.bluetoothconnect.utils.BluetoothPermissions
import java.io.IOException
import java.util.UUID

class BluetoothHelper(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): List<BluetoothDevice> {
        if (!BluetoothPermissions.hasPermissions(context)) {
            return emptyList()
        }
        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }

    @SuppressLint("MissingPermission")
//    fun connectToDevice(device: BluetoothDevice): BluetoothSocket? {
//        return try {
//            val socket = device.createRfcommSocketToServiceRecord(device.uuids.first().uuid)
//            socket.connect()
//            socket
//        } catch (e: Exception) {
//            Log.e("Bluetooth", "Erro ao conectar: ${e.message}")
//            null
//        }
//    }

    fun connectToDevice(device: BluetoothDevice): BluetoothSocket? {
        val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        return try {
            val uuid = device.uuids?.firstOrNull()?.uuid ?: MY_UUID
            val socket = device.createRfcommSocketToServiceRecord(uuid)

            BluetoothAdapter.getDefaultAdapter().cancelDiscovery() // Parar descoberta antes de conectar

            socket.connect() // Tenta conectar
            Log.d("Bluetooth", "Conectado com sucesso a ${device.name}")
            socket
        } catch (e: IOException) {
            Log.d("Bluetooth", "Erro ao conectar ao dispositivo: ${e.message}")
            null
        }
    }


    fun sendData(socket: BluetoothSocket?, data: String) {
        try {
            socket?.outputStream?.write(data.toByteArray())
        } catch (e: Exception) {
            Log.e("Bluetooth", "Erro ao enviar dados: ${e.message}")
        }
    }

//    fun receiveData(socket: BluetoothSocket?): String? {
//        return try {
//            val buffer = ByteArray(1024)
//            val bytes = socket?.inputStream?.read(buffer)
//            bytes?.let { String(buffer, 0, it) }
//        } catch (e: Exception) {
//            Log.e("Bluetooth", "Erro ao receber dados: ${e.message}")
//            null
//        }
//    }
        fun receiveData(socket: BluetoothSocket?): String? {
            return try {
                if (socket == null) {
                    Log.e("Bluetooth", "Socket é null")
                    return "Erro: Socket não conectado"
                }

                val inputStream = socket.inputStream
                if (inputStream == null) {
                    Log.e("Bluetooth", "InputStream é null")
                    return "Erro: Falha ao acessar InputStream"
                }

                val buffer = ByteArray(1024)
                val bytes = inputStream.read(buffer)

                if (bytes == -1) {
                    Log.e("Bluetooth", "Nenhum dado recebido")
                    return "Erro: Nenhum dado recebido"
                }

                String(buffer, 0, bytes) // Converte bytes recebidos em string
            } catch (e: Exception) {
                Log.e("Bluetooth", "Erro ao receber dados: ${e.message}")
                "Erro ao receber: ${e.message}"
            }
        }

}
