package ru.vas.lan.test.androidtestathh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.vas.lan.test.androidtestathh.api.ApiFactory;
import ru.vas.lan.test.androidtestathh.api.models.Comment;
import ru.vas.lan.test.androidtestathh.api.models.Photo;
import ru.vas.lan.test.androidtestathh.api.models.Post;
import ru.vas.lan.test.androidtestathh.api.models.Todo;
import ru.vas.lan.test.androidtestathh.api.models.User;

public class CardsFragment extends Fragment {

    private static final String TAG = "cards_fragment";

    public CardsFragment() {}

    public static CardsFragment newInstance() {
        return new CardsFragment();
    }


    @BindView(R.id.ll_post_response)
    LinearLayout mLLPostResponse;

    @BindView(R.id.pb_post)
    ProgressBar mPBPost;

    @BindView(R.id.tv_post_title)
    TextView mPostTitle;

    @BindView(R.id.tv_post_body)
    TextView mPostBody;

    @BindView(R.id.ed_post_id)
    EditText mEDPostID;

    @BindView(R.id.btn_request_post)
    Button mButtonRequestPost;


    @BindView(R.id.ll_comment_response)
    LinearLayout mLLCommentResponse;

    @BindView(R.id.pb_comment)
    ProgressBar mPBComment;

    @BindView(R.id.tv_comment_name)
    TextView mCommentName;

    @BindView(R.id.tv_comment_email)
    TextView mCommentEmail;

    @BindView(R.id.tv_comment_body)
    TextView mCommentBody;

    @BindView(R.id.ed_comment_id)
    EditText mEDCommentID;

    @BindView(R.id.btn_request_comment)
    Button mButtonRequestComment;


    @BindView(R.id.rv_user_list)
    RecyclerView mRVUserList;

    @BindView(R.id.pb_users)
    ProgressBar mPBUsers;

    @BindView(R.id.btn_request_users)
    Button mButtonRequestUsers;


    @BindView(R.id.iv_photo)
    ImageView mIVPhoto;

    @BindView(R.id.btn_request_photo)
    Button mButtonRequestPhoto;

    @BindView(R.id.pb_photo)
    ProgressBar mPBPhoto;


    @BindView(R.id.ll_todo_response)
    LinearLayout mLLToDoResponse;

    @BindView(R.id.pb_todo)
    ProgressBar mPBToDo;

    @BindView(R.id.tv_todo_id)
    TextView mToDoId;

    @BindView(R.id.tv_todo_title)
    TextView mToDoTitle;

    @BindView(R.id.tv_todo_completed)
    TextView mToDoCompleted;

    @BindView(R.id.btn_request_todo)
    Button mButtonRequestToDo;


    private CompositeDisposable mCompositeDisposable;

    private Post mPost = null;
    private Comment mComment = null;
    private List<User> mUserList = new ArrayList<>();
    private Photo mPhoto = null;
    private Todo mTodo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        ButterKnife.bind(this, view);

        mCompositeDisposable.add(
                RxTextView.textChanges(mEDPostID)
                        .map(charSequence -> !charSequence.toString().isEmpty())
                        .subscribe(enable -> mButtonRequestPost.setEnabled(enable))
        );

        mCompositeDisposable.add(
                RxTextView.textChanges(mEDCommentID)
                        .map(charSequence -> !charSequence.toString().isEmpty())
                        .subscribe(enable -> mButtonRequestComment.setEnabled(enable))
        );

        mRVUserList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRVUserList.setHasFixedSize(true);

        if (mPost != null) {
            showPost(mPost);
        }

        if (mComment != null) {
            showComment(mComment);
        }

        if (mUserList.size() == 5) {
            showUsers();
        } else {
            requestUsers();
        }

        if (mPhoto != null) {
            showPhoto(mPhoto);
        } else {
            requestPhoto();
        }

        if (mTodo != null) {
            showTodo(mTodo);
        } else {
            requestToDo();
        }

        return view;
    }

    @OnClick(R.id.btn_request_post)
    public void requestPost() {
        int id = Integer.valueOf(mEDPostID.getText().toString());
        if (id < 1) {
            id = 1;
            mEDPostID.setText(String.valueOf(id));
        } else if (id > 100) {
            id = 100;
            mEDPostID.setText(String.valueOf(id));
        }
        ApiFactory.getService()
                .getPost(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {
                    mLLPostResponse.setVisibility(View.GONE);
                    mPBPost.setVisibility(View.VISIBLE);
                })
                .doAfterTerminate(() -> mPBPost.setVisibility(View.GONE))
                .subscribe(
                        this::showPost,
                        Throwable::printStackTrace,
                        () -> Log.i(TAG, "Complete")
                );
    }

    private void showPost(Post post) {
        mPost = post;
        mPostTitle.setText(post.getTitle());
        mPostBody.setText(post.getBody());
        mLLPostResponse.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_request_comment)
    public void requestComment() {
        int id = Integer.valueOf(mEDCommentID.getText().toString());
        if (id < 1) {
            id = 1;
            mEDCommentID.setText(String.valueOf(id));
        } else if (id > 500) {
            id = 500;
            mEDCommentID.setText(String.valueOf(id));
        }
        ApiFactory.getService()
                .getComment(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {
                    mLLCommentResponse.setVisibility(View.GONE);
                    mPBComment.setVisibility(View.VISIBLE);
                })
                .doAfterTerminate(() -> mPBComment.setVisibility(View.GONE))
                .subscribe(
                        this::showComment,
                        Throwable::printStackTrace,
                        () -> Log.i(TAG, "Complete")
                );
    }

    private void showComment(Comment comment) {
        mComment = comment;
        mCommentName.setText(comment.getName());
        mCommentEmail.setText(comment.getEmail());
        mCommentBody.setText(comment.getBody());
        mLLCommentResponse.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_request_users)
    public void requestUsers() {
        mUserList.clear();
        mButtonRequestUsers.setEnabled(false);
        mRVUserList.setVisibility(View.GONE);
        mPBUsers.setVisibility(View.VISIBLE);
        for (int id = 1; id <= 5; ++id) {
            ApiFactory.getService()
                    .getUser(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::addUser,
                            this::errorRequestUser,
                            () -> Log.i(TAG, "Complete")
                    );
        }
    }

    private void addUser(User user) {
        mUserList.add(user);
        if (mUserList.size() == 5) {
            showUsers();
        }
    }

    private void showUsers() {
        Collections.sort(mUserList, (o1, o2) -> o1.getId() - o2.getId());
        mRVUserList.setAdapter(new UserAdapter(getContext(), mUserList));
        mPBUsers.setVisibility(View.GONE);
        mRVUserList.setVisibility(View.VISIBLE);
        mButtonRequestUsers.setEnabled(true);
    }

    private void errorRequestUser(Throwable throwable) {
        throwable.printStackTrace();
        mUserList.clear();
        mPBUsers.setVisibility(View.GONE);
        mButtonRequestUsers.setEnabled(true);
    }

    @OnClick(R.id.btn_request_photo)
    public void requestPhoto() {
        ApiFactory.getService()
                .getPhoto(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {
                    mIVPhoto.setVisibility(View.GONE);
                    mPBPhoto.setVisibility(View.VISIBLE);
                    mButtonRequestPhoto.setEnabled(false);
                })
                .doAfterTerminate(() -> {
                    mPBPhoto.setVisibility(View.GONE);
                    mButtonRequestPhoto.setEnabled(true);
                })
                .subscribe(
                        this::showPhoto,
                        Throwable::printStackTrace,
                        () -> Log.i(TAG, "Complete")
                );
    }

    private void showPhoto(Photo photo) {
        mPhoto = photo;
        Picasso.with(getContext())
                .load(photo.getUrl())
                .into(mIVPhoto);
        mIVPhoto.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_request_todo)
    public void requestToDo() {
        int id = new Random().nextInt(200) + 1;
        ApiFactory.getService()
                .getToDo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {
                    mLLToDoResponse.setVisibility(View.GONE);
                    mPBToDo.setVisibility(View.VISIBLE);
                    mButtonRequestToDo.setEnabled(false);
                })
                .doAfterTerminate(() -> {
                    mPBToDo.setVisibility(View.GONE);
                    mButtonRequestToDo.setEnabled(true);
                })
                .subscribe(
                        this::showTodo,
                        Throwable::printStackTrace,
                        () -> Log.i(TAG, "Complete")
                );
    }

    private void showTodo(Todo todo) {
        mTodo = todo;
        mToDoId.setText(String.valueOf(todo.getId()));
        mToDoTitle.setText(todo.getTitle());
        mToDoCompleted.setText(String.valueOf(todo.getCompleted()));
        mLLToDoResponse.setVisibility(View.VISIBLE);
    }

}
