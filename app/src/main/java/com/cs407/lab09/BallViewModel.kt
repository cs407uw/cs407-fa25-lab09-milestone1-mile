package com.cs407.lab09

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BallViewModel : ViewModel() {

    private var ball: Ball? = null
    private var lastTimestamp: Long = 0L

    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()

    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            ball = Ball(
                backgroundWidth = fieldWidth,
                backgroundHeight = fieldHeight,
                ballSize = ballSizePx
            )

            ball?.let { b ->
                _ballPosition.value = Offset(b.posX, b.posY)
            }

            lastTimestamp = 0L
        }
    }

    fun onSensorDataChanged(event: SensorEvent) {
        val currentBall = ball ?: return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            if (lastTimestamp != 0L) {

                val NS2S = 1.0f / 1_000_000_000.0f
                val dT = (event.timestamp - lastTimestamp) * NS2S

                val speedScale = 100f

                // Tilting right -> ball moves right
                // Tilting forward/down -> ball moves down
                val xAcc = -event.values[0] * speedScale
                val yAcc = event.values[1] * speedScale

                currentBall.updatePositionAndVelocity(xAcc, yAcc, dT)
                currentBall.checkBoundaries()

                _ballPosition.update { Offset(currentBall.posX, currentBall.posY) }
            }

            lastTimestamp = event.timestamp
        }
    }

    fun reset() {
        ball?.reset()

        ball?.let { b ->
            _ballPosition.value = Offset(b.posX, b.posY)
        }

        lastTimestamp = 0L
    }
}
