package com.mustafa.sar.instagramthesis.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.mustafa.sar.instagramthesis.R;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context context = ProfileActivity.this;
    private ImageView profilePhotoImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ProfileActivity");
        setContentView(R.layout.activity_profile);

//        setupBottomNavigationView();
//        setupToolbar();
//        setupActivityWidgets();
//        setProfileImage();
//        // tempGridSetup();
//
//        setupRecycleView();

        profileContainerMethod();
    }

    private void profileContainerMethod(){

        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer , profileFragment);
        fragmentTransaction.addToBackStack("Profile");
        fragmentTransaction.commit();

    }

//    /**
//     * sets the profile image
//     */
//    private void setProfileImage() {
//        String imgURL = "https://www.muscleprodigy.com/wp-content/uploads/2015/06/wallpaper1.jpg";
//        UniversalImageLoader.setImage(imgURL, profilePhotoImg, null, "");
//    }
//
//    private void setupRecycleView() {
//
//        int recyclerWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = recyclerWidth / 3;
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//
//
//        /*
//         * for setting a divider between the items or the views in The recycleView which can be done in two ways
//         *the first one is by creating a new class that extends RecyclerView.ItemDecoration and overriding the method
//         * getItemOffsets()
//         * */
//        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(1, EqualSpacingItemDecoration.GRID));
//
//        /*
//         *
//         * OR by using the new class DividerItemDecoration */
//
//        /*DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL );
//        recyclerView.addItemDecoration(decoration);
//        DividerItemDecoration decoration2 = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL );
//        recyclerView.addItemDecoration(decoration2);*/
//
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
//        recyclerView.setLayoutManager(layoutManager);
//        ArrayList<String> createLists = tempRecyclerView();
//        RecycleViewAdapter adapter = new RecycleViewAdapter(context, R.layout.layout_grid_imageview, "", createLists);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private ArrayList<String> tempRecyclerView() {
//        ArrayList<String> imgs = new ArrayList<>();
//        imgs.add("http://www.goodwp.com/images/201609/goodwp.com_33046.jpg");
//        imgs.add("https://www.ayezakhanofficial.com/wp-content/uploads/2017/01/random_selfie_2017.jpg");
//        imgs.add("https://vbothransh.files.wordpress.com/2014/09/selfie5.jpg");
//        imgs.add("https://i.pinimg.com/originals/5f/b4/f5/5fb4f5d4fc4f8bf4c77d02534bbbcd2c.jpg");
//        imgs.add("https://i2.wp.com/www.girlsdp.com/wp-content/uploads/2017/11/4a8c1c209e4fbe8a01738977aa2f9391.jpeg");
//        imgs.add("https://78.media.tumblr.com/tumblr_m7zn82Zdml1r4nm0p.jpg");
//        imgs.add("http://data.whicdn.com/images/35013714/tumblr_m8lik6EVg71rwuvcoo1_500_large.jpg");
//        imgs.add("https://cdn2.stylecraze.com/wp-content/uploads/2013/10/2.-Anushka-Sharma_1.jpg");
//        imgs.add("https://img.izismile.com/img/img6/20130719/640/beautiful_girls_make_the_world_go_around_640_25.jpg");
//        imgs.add("http://nguoi-noi-tieng.com/images/item/tieu-su-hot-girl-cctalk-ta-phuong-thuy-529563.jpg");
//        imgs.add("https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX1240415.jpg");
//        imgs.add("https://assets.myntassets.com/h_240,q_90,w_180/v1/assets/images/1109329/2015/12/4/11449227463414-RARE-Navy-Ruffle-Top-5921449227463023-1_mini.jpg");
//        imgs.add("https://ak0.scstatic.net/1/cdn2-cont7.sweetcouch.com/143281549068917369-joy-grey-formal-top.jpg");
//        imgs.add("https://rukminim1.flixcart.com/image/612/612/j9hdn680/top/v/t/g/l-kannantasss-04-l-bhumi-original-imaetkeaspdtgssn.jpeg");
//        imgs.add("https://78.media.tumblr.com/tumblr_m7zn82Zdml1r4nm0p.jpg");
//        imgs.add("http://data.whicdn.com/images/35013714/tumblr_m8lik6EVg71rwuvcoo1_500_large.jpg");
//        imgs.add("https://cdn2.stylecraze.com/wp-content/uploads/2013/10/2.-Anushka-Sharma_1.jpg");
//        imgs.add("https://78.media.tumblr.com/tumblr_m7zn82Zdml1r4nm0p.jpg");
//        imgs.add("http://nguoi-noi-tieng.com/images/item/tieu-su-hot-girl-cctalk-ta-phuong-thuy-529563.jpg");
//        imgs.add("https://img.izismile.com/img/img6/20130719/640/beautiful_girls_make_the_world_go_around_640_25.jpg");
//        imgs.add("https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX1240415.jpg");
//        imgs.add("http://www.goodwp.com/images/201609/goodwp.com_33046.jpg");
//        imgs.add("https://78.media.tumblr.com/tumblr_m7zn82Zdml1r4nm0p.jpg");
//        imgs.add("http://data.whicdn.com/images/35013714/tumblr_m8lik6EVg71rwuvcoo1_500_large.jpg");
//        imgs.add("http://data.whicdn.com/images/35013714/tumblr_m8lik6EVg71rwuvcoo1_500_large.jpg");
//        imgs.add("http://mygreatminds.com/wp-content/uploads/2018/04/495272006.jpg");
//        imgs.add("http://mygreatminds.com/wp-content/uploads/2018/04/495272006.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//
//        // setupImageGrid(imgs); // If I wanna use GridView instead of RecycleView but most likely I would NOT
//        return imgs;
//    }
//
//
//    /**
//     * sets up the widgets that we have
//     */
//    private void setupActivityWidgets() {
//
//        ProgressBar progressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
//        progressBar.setVisibility(View.GONE);
//        profilePhotoImg = (ImageView) findViewById(R.id.profile_photo);
//
//
//    }
//
//
// /*   private void tempGridSetup(){
//        ArrayList<String> imgs = new ArrayList<>();
//        imgs.add("");
//        imgs.add("http://www.goodwp.com/images/201609/goodwp.com_33046.jpg");
//        imgs.add("http://mygreatminds.com/wp-content/uploads/2018/04/495272006.jpg");
//        imgs.add("http://mygreatminds.com/wp-content/uploads/2018/04/495272006.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        imgs.add("http://media.salemwebnetwork.com/cms/IB/11655-teenage_girl_edited.630w.tn.jpg");
//        setupImageGrid(imgs);
//    }*/
//
//    /*private void setupImageGrid(ArrayList<String> imgs) {
//
//        GridView gridView = (GridView) findViewById(R.id.gridView);
//        GridImageAdapter adapter = new GridImageAdapter(context , R.layout.layout_grid_imageview ,"",imgs);
//        gridView.setAdapter(adapter);
//
//    }*/
//
//
//    /**
//     * Sitting up the BottomNavigationView and disabling the animation and shifting
//     */
//    private void setupBottomNavigationView() {
//
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(context, bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//        // menuItem.getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
//
//
//    }
//
//    /**
//     * sets the toolbar
//     */
//    private void setupToolbar() {
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.profileToolBar);
//        setSupportActionBar(myToolbar);
//        ImageView imageView = (ImageView) findViewById(R.id.profileMenu);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, AccountSettingActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//    }
//
//    /*    *//**
//     * this is how Menus work you should add otherwise it won't work
//     * @param menu
//     * @return
//     *//*
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.profile_menu, menu);
//        return true;
//    }*/
//
//
//    //                      //                    //               //
//
//    /**
//     * you can use this method instead of using myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
//     * the same exactly
//     * @param item
//     * @return
//     */
///*    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.profileMenu:
//                Toast.makeText(this, "You have selected fucked up Menu", Toast.LENGTH_SHORT).show();
//
//
//        }
//        return true;
//    }*/

    @Override
    public void onBackPressed() {
        finish();
    }
}
