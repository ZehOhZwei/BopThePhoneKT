package com.example.bopthephone

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.os.CountDownTimer as CountDownTimer1

class GameActivity : AppCompatActivity(), SensorEventListener {


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
    private var cont: Boolean = false
    private var score: Int = 0
    private var countdown: Long = 3000
    private var countdownBarValue: Long = 0
    private val interval: Int = 100


    override fun onStart() {
        super.onStart()
        Intent(this, SocketService::class.java).also { intent ->
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also { gyroscope ->
            sensorManager.registerListener(this,
                gyroscope,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI)
        }

        scoreText = findViewById<TextView>(R.id.ScoreText)
        taskText = findViewById<TextView>(R.id.TaskText)
        startGameButton = findViewById<Button>(R.id.StartGameButton)
        tapItButton = findViewById<Button>(R.id.TapItButton)
        twistItButton = findViewById<Button>(R.id.TwistItButton)
        pullItButton = findViewById<Button>(R.id.PullItButton)
        countDownBar = findViewById<ProgressBar>(R.id.CountDownBar)



        currentTask = chooseNextTask(Random.nextInt(until = 3))
        scoreText.text = "score = $score"
    }

    fun startGameClick(view: View) {

        startGameButton.visibility = View.INVISIBLE
        tapItButton.visibility = View.VISIBLE
//      twistItButton.visibility = View.VISIBLE
//      pullItButton.visibility = View.VISIBLE
        countDownBar.visibility = View.VISIBLE
        countdownBarValue = countdown
        score = 0

        gameRound(countdown)
    }

    private fun gameRound(countdown: Long) {
        object : CountDownTimer1(countdown, interval.toLong()) {

            override fun onTick(l: Long) {
                countdownBarValue -= interval
                taskText.text = currentTask
                scoreText.text = "score = $score"
                countDownBar.progress = countdownBarValue.toInt()
                if (cont) {
                    cont = false
                    score++
                    currentTask = chooseNextTask(Random.nextInt(until = 3))
                    countdownBarValue = countdown - countdown / 50
                    countDownBar.max = countdownBarValue.toInt()
                    gameRound(countdown - countdown / 50)
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
                startGameButton.text = "Play Again?"
            }
        }.start()
    }

    fun twistItClick(view: View) {
        if (currentTask == twist) cont = true
    }

    fun pullItClick(view: View) {
        if (currentTask == pull) cont = true
    }

    fun tapItClick(view: View) {
        if (currentTask == tap) cont = true
    }


    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                if (currentTask != pull) return
                if (event.values[1] - 9.81f < accelThreshold) return
                cont = true
            }
            Sensor.TYPE_GYROSCOPE -> {
                if (currentTask != twist) return
                if (event.values[1] < gyroThreshold) return
                cont = true
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    fun chooseNextTask(task: Int): String {
        return when (task) {
            0 -> tap
            1 -> twist
            2 -> pull
            else -> tap
        }
    }

}