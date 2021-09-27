package ca.greenops.it.smartbuilding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class EventsActivity extends AppCompatActivity {

    private String roomTemp, humidity;
    private String fireStat;
    private String doorStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
