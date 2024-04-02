package com.example.ewestmembers.ManiActivites.Attendance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ewestmembers.R;
import com.mut_jaeryo.circletimer.CircleTimer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceFragment extends Fragment {
    public AttendanceFragment() {
        super();
    }

    CircleTimer timer;
    LinearLayout leavingView;
    RelativeLayout attendView;
    RelativeLayout attendedDay;
    RelativeLayout holidayView;

    TextView todayText;
    TextView todayShiftTimes;
    TextView empAtt;
    TextView empAttLate;
    TextView empLeave;
    TextView empLeaveEarly;
    Button holidayBtn;
    Button earlyLeaveBtn;


    TextView holidayName;


    TextView viewLeftTime;

    String type = "attend";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.attendance_layout, container, false);

        attendView = root.findViewById(R.id.attendance_view);
        attendView.setVisibility(View.GONE);

        attendedDay = root.findViewById(R.id.attendanced);


        holidayView = root.findViewById(R.id.holiday);
        holidayView.setVisibility(View.GONE);

        leavingView = root.findViewById(R.id.leaving_view);
        leavingView.setVisibility(View.GONE);

        todayText = root.findViewById(R.id.today_date);
        todayShiftTimes = root.findViewById(R.id.shiftTime);
        empAtt = root.findViewById(R.id.attTime);
        empAttLate = root.findViewById(R.id.attLate);
        empLeave = root.findViewById(R.id.LevTime);
        empLeaveEarly = root.findViewById(R.id.LevEarly);

        holidayBtn = root.findViewById(R.id.askForHoliday);
        holidayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "leave";
                setActiveView();
            }
        });
        earlyLeaveBtn = root.findViewById(R.id.askForEarlyLeaving);
        earlyLeaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "holiday";
                setActiveView();
            }
        });

        holidayName = root.findViewById(R.id.holidayReason);

        viewLeftTime = root.findViewById(R.id.leftViewLeaveTime);
        timer = root.findViewById(R.id.timmer_to_leave);
        setActiveView();

        return root;
    }

    private void setActiveView() {
        attendView.setVisibility(View.GONE);
        holidayView.setVisibility(View.GONE);
        leavingView.setVisibility(View.GONE);
        switch (type) {
            case "attend":
                attendView.setVisibility(View.VISIBLE);
                break;
            case "holiday":
                holidayView.setVisibility(View.VISIBLE);
                break;
            case "leave":
                leavingView.setVisibility(View.VISIBLE);
                timer.setMaximumTime(60);
                timer.setInitPosition(60);
                timer.start();
                timer.setBaseTimerEndedListener(new CircleTimer.baseTimerEndedListener() {
                    @Override
                    public void OnEnded() {
                        viewLeftTime.setText(new StringBuilder().append("leave time : ").append(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).toString());
                    }
                });
                break;
        }
    }
}
