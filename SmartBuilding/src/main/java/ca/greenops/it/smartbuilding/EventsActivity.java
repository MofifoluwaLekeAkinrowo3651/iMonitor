package ca.greenops.it.smartbuilding;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class EventsActivity extends AppCompatActivity {

    private String roomTemp, humidity;
    private String fireStat;
    private String doorStat;
    private TextView eventView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        eventView = (TextView) findViewById(R.id.eventView);

        eventView.setText(printEvents());

    }

    public String printEvents()
    {
        String line1, line2,line3;
         line1 = getString(R.string.temp)+roomTemp+"\t"+getString(R.string.humid)+humidity;

        line2 = getString(R.string.doorClose);

        if (fireStat.equals(getString(R.string.on)))
        {
            line3 = getString(R.string.fireAlarmOn);
        }
        else
        {
            line3 = getString(R.string.fireAlarmOff);
        }

                return line1+"\n"+line2+"\n"+line3;

    }

    private void setRoomTemp(String roomTemp)
    {
        this.roomTemp = roomTemp;
    }

    private void setHumidity(String humidity)
    {
        this.humidity = humidity;
    }

    private void setFireStat(String fireStat)
    {
        this.fireStat = fireStat;
    }

    private void setDoorStat(String doorStat)
    {
        this.doorStat = doorStat;
    }
}
