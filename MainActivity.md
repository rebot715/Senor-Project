    package com.example.android.navigationdrawerexample;
    
    import java.io.File;
    import java.io.IOException;
    import java.util.Locale;
    
    import android.app.Activity;
    import android.app.Fragment;
    import android.app.FragmentManager;
    import android.app.SearchManager;
    import android.content.Context;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.content.res.Configuration;
    import android.media.AudioRecord;
    import android.media.MediaRecorder;
    import android.media.MediaPlayer;
    import android.os.Bundle;
    import android.os.Environment;
    import android.support.v4.app.ActionBarDrawerToggle;
    import android.support.v4.view.GravityCompat;
    import android.support.v4.widget.DrawerLayout;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.Toast;
    import android.util.Log;
    import android.media.MediaRecorder;
    import android.media.MediaPlayer;


    public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mRoom;
    private int position;
    Intent a,b,c,d;
    private String mFileName = Environment.getExternalStorageDirectory() + "/audiorecorder.3gpp";
    private String OUTPUT_FILE;
    private MediaRecorder recorder;
    private MediaPlayer   mPlayer;
    boolean mStartRecording = true;
    boolean mStartPlaying = true;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerTitle = "Home-Auto Sys";
        mTitle = getTitle();
        mRoom = getResources().getStringArray(R.array.rooms_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mRoom));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mRoom[position]);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Rooms");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this room
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startintent1(){
        a = new Intent(this, roomintent1.class);
        startActivity(a);
    }
    
    public void startintent2(){
        b = new Intent(this, roomintent2.class);
        startActivity(b);
    }
    
    public void startintent3(){
        c = new Intent(this, roomintent3.class);
        startActivity(c);
    }
    
    public void startintent4(){
        d = new Intent(this, roomintent4.class);
        startActivity(d);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);

            //selects intent based on position
            if(position==1){
                startintent1();
            }
            if(position==2){
                startintent2();
            }
            if(position==3){
                startintent3();
            }
            if(position==4){
                startintent4();
            }
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putInt(RoomFragment.ARG_ROOM_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mRoom[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a room
     */
    public static class RoomFragment extends Fragment {
        public static final String ARG_ROOM_NUMBER = "room_number";

        public RoomFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //finds selected room name
            View rootView = inflater.inflate(R.layout.fragment_room, container, false);
            int i = getArguments().getInt(ARG_ROOM_NUMBER);
            String room = getResources().getStringArray(R.array.rooms_array)[i];
            int imageId = getResources().getIdentifier(room.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());

            //changes the image on room selection
            //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);

            getActivity().setTitle(room);
            return rootView;
        }
    }

        //button for the light
        public void lightbutton(View view) {
        Log.d("VIVZ","Hello Light");
        }

        //recorder button
        public void recordbutton(View v)throws IOException{
            Log.d("VIVZ","Button was clicked");
            counter++;
            //creates new instance of the recorder
            MediaRecorder recorder = new MediaRecorder();
            //if statements for seeing how many times the button has been pressed
            //calls the onRecord class
            if(counter==1) {
                Log.d("VIVZ","onRecord() call");
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
            else if(counter==2){
                Log.d("VIVZ","stopRecording() call");
                stopRecording();
                mStartRecording = !mStartRecording;
            }
            else if(counter==3){
                Log.d("VIVZ","onPlay() call");
                onPlay(mStartPlaying);
                mStartPlaying = !mStartPlaying;
            }
            else if(counter==4){
                Log.d("VIVZ","stopPlaying() call");
                stopPlaying();
                mStartPlaying = !mStartPlaying;
                counter=0;
            }
        }

        private void onRecord(boolean start) throws IOException {
            if(start){startRecording();}
            else{stopRecording();}
        }
        
        private void startRecording () throws IOException {
            Log.d("VIVZ", "start recording");
            ditchMediaRecorder();
            File outFile = new File(mFileName);
            if(outFile.exists()){
                outFile.delete();}
            Log.d("VIVZ", "Initializing");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(mFileName);
            Log.d("VIVZ", "Initialized");
            recorder.prepare();
            Log.d("VIVZ", "prepared");
            recorder.start();
            Log.d("VIVZ", "started");
        }
        
        private void stopRecording() {
            Log.d("VIVZ", "stop recording");
            recorder.stop();
            ditchMediaRecorder();
            Log.d("VIVZ", "Recorded!");
        }
        
        private void ditchMediaRecorder(){
            if(recorder != null){
                recorder.release();
                Log.d("VIVZ", "Recorder released");}
        }

        private void onPlay(boolean start) throws IOException {
            if(start){startPlaying();}
            else{stopPlaying();}
        }
        private void startPlaying() throws IOException {
            Log.d("VIVZ", "start play back");
            if(mPlayer!=null){
                mPlayer.stop();
                ditchMediaPlayer();}
            Log.d("VIVZ", "Initializing player");
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(mFileName);
            Log.d("VIVZ", "Initialized");
            Log.d("VIVZ", "preparing");
            mPlayer.prepare();
            mPlayer.start();
            Log.d("VIVZ","started");
        }
        private void stopPlaying() {
            Log.d("VIVZ","stop playing");
            ditchMediaPlayer();
            mPlayer = null;
        }
        private void ditchMediaPlayer(){
        if(mPlayer != null){
            mPlayer.release();
            Log.d("VIVZ", "Player Released");}
        }
    }
    
    /*PackageManager packageManager = getPackageManager();
    List activities = packageManager.queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY);
    boolean isIntentSafe = activities.size() > 0;*/
