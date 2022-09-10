package com.example.ewestmembers.SideActivites.Messeges.Chatting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.ewestmembers.R;
import com.example.ewestmembers.SideActivites.Messeges.Contact.MessageContact;

import java.util.List;
import java.util.Objects;

public class MessagesAdapter extends ArrayAdapter<Messages> {
    Context context;
    String photoUrl;
    private View.OnClickListener defaultPlayVoiceClickListener;
    private View.OnClickListener defaultDownloadFileClickListener;
    private View.OnClickListener defaultViewImageClickListener;
    private View.OnLongClickListener defaultCopyMessageClickListener;
    boolean isPlaying = false;

    public MessagesAdapter(Context context, int resource, List<Messages> objects, String photoUrl) {
        super(context, resource);
        this.context = context;
        this.photoUrl = photoUrl;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertsView, @NonNull ViewGroup parent) {
        View sender = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.messages_chating_sender_layout, parent, false);
        View receiver = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.messages_chating_reciver_layout, parent, false);

        View CurrentView = null;
        Messages item = getItem(position);
        boolean isSender = Objects.equals(item.getSender(), "Ahmed Saleh");

        ViewHolder viewHolder;
        if (CurrentView == null) {
            if (isSender) {
                CurrentView = sender;
            } else {
                CurrentView = receiver;
            }
            viewHolder = new ViewHolder();

            viewHolder.messageImage = CurrentView.findViewById(R.id.message_image);
            viewHolder.messageSender = CurrentView.findViewById(R.id.message_sender);
            viewHolder.togglePlaying = CurrentView.findViewById(R.id.toggle_voice);
            viewHolder.voiceContainer = CurrentView.findViewById(R.id.voice_container);
            viewHolder.fileContainer = CurrentView.findViewById(R.id.file_container);
            viewHolder.fileDownload = CurrentView.findViewById(R.id.file_download);
            viewHolder.voicePlayer = CurrentView.findViewById(R.id.voice_seek);

            viewHolder.message = CurrentView.findViewById(R.id.message_text);
            viewHolder.dateTime = CurrentView.findViewById(R.id.message_time);
            CurrentView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) CurrentView.getTag();
        }
        viewHolder.messageImage.setImageResource(R.color.colorPrimary);
        boolean isPhoto = photoUrl != null;
        if (isPhoto) {
            Glide.with(viewHolder.messageSender.getContext())
                    .load(photoUrl)
                    .into(viewHolder.messageSender);
        }

        viewHolder.voiceContainer.setVisibility(View.GONE);
        viewHolder.fileContainer.setVisibility(View.GONE);

        switch (item.getType()) {
            case "message":
                switch (item.getStatue()) {
                    case "send":
                        viewHolder.messageSender.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                        break;
                    case "received":
                        viewHolder.messageSender.setImageResource(R.drawable.ic_baseline_check_circle_24);
                        break;
                    case "seen":
                        Glide.with(viewHolder.messageSender.getContext())
                                .load(photoUrl)
                                .into(viewHolder.messageSender);
                        break;

                }
                break;
            case "voice":
                viewHolder.voiceContainer.setVisibility(View.VISIBLE);
                viewHolder.togglePlaying.setImageResource(item.isPlaying() ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_baseline_play_arrow_24);
                if (isSender) {
                    switch (item.getStatue()) {
                        case "send":
                            viewHolder.messageSender.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                            break;
                        case "received":
                            viewHolder.messageSender.setImageResource(R.drawable.ic_baseline_check_circle_24);
                            break;
                        case "seen":
                            Glide.with(viewHolder.messageSender.getContext())
                                    .load(photoUrl)
                                    .into(viewHolder.messageSender);
                            break;

                    }
                }
                break;
            case "image":
                Glide.with(viewHolder.messageImage.getContext())
                        .load(item.getPhotoUrl())
                        .into(viewHolder.messageImage);
                break;
            case "file":
                viewHolder.fileContainer.setVisibility(View.VISIBLE);
                break;

        }
        viewHolder.message.setText(item.getMessage());

        String dating = item.getDate() + " - " + item.getTime();
        viewHolder.dateTime.setText(dating);


        if (item.getPlayVoiceClickListener() != null) {
            viewHolder.togglePlaying.setOnClickListener(item.getPlayVoiceClickListener());
        } else {
            viewHolder.togglePlaying.setOnClickListener(defaultPlayVoiceClickListener);
        }

        if (item.getCopyMessageClickListener() != null) {
            viewHolder.message.setOnLongClickListener(item.getCopyMessageClickListener());
        } else {
            viewHolder.message.setOnLongClickListener(defaultCopyMessageClickListener);
        }
        if (item.getViewImageClickListener() != null) {
            viewHolder.messageImage.setOnClickListener(item.getViewImageClickListener());
        } else {
            viewHolder.messageImage.setOnClickListener(defaultViewImageClickListener);
        }
        if (item.getDownloadFileClickListener() != null) {
            viewHolder.fileDownload.setOnClickListener(item.getDownloadFileClickListener());
        } else {
            viewHolder.fileDownload.setOnClickListener(defaultDownloadFileClickListener);
        }
        return CurrentView;
    }

    private static class ViewHolder {
        ImageView messageImage;

        ImageView messageSender;

        ImageView togglePlaying;
        LinearLayout voiceContainer;
        SeekBar voicePlayer;

        ImageView fileDownload;
        LinearLayout fileContainer;

        TextView message;
        TextView dateTime;
    }
}
