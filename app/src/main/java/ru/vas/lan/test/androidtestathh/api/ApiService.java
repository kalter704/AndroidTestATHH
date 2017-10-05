package ru.vas.lan.test.androidtestathh.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.vas.lan.test.androidtestathh.api.models.Comment;
import ru.vas.lan.test.androidtestathh.api.models.Photo;
import ru.vas.lan.test.androidtestathh.api.models.Post;
import ru.vas.lan.test.androidtestathh.api.models.Todo;
import ru.vas.lan.test.androidtestathh.api.models.User;

public interface ApiService {

    @GET("posts/{id}")
    Observable<Post> getPost(@Path("id") int id);

    @GET("comments/{id}")
    Observable<Comment> getComment(@Path("id") int id);

    @GET("users/{id}")
    Observable<User> getUser(@Path("id") int id);

    @GET("photos/{id}")
    Observable<Photo> getPhoto(@Path("id") int id);

    @GET("todos/{id}")
    Observable<Todo> getToDo(@Path("id") int id);

}
