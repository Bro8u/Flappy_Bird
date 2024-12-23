package com.example.fluppybirds

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log


class Sprite(
    private var x: Double,
    private var y: Double,
    private var velocityX: Double,
    private var velocityY: Double,
    private var frames: ArrayList<Rect>,
    private var bitmap: Bitmap?
) {
    private var frameWidth = 0
    private var frameHeight = 0
    private var currentFrame = 0
    private var frameTime = 200.0
    private var timeForCurrentFrame = 0.0
    private var padding = 0

    init {
        frameWidth = frames.get(0).width()
        frameHeight = frames.get(0).height()
    }

    fun addFrame(frame: Rect?) {
        frames!!.add(frame!!)
    }

    fun update(ms: Int) {
        timeForCurrentFrame += ms
        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames!!.size
            timeForCurrentFrame = timeForCurrentFrame - frameTime
        }
        x += velocityX * ms / 1000.0
        y += velocityY * ms / 1000.0
    }
    fun getBoundingBoxRect(): Rect? {
        return Rect(
            x.toInt() + padding,
            y.toInt() + padding,
            (x + frameWidth - 2 * padding).toInt(),
            (y + frameHeight - 2 * padding).toInt()
        )
    }
    fun draw(canvas: Canvas) {
        val p = Paint()

        val destination =
            Rect(x.toInt(), y.toInt(), (x + frameWidth).toInt(), (y + frameHeight).toInt())
        canvas.drawBitmap(bitmap!!, frames!![currentFrame], destination, p)
//        val debugPaint = Paint().apply {
//            style = Paint.Style.STROKE
//            color = Color.RED
//            strokeWidth = 2f
//        }
//        getBoundingBoxRect()?.let { canvas.drawRect(it, debugPaint) }
    }



    fun intersect(s: Sprite): Boolean {
        return getBoundingBoxRect()!!.intersect(s.getBoundingBoxRect()!!)
    }

    fun getX(): Double {
        return x
    }

    fun setX(x: Double) {
        this.x = x
    }

    fun getY(): Double {
        return y
    }

    fun setY(y: Double) {
        this.y = y
    }

    fun getFrameWidth(): Int {
        return frameWidth
    }

    fun setFrameWidth(frameWidth: Int) {
        this.frameWidth = frameWidth
    }

    fun getFrameHeight(): Int {
        return frameHeight
    }

    fun setFrameHeight(frameHeight: Int) {
        this.frameHeight = frameHeight
    }

    fun getVx(): Double {
        return velocityX
    }

    fun setVx(velocityX: Double) {
        this.velocityX = velocityX
    }

    fun getVy(): Double {
        return velocityY
    }

    fun setVy(velocityY: Double) {
        this.velocityY = velocityY
    }

    fun setframeWidth(width: Int) {
        this.frameWidth = width
    }

    fun setframeHeight(height: Int) {
        this.frameHeight = height
    }

    fun set(velocityY: Double) {
        this.velocityY = velocityY
    }

    fun getCurrentFrame(): Int {
        return currentFrame
    }

    fun setCurrentFrame(currentFrame: Int) {
        this.currentFrame = currentFrame % frames!!.size
    }

    fun getFrameTime(): Double {
        return frameTime
    }

    fun setFrameTime(frameTime: Double) {
        this.frameTime = Math.abs(frameTime)
    }

    fun getTimeForCurrentFrame(): Double {
        return timeForCurrentFrame
    }

    fun setTimeForCurrentFrame(timeForCurrentFrame: Double) {
        this.timeForCurrentFrame = Math.abs(timeForCurrentFrame)
    }

    fun getFramesCount(): Int {
        return frames!!.size
    }
}