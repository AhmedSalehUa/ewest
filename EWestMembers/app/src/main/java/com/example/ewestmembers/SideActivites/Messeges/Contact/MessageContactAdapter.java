package com.example.ewestmembers.SideActivites.Messeges.Contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.ewestmembers.R;

import java.util.List;

public class MessageContactAdapter extends ArrayAdapter<MessageContact> {

    Context context;

    public MessageContactAdapter(Context context, int resource, List<MessageContact> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.messages_contact_layout, parent, false);

        MessageContact messageContact = getItem(position);

        ImageView avatar = (ImageView) convertView.findViewById(R.id.messageContactImage);
        boolean isPhoto = messageContact.getPhotoUrl() != null;
        if (isPhoto) {
            Glide.with(avatar.getContext())
                    .load(messageContact.getPhotoUrl())
                    .into(avatar);
        } else {
            avatar.setImageResource(R.color.colorPrimary);
        }

        TextView user = (TextView) convertView.findViewById(R.id.messageContactUsername);
        user.setText(messageContact.getName());

        TextView message = (TextView) convertView.findViewById(R.id.messageContactMessage);
        message.setText(messageContact.getLatestMessage());

        TextView time = (TextView) convertView.findViewById(R.id.messageContactTime);
        time.setText(messageContact.getTime());

        TextView date = (TextView) convertView.findViewById(R.id.messageContactDate);
        date.setText(messageContact.getDate());

        return convertView;
    }
}
