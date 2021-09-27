package ca.greenops.it.smartbuilding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ca.greenops.it.smartbuilding.R;
import ca.greenops.it.smartbuilding.Room;
import ca.greenops.it.smartbuilding.SingleRoomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Smart Building
 * https://github.com/MofifoluwaLekeAkinrowo3651/SmartBuilding
 * Created on 25-SEP-2021.
 * Created by : greenOps Team - Mofifoluwa, Andrew & Bibek
 */
public class RoomDetailsActivity extends AppCompatActivity {
    private List<Room> roomList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SingleRoomAdapter mAdapter;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new SingleRoomAdapter(roomList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareRoomData();
    }

    private void prepareRoomData() {
        Room room = new Room("1", "Lights");
        roomList.add(room);
        room = new Room("1", "Air Conditioner");
        roomList.add(room);
        room = new Room("2", "Locks");
        roomList.add(room);

        mAdapter.notifyDataSetChanged();
    }

    public void onBackClicked(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
