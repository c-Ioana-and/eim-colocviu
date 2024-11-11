package lab03.eim.systems.cs.pub.ro.eim_colocviu

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class ColocviuService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SERVICE_TAG", "Service has been initiated")
        val firstNumber = intent?.getIntExtra("INPUT1", 0)
        val secondNumber = intent?.getIntExtra("INPUT2", 0)

        Log.d("SERVICE_TAG", "First number: $firstNumber")
        Log.d("SERVICE_TAG", "Second number: $secondNumber")

        val processingThread = ProcessingThread(this, firstNumber!!, secondNumber!!)
        processingThread.start()

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d("SERVICE_TAG", "Service has stopped")
        super.onDestroy()
    }
}