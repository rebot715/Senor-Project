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

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mRoom));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                 
                mDrawerLayout,         
                R.drawable.ic_drawer,  
                R.string.drawer_open,  
                R.string.drawer_close  
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mRoom[position]);
                invalidateOptionsMenu(); 
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Rooms");
                invalidateOptionsMenu(); 
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

    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
            case R.id.action_websearch:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static class RoomFragment extends Fragment {
        public static final String ARG_ROOM_NUMBER = "room_number";

        public RoomFragment() {
            //empty constructor for subclasses
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
