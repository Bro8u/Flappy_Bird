import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import com.example.fluppybirds.R
import com.example.fluppybirds.Sprite


class GameView(context: Context?) : View(context) {
    
    private var viewWidth = 0
    private var viewHeight = 0
    private var points = 0
    private var playerBird: Sprite? = null
    private var enemyBird: Sprite? = null
    private var coin: Sprite? = null
    private var timeLiveCoin = 4000.0 // ms or sec
    private var curTime = 0.0
    private val timerInterval = 30
    private val gameTimer = Timer()
    init {
        gameTimer.start()
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

        val spritesShitt = BitmapFactory.decodeResource(resources, R.drawable.sheet)
        val spritesShit = Bitmap.createScaledBitmap(
            spritesShitt,
            viewWidth,
            (spritesShitt.height * (viewWidth)) / spritesShitt.width,
            true
        )
        val columns = 4
        val rows = 4
        val sheetWidth = spritesShit.width
        val sheetHeight = spritesShit.height
        val spriteWidth = sheetWidth / columns
        val spriteHeight = sheetHeight / rows
        var spriteRects = arrayListOf<Rect>()
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val left = col * spriteWidth
                val top = row * spriteHeight
                val right = left + spriteWidth
                val bottom = top + spriteHeight
                val rect = Rect(left, top, right, bottom)
                spriteRects.add(rect)
            }
        }
        playerBird = Sprite(
            10.0, 10.0, 0.0, 590.0, spriteRects, spritesShit
        )

        val EnemyspritesShitt = BitmapFactory.decodeResource(resources, R.drawable.enemysheet)
        val EnemyspritesShit = Bitmap.createScaledBitmap(
            EnemyspritesShitt,
            viewWidth,
            (EnemyspritesShitt.height * (viewWidth)) / EnemyspritesShitt.width,
            true
        )

        val EnemysheetWidth = EnemyspritesShit.width
        val EnemysheetHeight = EnemyspritesShit.height
        val EnemyspriteWidth = EnemysheetWidth / columns
        val EnemyspriteHeight = EnemysheetHeight / rows
        var EnemyspriteRects = arrayListOf<Rect>()
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val left = col * EnemyspriteWidth
                val top = row * EnemyspriteHeight
                val right = left + EnemyspriteWidth
                val bottom = top + EnemyspriteHeight
                val rect = Rect(left, top, right, bottom)
                EnemyspriteRects.add(rect)
            }
        }
        enemyBird = Sprite(
            250.0, 250.0, -500.0, 0.0, EnemyspriteRects, EnemyspritesShit
        )

        val coinsBitmap = BitmapFactory.decodeResource(resources, R.drawable.last)
        val coinSpritesShit = Bitmap.createScaledBitmap(
            coinsBitmap,
            viewWidth,
            (coinsBitmap.height * (viewWidth)) / coinsBitmap.width,
            true
        )


        val coinSheetWidth = coinSpritesShit.width
        val coinSheetHeight = coinSpritesShit.height
        val coinSpriteWidth = coinSheetWidth / 6
        val coinSpriteHeight = coinSheetHeight / 2.5
        var coinSpriteRects = arrayListOf<Rect>()
        for (row in 0 until 1) {
            for (col in 0 until 6) {
                val left = col * coinSpriteWidth
                val top = row * coinSpriteHeight
                val right = left + coinSpriteWidth
                val bottom = top + coinSpriteHeight
                val rect = Rect(left, top.toInt(), right, bottom.toInt())
                coinSpriteRects.add(rect)
            }
        }
        coin = Sprite(
            25.0, 500.0, 0.0, 0.0, coinSpriteRects, coinSpritesShit
        )
        coin!!.setFrameTime(10.0);
    }
    override fun onDraw(canvas: Canvas) {
        if (canvas == null) return
        super.onDraw(canvas)
        canvas.drawARGB(250, 127, 199, 255)
        playerBird!!.draw(canvas);
        enemyBird!!.draw(canvas);
        coin!!.draw(canvas);
        val paint = Paint().apply {
            isAntiAlias = true
            textSize = 55.0f
            color = Color.BLACK
        }
        canvas.drawText(points.toString(), (viewWidth - 100).toFloat(), 70f, paint)
    }
    protected fun update() {
        playerBird?.let { bird ->
            bird.update(timerInterval)
            if (bird.getY() + bird.getFrameHeight() > viewHeight) {
                bird.setY((viewHeight - bird.getFrameHeight()).toDouble())
                bird.setVy(-bird.getVy())
                points--
            } else if (bird.getY() < 0) {
                bird.setY(0.0)
                bird.setVy(-bird.getVy())
                points--
            } else {

            }
        }
        coin?.let { coin ->
            coin.update(timerInterval)
        }
        curTime += timerInterval
        if (curTime > timeLiveCoin) {
            teleportCoin();
            curTime = 0.0;
        }
        if (enemyBird!!.getX() < - enemyBird!!.getFrameWidth()) {
            teleportEnemy ();
            points += 10;
        }
        if (enemyBird!!.intersect(playerBird!!)) {
            teleportEnemy ();
            points -= 40;
        }
        if (playerBird!!.intersect(coin!!)) {
            teleportCoin ();
            points += 40;
        }
        enemyBird?.let { enemy ->
            enemy.update(timerInterval)
        }

        invalidate()
    }
    inner class Timer : CountDownTimer(Int.MAX_VALUE.toLong(), timerInterval.toLong()) {
        override fun onTick(millisUntilFinished: Long) {
            update()
        }
        override fun onFinish() {}
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventAction = event.action
        if (eventAction == MotionEvent.ACTION_DOWN) {
            if (event.y < playerBird!!.getBoundingBoxRect()!!.top) {
                playerBird!!.setVy(-500.0)
                points--
            } else if (event.y > playerBird!!.getBoundingBoxRect()!!.bottom) {
                playerBird!!.setVy(500.0)
                points--
            }
        }
        return true
    }

    private fun teleportEnemy() {
        enemyBird!!.setX(viewWidth + Math.random())
        enemyBird!!.setY(Math.random() * (viewHeight - enemyBird!!.getFrameHeight()))
    }
    private fun teleportCoin() {
        // x, y
        coin!!.setY(Math.random() * (viewHeight - coin!!.getFrameHeight()))
    }
}

