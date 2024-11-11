package lab03.eim.systems.cs.pub.ro.eim_colocviu

import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Date
import kotlin.math.sqrt

class ProcessingThread(private var context : Context, private var firstNumber: Int,
                       private var secondNumber: Int) : Thread() {
    private var arithmeticMean = 0.0
    private var geometricMean = 0.0
    private var isRunning = true

    override fun run() {
        Log.d("Processing_Thread", "Thread has started! PID: " + android.os.Process.myPid() + " TID: " + android.os.Process.myTid())
        Log.d("Processing_Thread", "First number: $firstNumber")
        Log.d("Processing_Thread", "Second number: $secondNumber")

        while (isRunning) {
            try {
                arithmeticMean = (firstNumber + secondNumber) / 2.0
                geometricMean = sqrt((firstNumber * secondNumber).toDouble())

                var intent = Intent().apply {
                    action = Constants.ACTION_STRING
                    putExtra(Constants.ARITHMETIC_MEAN, arithmeticMean.toString())
                    putExtra(Constants.GEOMETRIC_MEAN, geometricMean.toString())
                    putExtra(Constants.CURRENT_DATE, Date().toString())
                }
                context.sendBroadcast(intent)

                sleep(10000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        Log.d("Processing_Thread", "Thread has stopped!");
    }

    fun stopThread() {
        isRunning = false
    }
}