package com.cs407.lab09

class Ball(
    private val backgroundWidth: Float,
    private val backgroundHeight: Float,
    private val ballSize: Float
) {
    var posX = 0f
    var posY = 0f
    var velocityX = 0f
    var velocityY = 0f
    private var accX = 0f   // a0 on x
    private var accY = 0f   // a0 on y

    private var isFirstUpdate = true

    init {
        reset()
    }

    /**
     * Uses Equations (1) and (2) from the handout:
     *
     * v1 = v0 + 1/2 (a1 + a0) Δt
     * l  = v0 Δt + (Δt^2 / 6) (3 a0 + a1)
     */
    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        // First call: just record initial acceleration and exit
        if (isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }

        // Previous acceleration a0
        val a0x = accX
        val a0y = accY
        // New acceleration a1
        val a1x = xAcc
        val a1y = yAcc

        // Save old velocity v0 before we update it
        val v0x = velocityX
        val v0y = velocityY

        // Distance traveled during this time step (Equation 2)
        val dt2 = dT * dT
        val dx = v0x * dT + (dt2 / 6f) * (3f * a0x + a1x)
        val dy = v0y * dT + (dt2 / 6f) * (3f * a0y + a1y)

        posX += dx
        posY += dy

        // New velocity (Equation 1)
        velocityX = v0x + 0.5f * (a1x + a0x) * dT
        velocityY = v0y + 0.5f * (a1y + a0y) * dT

        // Store a1 as the previous acceleration for next time
        accX = a1x
        accY = a1y
    }

    /**
     * Keep the ball inside the rectangle [0, backgroundWidth - ballSize] x
     * [0, backgroundHeight - ballSize].
     *
     * When it hits a vertical wall, X velocity/acceleration go to 0.
     * When it hits a horizontal wall, Y velocity/acceleration go to 0.
     */
    fun checkBoundaries() {
        // Left wall
        if (posX < 0f) {
            posX = 0f
            velocityX = 0f
            accX = 0f
        }

        // Right wall
        val maxX = backgroundWidth - ballSize
        if (posX > maxX) {
            posX = maxX
            velocityX = 0f
            accX = 0f
        }

        // Top wall
        if (posY < 0f) {
            posY = 0f
            velocityY = 0f
            accY = 0f
        }

        // Bottom wall
        val maxY = backgroundHeight - ballSize
        if (posY > maxY) {
            posY = maxY
            velocityY = 0f
            accY = 0f
        }
    }

    /**
     * Reset to center with zero velocity and acceleration.
     */
    fun reset() {
        posX = (backgroundWidth - ballSize) / 2f
        posY = (backgroundHeight - ballSize) / 2f

        velocityX = 0f
        velocityY = 0f
        accX = 0f
        accY = 0f

        isFirstUpdate = true
    }
}
