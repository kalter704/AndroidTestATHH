package ru.vas.lan.test.androidtestathh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.vas.lan.test.androidtestathh.api.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>{

    private Context mContext;
    private List<User> mUserList;

    public UserAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.bind(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_id)
        TextView mId;

        @BindView(R.id.tv_username)
        TextView mUsername;

        @BindView(R.id.tv_name)
        TextView mName;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(User user) {
            mId.setText(String.valueOf(user.getId()));
            mUsername.setText(user.getUsername());
            mName.setText(user.getName());
        }
    }
}
