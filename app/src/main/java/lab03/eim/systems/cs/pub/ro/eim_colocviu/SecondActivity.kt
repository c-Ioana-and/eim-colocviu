package lab03.eim.systems.cs.pub.ro.eim_colocviu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class SecondActivity : ComponentActivity() {

    private lateinit var nrOfClicksView : TextView
    private lateinit var okButton : Button
    private lateinit var cancelButton2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app2layout)

        nrOfClicksView = findViewById(R.id.nr_of_clicks)

        // get intent
        val intent = intent
        val nrOfClicks = intent.getIntExtra(Constants.NR_OF_CLICKS, 0)
        nrOfClicksView.text = nrOfClicks.toString()

        okButton = findViewById(R.id.button1)
        okButton.setOnClickListener {
            setResult(RESULT_OK, Intent().putExtra(Constants.NR_OF_CLICKS, nrOfClicks))
            finish()
        }

        cancelButton2 = findViewById(R.id.button2)
        cancelButton2.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
