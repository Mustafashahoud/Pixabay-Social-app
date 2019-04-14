package com.mustafa.sar.instagramthesis.utilities;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.models.Comment;
import com.mustafa.sar.instagramthesis.models.UserProfileAccountSetting;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "CommentListAdapter";
    public List<Comment> mCommentList;
    private LayoutInflater mInflater;
    private int layoutResource;
    private Context mContext;

    public CommentRecyclerViewAdapter(@NonNull Context context, @LayoutRes int resource,
                                      @NonNull List<Comment> mDataModelList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        layoutResource = resource;
        this.mCommentList = mDataModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_single_comment, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //set the comment
        holder.comment.setText(mCommentList.get(position).getComment());

        //set the timestamp difference
        String timestampDifference = holder.getTimestampDifference(mCommentList.get(position));
        if (!timestampDifference.equals("0")) {
            holder.timestamp.setText(timestampDifference + " d");
        } else {
            holder.timestamp.setText("today");
        }

        //set the username and profile image
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(mContext.getString(R.string.db_userprofileaccount))
                .orderByChild(mContext.getString(R.string.field_user_id))
                .equalTo(mCommentList.get(position).getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    holder.username.setText(
                            singleSnapshot.getValue(UserProfileAccountSetting.class).getUsername());

                    ImageLoader imageLoader = ImageLoader.getInstance();

                    imageLoader.displayImage(
                            singleSnapshot.getValue(UserProfileAccountSetting.class).getProfile_photo(),
                            holder.profileImage);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });

        try {
            if (position == 0) {
                holder.like.setVisibility(View.GONE);
                holder.likes.setVisibility(View.GONE);
                holder.reply.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "getView: NullPointerException: " + e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView comment, username, timestamp, reply, likes;
        CircleImageView profileImage;
        ImageView like;


        public MyViewHolder(View v) {
            super(v);
            comment = (TextView) v.findViewById(R.id.comment);
            username = (TextView) v.findViewById(R.id.comment_username);
            timestamp = (TextView) v.findViewById(R.id.comment_time_posted);
            reply = (TextView) v.findViewById(R.id.comment_reply);
            like = (ImageView) v.findViewById(R.id.comment_like);
            likes = (TextView) v.findViewById(R.id.comment_likes);
            profileImage = (CircleImageView) v.findViewById(R.id.comment_profile_image);
        }

        /**
         * Returns a string representing the number of days ago the post was made
         *
         * @return
         */
        public String getTimestampDifference(Comment comment) {
            Log.d(TAG, "getTimestampDifference: getting timestamp difference.");

            String difference = "";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("Europe/Budapest"));//google 'android list of timezones'
            Date today = c.getTime();
            sdf.format(today);
            Date timestamp;
            final String photoTimestamp = comment.getDate_created();
            try {
                Log.d(TAG, "getTimestampDifference: try to parse date");
                timestamp = sdf.parse(photoTimestamp);
                difference = String.valueOf(Math.round((today.getTime() - timestamp.getTime())/86400000));
            } catch (ParseException e) {
                Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage());
                difference = "0";
            }
            return difference;
        }
    }
}