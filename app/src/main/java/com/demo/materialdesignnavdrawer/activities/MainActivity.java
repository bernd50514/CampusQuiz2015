package com.demo.materialdesignnavdrawer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.materialdesignnavdrawer.R;
import com.demo.materialdesignnavdrawer.customViews.ScrimInsetsFrameLayout;
import com.demo.materialdesignnavdrawer.fragments.MainMenuFragment;
import com.demo.materialdesignnavdrawer.fragments.OptionFragment;
import com.demo.materialdesignnavdrawer.fragments.ProfileFragment;
import com.demo.materialdesignnavdrawer.managers.ManagerTypeface;
import com.demo.materialdesignnavdrawer.utils.UtilsDevice;
import com.demo.materialdesignnavdrawer.utils.UtilsMiscellaneous;

import java.util.HashMap;

import campusquizregandlogdesign.LoginActivity;
import campusquizregandlogdesign.com.example.helper.SQLiteHandler;
import campusquizregandlogdesign.com.example.helper.SessionManager;

/**
 * Main class hosting the navigation drawer
 *
 * @author Sotti https://plus.google.com/+PabloCostaTirado/about
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static double sNAVIGATION_DRAWER_ACCOUNT_SECTION_ASPECT_RATIO = 9d / 16d;

    private Menu menu;
    private OptionFragment fragment;
    private MainMenuFragment Listfragment;
    private ProfileFragment profileFragment;
    private ListFragment listFragment;
    private ListFragment newListfragment;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mFrameLayout_AccountView;
    private LinearLayout mNavDrawerEntriesRootView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private FrameLayout mFrameLayout_Home, mFrameLayout_Profile, mFrameLayout_HelpAndFeedback,
            mFrameLayout_Option, mFrameLayout_Ranking, mFrameLayout_Impressum, mFrameLayout_Logout;

    private TextView mTextView_AccountDisplayName, mTextView_AccountEmail;
    private TextView mTextView_Home, mTextView_Profile, mTextView_HelpAndFeedback, mTextView_Option, mTextView_Ranking, mTextView_Impressum, mTextView_Logout;


    private Toolbar mToolbar;
    private Button button;

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // TODO: Invoke new intent..
        if (id == R.id.menu_item_plus) {
            Intent intent = new Intent(MainActivity.this, SubmitQuestion.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        HashMap<String, String> user = db.getUserDetails();


        String name = user.get("name");
        String email = user.get("email");
        mTextView_AccountDisplayName = (TextView) findViewById(R.id.navigation_drawer_account_information_display_name);
        mTextView_AccountEmail = (TextView) findViewById(R.id.navigation_drawer_account_information_email);
        mTextView_AccountDisplayName.setText(name);
        mTextView_AccountEmail.setText(email);

        initialise();
    }

    /**
     * Bind, create and set up the resources
     */
    private void initialise() {

        // Toolbar
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Layout resources
        mFrameLayout_AccountView = (FrameLayout) findViewById(R.id.navigation_drawer_account_view);
        mNavDrawerEntriesRootView = (LinearLayout) findViewById(R.id.navigation_drawer_linearLayout_entries_root_view);
        // FrameLayout Element
        mFrameLayout_Home = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_home);
        mFrameLayout_Profile = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_profile);
        mFrameLayout_HelpAndFeedback = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_help_and_feedback);
        mFrameLayout_Option = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_option);
        mFrameLayout_Impressum = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_impressum);
        mFrameLayout_Logout = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_logout);


        mTextView_Home = (TextView) findViewById(R.id.navigation_drawer_items_textView_home);
        mTextView_Profile = (TextView) findViewById(R.id.navigation_drawer_items_textView_profile);
        mTextView_HelpAndFeedback = (TextView) findViewById(R.id.navigation_drawer_items_textView_help_and_feedback);
        mTextView_Option = (TextView) findViewById(R.id.navigation_drawer_items_textView_option);
        mTextView_Impressum = (TextView) findViewById(R.id.navigation_drawer_items_textView_impressum);
        mTextView_Logout = (TextView) findViewById(R.id.navigation_drawer_items_textView_logout);

        // Typefaces
        mTextView_AccountDisplayName.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        mTextView_AccountEmail.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_regular));
        mTextView_Home.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        mTextView_Profile.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        mTextView_HelpAndFeedback.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        mTextView_Option.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
//        mTextView_Ranking.setTypeface(ManagerTypeface.getTypeface(this,R.string.typeface_roboto_medium));
        mTextView_Impressum.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        mTextView_Logout.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));

        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark)); // Change Color of Statusbar
        mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.main_activity_navigation_drawer_rootLayout);

        mActionBarDrawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        mToolbar,
                        R.string.navigation_drawer_opened,
                        R.string.navigation_drawer_closed
                ) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Disables the burger/arrow animation by default
                super.onDrawerSlide(drawerView, 0);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle.syncState();

        // Navigation Drawer layout width
        int possibleMinDrawerWidth = UtilsDevice.getScreenWidth(this) -
                UtilsMiscellaneous.getThemeAttributeDimensionSize(this, android.R.attr.actionBarSize);
        int maxDrawerWidth = getResources().getDimensionPixelSize(R.dimen.navigation_drawer_max_width);

        mScrimInsetsFrameLayout.getLayoutParams().width = Math.min(possibleMinDrawerWidth, maxDrawerWidth);

        // Account section height
        mFrameLayout_AccountView.getLayoutParams().height = (int) (mScrimInsetsFrameLayout.getLayoutParams().width
                * sNAVIGATION_DRAWER_ACCOUNT_SECTION_ASPECT_RATIO);

        // Nav Drawer item click listener
        mFrameLayout_AccountView.setOnClickListener(this);
        mFrameLayout_Home.setOnClickListener(this);
        mFrameLayout_Profile.setOnClickListener(this);
        mFrameLayout_HelpAndFeedback.setOnClickListener(this);
        mFrameLayout_Option.setOnClickListener(this);
        mFrameLayout_Impressum.setOnClickListener(this);
        mFrameLayout_Logout.setOnClickListener(this);


        // Set the first item as selected for the first time


        // Create the first fragment to be shown


        getSupportActionBar().setTitle(getString(R.string.toolbar_title_home));

        if (Listfragment == null) {
            Listfragment = new MainMenuFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_content_frame, Listfragment, MainMenuFragment.TAG)
                .commit();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.navigation_drawer_account_view) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);

            // If the user is signed in, go to the profile, otherwise show sign up / sign in
        } else {
            if (!view.isSelected()) {
                onRowPressed((FrameLayout) view);

                switch (view.getId()) {   // call Main Menu Fragment
                    case R.id.navigation_drawer_items_list_linearLayout_home:// 1. case home
                    {


                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(getString(R.string.toolbar_title_home));
                        }


                        if (listFragment == null) {
                            listFragment = new MainMenuFragment();
                        }

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_activity_content_frame, listFragment, MainMenuFragment.TAG)
                                .commit();
                        break;

                    }

                    case R.id.navigation_drawer_items_list_linearLayout_profile: // 2. case profile
                    {

                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(getString(R.string.nav_drawer_item_profile));
                        }


                        if (newListfragment == null) {
                            newListfragment = new ProfileFragment();
                        }


                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_activity_content_frame, newListfragment, ProfileFragment.TAG)
                                .commit();
                        break;


                    }

                    case R.id.navigation_drawer_items_list_linearLayout_help_and_feedback:
                        startActivity(new Intent(view.getContext(), HelpActivity.class));
                        break;


                    case R.id.navigation_drawer_items_list_linearLayout_option: // 3. case option


                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(getString(R.string.optionen));
                        }


                        if (fragment == null) {
                            fragment = new OptionFragment();
                        }


                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_activity_content_frame, fragment, OptionFragment.TAG)
                                .commit();
                        break;


                    // TODO: Add Ranking slot
                    case R.id.navigation_drawer_items_list_linearLayout_impressum: // 4. case impressum

                        startActivity(new Intent(view.getContext(), Impressum.class));

                        break;

                    case R.id.navigation_drawer_items_list_linearLayout_logout: // 5. case logout

                        session.setLogin(false);

                        db.deleteUsers();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    default:
                        break;
                }
            } else {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        }
    }

    /**
     * Set up the rows when any is pressed
     *
     * @param pressedRow is the pressed row in the drawer
     */
    private void onRowPressed(FrameLayout pressedRow) {
        if (pressedRow.getTag() != getResources().getString(R.string.tag_nav_drawer_special_entry)) {
            for (int i = 0; i < mNavDrawerEntriesRootView.getChildCount(); i++) {
                View currentView = mNavDrawerEntriesRootView.getChildAt(i);

                boolean currentViewIsMainEntry = currentView.getTag() ==
                        getResources().getString(R.string.tag_nav_drawer_main_entry);

                if (currentViewIsMainEntry) {
                    if (currentView == pressedRow) {
                        currentView.setSelected(true);
                    } else {
                        currentView.setSelected(false);
                    }
                }
            }
        }

        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    // Test for Login
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
