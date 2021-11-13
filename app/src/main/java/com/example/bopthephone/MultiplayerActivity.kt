package com.example.bopthephone

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random


class MultiplayerActivity : AppCompatActivity() , SensorEventListener{
    private var lastPerson: Boolean = false
    private lateinit var lobbyCode: String
    private lateinit var accelerometer: Sensor
    private lateinit var gyroscope: Sensor
    private lateinit var sensorManager: SensorManager

    private val gyroThreshold = 2f
    private val accelThreshold = 1.5f

    private lateinit var taskText: TextView
    private lateinit var scoreText: TextView
    private lateinit var startGameButton: Button
    private lateinit var tapItButton: Button
    private lateinit var twistItButton: Button
    private lateinit var pullItButton: Button
    private lateinit var countDownBar: ProgressBar

    private val tap = "Tap It!"
    private val twist = "Twist It!"
    private val pull = "Pull It!"

    private lateinit var currentTask: String
    private var cont : Boolean = false
    private var score : Int = 0
    private var countdown : Long = 3000
    private var countdownBarValue: Long = 0
    private val interval : Int = 100


    private lateinit var socketService: SocketService
    private var bound: Boolean = false

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("super123")
            val binder = service as SocketService.SocketBinder
            socketService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            println("nichtsuper123")
            bound = false
        }
    }

    override fun onResume() {
        super.onResume()
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

    }

    override fun onPause() {
        super.onPause()
        unbindService(mConnection)
    }


    override fun onStart() {
        super.onStart()
        Intent(this, SocketService::class.java).also { intent ->
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)

        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { this.accelerometer = it }
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let { this.gyroscope = it }

        scoreText = findViewById<TextView>(R.id.ScoreText)
        taskText = findViewById<TextView>(R.id.TaskText)
        startGameButton = findViewById<Button>(R.id.StartGameButton)
        tapItButton = findViewById<Button>(R.id.TapItButton)
        twistItButton = findViewById<Button>(R.id.TwistItButton)
        pullItButton = findViewById<Button>(R.id.PullItButton)
        countDownBar = findViewById<ProgressBar>(R.id.CountDownBar)

        scoreText.text = "score = $score"
        lobbyCode = intent.getStringExtra("LobbyCode")!!
    }

    fun onMessage(response: CallbackResponse<Message<String>>){
        when(response.data.type){
            "last-person"-> lastPerson = true

            "to-lobby"->{
                val toLobbyActivity: Intent = Intent(this, LobbyActivity()::class.java)
                val b: Bundle = Bundle()
                b.putString("LobbyCode", lobbyCode)
                    toLobbyActivity.putExtras(b)
                    startActivity(toLobbyActivity)
            }
        }
    }

    private fun gameRound(countdown: Long) {
        object : CountDownTimer(countdown.toLong(), interval.toLong()) {

            override fun onTick(l: Long) {
                countdownBarValue -= interval
                taskText.text = currentTask
                scoreText.text = "score = $score"
                countDownBar.progress = countdownBarValue.toInt()
                if (cont) {
                    cont = false
                    score++
                    countdownBarValue = countdown - countdown / 100
                    countDownBar.max = countdownBarValue.toInt()
                    gameRound(countdown - countdown / 100)
                    cancel()
                }
                if (lastPerson){
                    taskText.text = "You win!!!"
                    cancel()

                }
            }

            override fun onFinish() {
                taskText.text = "Game Over!"
                startGameButton.visibility = View.VISIBLE
                tapItButton.visibility = View.INVISIBLE
                twistItButton.visibility = View.INVISIBLE
                pullItButton.visibility = View.INVISIBLE
                countDownBar.visibility = View.INVISIBLE
                socketService.sendMessage(Json.encodeToString(Message("game-over", lobbyCode)))
            }
        }.start()
    }

    override fun onSensorChanged(event: SensorEvent){
        when (event.sensor?.type){
            Sensor.TYPE_ACCELEROMETER ->{
                if (event.values[1] - 9.81f >= accelThreshold) {
                    if (currentTask === pull) {
                        cont = true
                    }
                }
            }
        }

        when (event.sensor?.type){
            Sensor.TYPE_GYROSCOPE ->{
                if (event.values[1] >= gyroThreshold) {
                    if (currentTask === twist) {
                        cont = true
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    fun twistItClick(view: View) {
        if (currentTask === twist) {
            cont = true
        }
    }

    fun pullItClick(view: View) {
        if (currentTask === pull) {
            cont = true
        }
    }

    fun tapItClick(view: View) {
        if (currentTask === tap) {
            cont = true
        }
    }
}