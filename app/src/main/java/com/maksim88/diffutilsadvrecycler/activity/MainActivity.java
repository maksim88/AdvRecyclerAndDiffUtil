package com.maksim88.diffutilsadvrecycler.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.maksim88.diffutilsadvrecycler.R;
import com.maksim88.diffutilsadvrecycler.adapter.SwipeableAdapter;
import com.maksim88.diffutilsadvrecycler.api.UserEndpointService;
import com.maksim88.diffutilsadvrecycler.model.Result;
import com.maksim88.diffutilsadvrecycler.model.User;
import com.maksim88.diffutilsadvrecycler.viewmodel.UserListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://randomuser.me/api/";

    private static final String DEFAULT_SEED = "seed";
    private static final int DEFAULT_RESULTS_COUNT = 50;

    private UserEndpointService apiService;

    private RecyclerView usersRecycler;

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<UserListItem> items;

    private SwipeableAdapter adapter;


    private final char[] alphabetChars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRetrofit();
        RecyclerViewSwipeManager swipeMgr = new RecyclerViewSwipeManager();
        usersRecycler = (RecyclerView) findViewById(R.id.user_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList(generateRandomSeed(4));
            }
        });

        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        items = new ArrayList<>();
        adapter = new SwipeableAdapter(this, items);
        usersRecycler.setAdapter(swipeMgr.createWrappedAdapter(adapter));

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        animator.setSupportsChangeAnimations(false);
        usersRecycler.setItemAnimator(animator);

        swipeMgr.attachRecyclerView(usersRecycler);
        updateList(DEFAULT_SEED);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(UserEndpointService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_list:
                updateList(DEFAULT_SEED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateList(String seed){
        swipeRefreshLayout.setRefreshing(false);
        Call<Result> users = apiService.getUsers(seed, String.valueOf(DEFAULT_RESULTS_COUNT));
        users.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    //Timber.d("Got %d items back!", response.body().getUsers().size());
                    List<User> users = response.body().getUsers();
                    ArrayList<UserListItem> viewModels = new ArrayList<>();
                    for (User user : users) {
                        UserListItem i = new UserListItem(user.getPicture().getLarge(), user.getName().getLast());
                        viewModels.add(i);
                    }
                    adapter.swapAndDiffList(viewModels);
                } else {
                    Timber.e("Response not successful: %s", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Timber.e("Failure: %s", t.toString());
            }
        });
    }

    private String generateRandomSeed(int numberOfChars) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < numberOfChars; i++) {
            char c = alphabetChars[random.nextInt(alphabetChars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

}
