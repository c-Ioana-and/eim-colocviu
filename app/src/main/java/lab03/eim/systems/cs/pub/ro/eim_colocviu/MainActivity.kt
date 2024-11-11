package lab03.eim.systems.cs.pub.ro.eim_colocviu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi

class MainActivity : ComponentActivity() {
    private lateinit var button1 : Button
    private lateinit var editText1 : EditText
    private lateinit var button2 : Button
    private lateinit var editText2 : EditText
    private lateinit var navigateToSecondActivityButton: Button
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private lateinit var intentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.applayout)

        editText1 = findViewById(R.id.editText1)
        editText2 = findViewById(R.id.editText2)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.LEFT_COUNT)) {
                editText1.setText(savedInstanceState.getString(Constants.LEFT_COUNT))
            } else {
                editText1.setText("0")
            }
            if (savedInstanceState.containsKey(Constants.RIGHT_COUNT)) {
                editText2.setText(savedInstanceState.getString(Constants.RIGHT_COUNT))
            } else {
                editText2.setText("0")
            }
        }
        else {
            editText1.setText("0")
            editText2.setText("0")
        }

        button1 = findViewById(R.id.button1)
        button1.setOnClickListener {
            editText1.setText((editText1.text.toString().toInt() + 1).toString())
            startServiceIfCond(editText1, editText2)
        }
        button2 = findViewById(R.id.button2)
        button2.setOnClickListener {
            editText2.setText((editText2.text.toString().toInt() + 1).toString())
            startServiceIfCond(editText1, editText2)
        }





        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("SecondActivity", "The activity returned with result OK")
            }
        }

        navigateToSecondActivityButton = findViewById(R.id.navigateToSecondaryActivityButton)
        navigateToSecondActivityButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra(Constants.NR_OF_CLICKS, editText1.text.toString().toInt() + editText2.text.toString().toInt())
            }

            startForResult.launch(intent)
        }

        intentFilter = IntentFilter()
        for (term in Constants.allTerms) {
            intentFilter.addAction(term)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.LEFT_COUNT, editText1.text.toString())
        outState.putString(Constants.RIGHT_COUNT, editText2.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(Constants.LEFT_COUNT)) {
            editText1.setText(savedInstanceState.getString(Constants.LEFT_COUNT))
        } else {
            editText1.setText("0")
        }
        if (savedInstanceState.containsKey(Constants.RIGHT_COUNT)) {
            editText2.setText(savedInstanceState.getString(Constants.RIGHT_COUNT))
        } else {
            editText2.setText("0")
        }
    }

    private fun startServiceIfCond(editText1: EditText, editText2: EditText) {
        val leftCount = editText1.text.toString().toInt()
        val rightCount = editText2.text.toString().toInt()

        if (leftCount + rightCount > 4) {
            Log.d("ServiceStart", "Starting service")

            val intent = Intent(applicationContext, ColocviuService::class.java).apply {
                putExtra("INPUT1", leftCount)
                putExtra("INPUT2", rightCount)
            }

            startService(intent)
        }
    }

    private lateinit var BroadcastRecv: MessageBroadcastReceiver

    class MessageBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("BroadcastReceiver", intent?.getStringExtra(Constants.ARITHMETIC_MEAN).toString())
            Log.d("BroadcastReceiver", intent?.getStringExtra(Constants.GEOMETRIC_MEAN).toString())
            Log.d("BroadcastReceiver", intent?.getStringExtra(Constants.CURRENT_DATE).toString())
        }
    }

    override fun onStart() {
        super.onStart()
        BroadcastRecv = MessageBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(BroadcastRecv)
    }

    override fun onDestroy() {
        val intent = Intent(applicationContext, ColocviuService::class.java)
        stopService(intent)
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        registerReceiver(BroadcastRecv, IntentFilter(Constants.ACTION_STRING), Context.RECEIVER_EXPORTED)
    }

    override fun onPause() {
        unregisterReceiver(BroadcastRecv)
        super.onPause()
    }

}