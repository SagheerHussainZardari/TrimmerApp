package com.example.trimmerapp

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var sensorManager: SensorManager? = null
    var proximitySensor: Sensor? = null
    lateinit var mediaPlayer: MediaPlayer
    lateinit var sensorEventListener: SensorEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor =  sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        mediaPlayer = MediaPlayer.create(this , R.raw.clippersound) as MediaPlayer

        sensorEventListener = object: SensorEventListener{

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

            override fun onSensorChanged(p0: SensorEvent?) {

                if(p0!!.values[0] < proximitySensor!!.maximumRange) {
                    mediaPlayer.start()
                }else {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    mediaPlayer = MediaPlayer.create(this@MainActivity , R.raw.clippersound)
                }
            }
        }

        sensorManager!!.registerListener(sensorEventListener ,proximitySensor  ,2*1000*1000)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
        mediaPlayer.release()
        sensorManager!!.unregisterListener(sensorEventListener, proximitySensor)
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer = MediaPlayer.create(this@MainActivity , R.raw.clippersound)
        sensorManager!!.registerListener(sensorEventListener ,proximitySensor  ,2*1000*1000)

    }
}
