package com.example.ewestmembers.SideActivites.Messeges.Chatting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.ewestmembers.MainActivity;
import com.example.ewestmembers.R;
import com.example.ewestmembers.SideActivites.Messeges.MessagesFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class ChattingFragment extends Fragment {
    String USERNAME;
    FrameLayout frameLayout;
    MessagesAdapter adapter;
    ListView list;
    String photoUrl;

    boolean isRecording = false;
    MediaRecorder mediaRecorder;
    String recordFile;
    String recordPath;

    MediaPlayer mediaPlayer;

    Chronometer recordTimer;
    GifImageView animation;
    RelativeLayout recordContainer;

    public ChattingFragment(String USERNAME, FrameLayout frameLayout, String photoUrl) {

        super();
        this.USERNAME = USERNAME;
        this.frameLayout = frameLayout;
        this.photoUrl = photoUrl;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.messages_chating_layout, container, false);
        ImageView back = root.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessagesFragment(frameLayout)).commit();
            }
        });
        // setup list and adapter with dummyData
        ArrayList<Messages> dummyData = new ArrayList<>();
        String sender = "Ahmed Saleh";
        dummyData.add(new Messages(1, sender, "2020-09-10", "08:20 PM", "message", "test sender", "", "", "", "", "seen"));
        dummyData.add(new Messages(2, USERNAME, "2020-09-10", "08:20 PM", "image", "Beauty!", "data:actionMessage/jpeg;base64,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", "", "", "", "seen"));
        dummyData.add(new Messages(3, USERNAME, "2020-09-10", "08:20 PM", "message", "test receiving", "", "", "", "", "seen"));
        dummyData.add(new Messages(4, sender, "2020-09-10", "08:20 PM", "image", "Awesome!", "https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1539117332142-bdb5d548d1ef", "", "", "", "seen"));
        dummyData.add(new Messages(4, USERNAME, "2020-09-10", "08:20 PM", "image", "Awesome!", "https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1539117332142-bdb5d548d1ef", "", "", "", "seen"));
        dummyData.add(new Messages(5, USERNAME, "2020-09-10", "08:20 PM", "message", "test receiving", "", "", "", "", "seen"));
        dummyData.add(new Messages(6, sender, "2020-09-10", "08:20 PM", "message", "test send", "", "", "", "", "send"));
        dummyData.add(new Messages(7, sender, "2020-09-10", "08:20 PM", "message", "test received", "", "", "", "", "received"));
        dummyData.add(new Messages(7, sender, "2020-09-10", "08:20 PM", "message", "test received", "", "", "", "", "seen"));
        dummyData.add(new Messages(8, sender, "2020-09-10", "08:20 PM", "message", "test seen", "", "", "", "", "seen"));
        dummyData.add(new Messages(8, sender, "2020-09-10", "08:20 PM", "voice", "", "", "", "", "", "seen"));
        dummyData.add(new Messages(8, USERNAME, "2020-09-10", "08:20 PM", "voice", "", "", "", "", "", "seen"));
        dummyData.add(new Messages(8, sender, "2020-09-10", "08:20 PM", "file", "", "", "", "", "", "seen"));
        dummyData.add(new Messages(8, USERNAME, "2020-09-10", "08:20 PM", "file", "", "", "", "data.excl", "80KB", "seen"));
        adapter = new MessagesAdapter(getContext(), 0, dummyData, photoUrl);

        adapter.addAll(dummyData);
        list = root.findViewById(R.id.chatting_list);
        list.setAdapter(adapter);

        //handle onBackPress()
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessagesFragment(frameLayout)).commit();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //handle message or voice
        ImageView actionMessage = root.findViewById(R.id.message_send);

        TextInputLayout message = root.findViewById(R.id.message);
        message.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (message.getEditText().getText().toString().isEmpty() || message.getEditText().getText() == null) {
                    actionMessage.setImageResource(R.drawable.ic_baseline_mic_24);
                } else {
                    actionMessage.setImageResource(R.drawable.ic_baseline_send_24_white);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //handel action of send message and voice
        actionMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getEditText().getText().toString().isEmpty() || message.getEditText().getText() == null) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

                        if (isRecording) {
                            stopRecording();
                            isRecording = false;
                        } else {
                            startRecording();
                            isRecording = true;
                        }
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 22);
                    }
                } else {
                    Toast.makeText(getContext(), "send", Toast.LENGTH_SHORT).show();
                }
            }

        });
        //setup recording Animations

        recordContainer = root.findViewById(R.id.record_container);
        recordContainer.setVisibility(View.GONE);

        ImageView stopRecord = root.findViewById(R.id.stop_record);
        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endRecording();
            }
        });

        animation = root.findViewById(R.id.recordAnimation);
        recordTimer = root.findViewById(R.id.recordTimer);

        setUpAdapter();
        TextView name = root.findViewById(R.id.username);
        name.setText(USERNAME);
        return root;
    }

    private void setUpAdapter() {
        for (int i = 0; i < adapter.getCount(); i++) {
            final int postion = i;
            if (adapter.getItem(postion).getType().equals("voice")) {
                adapter.getItem(postion).setPlayVoiceClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (adapter.getItem(postion).isPlaying()) {
                            adapter.getItem(postion).setPlaying(false);
                            if(mediaPlayer.isPlaying()) {mediaPlayer.stop();}
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.getItem(postion).setPlaying(true);
                            mediaPlayer=new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(adapter.getItem(postion).getVoiceUrl());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        adapter.getItem(postion).setPlaying(false);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }

        }


    }



    private void startRecording() {
        recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        recordFile = "Recording_" + formatter.format(new Date()) + ".3gp";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordContainer.setVisibility(View.VISIBLE);
        recordTimer.setBase(SystemClock.elapsedRealtime());
        recordTimer.start();
        mediaRecorder.start();
    }

    private void stopRecording() {
        recordContainer.setVisibility(View.GONE);
        mediaRecorder.stop();
        recordTimer.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        adapter.add(new Messages(9, USERNAME, "2020-09-10", "08:20 PM", "voice", "", "", recordPath + "/" + recordFile, "", "", "seen"));
        setUpAdapter();
        adapter.notifyDataSetChanged();
    }

    private void endRecording() {
        recordContainer.setVisibility(View.GONE);
        mediaRecorder.stop();
        recordTimer.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;

        isRecording = false;
    }
}
