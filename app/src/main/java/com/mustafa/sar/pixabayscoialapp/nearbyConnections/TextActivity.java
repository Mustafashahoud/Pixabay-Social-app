package com.mustafa.sar.pixabayscoialapp.nearbyConnections;
//Nearby.Connections.sendPayload(mGoogleApiClient, mRemoteHostEndpoint, Payload.fromBytes(message.getBytes(Charset.forName("UTF-8"))));

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;
import com.mustafa.sar.pixabayscoialapp.R;

import java.util.Collection;
import java.util.Random;

/**
 * Our WalkieTalkie Activity. This Activity has 4 {@link State}s.
 *
 * <p>{@link State#UNKNOWN}: We cannot do anything while we're in this state. The app is likely in
 * the background.
 *
 * <p>{@link State#DISCOVERING}: Our default state (after we've connected). We constantly listen for
 * a device to advertise near us.
 *
 * <p>{@link State#ADVERTISING}: If a user shakes their device, they enter this state. We advertise
 * our device so that others nearby can discover us.
 *
 * <p>{@link State#CONNECTED}: We've connected to another device. We can now talk to them by holding
 * down the volume keys and speaking into the phone. We'll continue to advertise (if we were already
 * advertising) so that more people can connect to us.
 */
public class TextActivity extends ConnectionsActivity {
    /** If true, debug logs are shown on the device. */
    private static final boolean DEBUG = true;

    Context mContext = TextActivity.this;

    private static final String TAG = "VoiceActivity";

    /**
     * The connection strategy we'll use for Nearby Connections. In this case, we've decided on
     * P2P_STAR, which is a combination of Bluetooth Classic and WiFi Hotspots.
     */
    private static final Strategy STRATEGY = Strategy.P2P_STAR;



    /**
     * Advertise for 30 seconds before going back to discovering. If a client connects, we'll continue
     * to advertise indefinitely so others can still connect.
     */
    private static final long ADVERTISING_DURATION = 30000;


    /**
     * This service id lets us find other nearby devices that are interested in the same thing. Our
     * sample does exactly one thing, so we hardcode the ID.
     */
    private static final String SERVICE_ID =
            "com.mustafa.sar.pixabayscoialapp.nearbyMessaging.SERVICE_ID";

    /**
     * The state of the app. As the app changes states, the UI will update and advertising/discovery
     * will start/stop.
     */
    private State mState = State.UNKNOWN;

    /** A random UID used as this device's endpoint name. */
    private String mName;



    /** A running log of debug messages. Only visible when DEBUG=true. */
    private TextView mDebugLogView;

    private Button mAdvertiseButton;
    private Button mDiscoverButton;

    private Button sendButton;

    private EditText editTextMessage ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_nearby);

        mName = generateRandomName();
        ((TextView) findViewById(R.id.name)).setText(mName);
        setupWidgets();
        setButtonsListeners();

    }
    private void setupWidgets(){
        sendButton  = findViewById(R.id.button_send);
        editTextMessage = findViewById(R.id.edittext_message);
        mDebugLogView = (TextView) findViewById(R.id.debug_text);
        mDebugLogView.setVisibility(DEBUG ? View.VISIBLE : View.GONE);
        mDebugLogView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setButtonsListeners(){
        mAdvertiseButton = findViewById(R.id.advertise_button);
        mAdvertiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(State.ADVERTISING);
            }
        });
        mDiscoverButton = findViewById(R.id.discover_button);
        mDiscoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(State.DISCOVERING);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMessage.getText() != null && !editTextMessage.getText().toString().equals("")){

                    final String message = editTextMessage.getText().toString();
                    logD(message);
                    send(Payload.fromBytes(message.getBytes()));
                    editTextMessage.setText("");
                }
                else{
                    logW("Please Enter a message");

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setState(State.DISCOVERING);
    }

    @Override
    protected void onStop() {


        setState(State.UNKNOWN);



        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (getState() == State.CONNECTED || getState() == State.ADVERTISING) {
            setState(State.DISCOVERING);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onEndpointDiscovered(Endpoint endpoint) {
        // We found an advertiser! // request a connection
        if (!isConnecting()) {
            connectToEndpoint(endpoint);
        }
    }

    //

    @Override
    protected void onConnectionInitiated(Endpoint endpoint, ConnectionInfo connectionInfo) {
        // A connection to another device has been initiated! We'll accept the connection immediately.
        acceptConnection(endpoint); // // Automatically accept the connection on both sides.
    }

    @Override
    protected void onEndpointConnected(Endpoint endpoint) {
        Toast.makeText(
                this, getString(R.string.toast_connected, endpoint.getName()), Toast.LENGTH_SHORT)
                .show();
        setState(State.CONNECTED);
    }

    @Override
    protected void onEndpointDisconnected(Endpoint endpoint) {
        Toast.makeText(
                this, getString(R.string.toast_disconnected, endpoint.getName()), Toast.LENGTH_SHORT)
                .show();

        // If we lost all our endpoints, then we should reset the state of our app and go back
        // to our initial state (discovering).
        if (getConnectedEndpoints().isEmpty()) {
            setState(State.DISCOVERING);
        }
    }

    @Override
    protected void onConnectionFailed(Endpoint endpoint) {
        // Let's try someone else.
        if (getState() == State.DISCOVERING && !getDiscoveredEndpoints().isEmpty()) {
            connectToEndpoint(pickRandomElem(getDiscoveredEndpoints()));
        }
    }

    /**
     * The state has changed. I wonder what we'll be doing now.
     *
     * @param state The new state.
     */
    private void setState(State state) {
        if (mState == state) {
            logW("State set to " + state + " but already in that state");
            return;
        }

      logD("State set to " + state);

        State oldState = mState;
        mState = state;
        onStateChanged(oldState, state);
    }

    /** @return The current state. */
    private State getState() {
        return mState;
    }

    /**
     * State has changed.
     *
     * @param oldState The previous state we were in. Clean up anything related to this state.
     * @param newState The new state we're now in. Prepare the UI for this state.
     */
    private void onStateChanged(State oldState, State newState) {

        // Update Nearby Connections to the new state.
        switch (newState) {
            case DISCOVERING:
                if (isAdvertising()) {
                    stopAdvertising();
                }
                disconnectFromAllEndpoints();
                startDiscovering();
                break;
            case ADVERTISING:
                if (isDiscovering()) {
                    stopDiscovering();
                }
                disconnectFromAllEndpoints();
                startAdvertising();
                break;
            case CONNECTED:
                if (isDiscovering()) {
                    stopDiscovering();
                }
                break;
            case UNKNOWN:
                stopAllEndpoints();
                break;
            default:
                // no-op
                break;
        }
    }



    /** {@see ConnectionsActivity#onReceive(Endpoint, Payload)} */
    @Override
    protected void onReceive(Endpoint endpoint, Payload payload) {
        if (payload.getType() == Payload.Type.BYTES) {
            logD(endpoint.getName() + " sent: " + new String(payload.asBytes()));
        }
    }

    /** {@see ConnectionsActivity#getRequiredPermissions()} */
    @Override
    protected String[] getRequiredPermissions() {
        return join(
                super.getRequiredPermissions(),
                Manifest.permission.RECORD_AUDIO);
    }

    /** Joins 2 arrays together. */
    private static String[] join(String[] a, String... b) {
        String[] join = new String[a.length + b.length];
        System.arraycopy(a, 0, join, 0, a.length);
        System.arraycopy(b, 0, join, a.length, b.length);
        return join;
    }

    /**
     * Queries the phone's contacts for their own profile, and returns their name. Used when
     * connecting to another device.
     */
    @Override
    protected String getName() {
        return mName;
    }

    /** {@see ConnectionsActivity#getServiceId()} */
    @Override
    public String getServiceId() {
        return SERVICE_ID;
    }

    /** {@see ConnectionsActivity#getStrategy()} */
    @Override
    public Strategy getStrategy() {
        return STRATEGY;
    }


    @Override
    protected void logV(String msg) {
        super.logV(msg);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_verbose)));
    }

    @Override
    protected void logD(String msg) {
        super.logD(msg);
        if (msg.contains(State.ADVERTISING)
                || msg.contains(State.DISCOVERING)
                || msg.contains(State.CONNECTED)
                || msg.contains(State.UNKNOWN)){
            appendToLogs(toColor(msg, getResources().getColor(R.color.blue)));
        }else {
            appendToLogs(toColor(msg, getResources().getColor(R.color.black)));
        }

    }

    @Override
    protected void logW(String msg) {
        super.logW(msg);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_warning)));
    }

    @Override
    protected void logW(String msg, Throwable e) {
        super.logW(msg, e);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_warning)));
    }

    @Override
    protected void logE(String msg, Throwable e) {
        super.logE(msg, e);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_error)));
    }

    private void appendToLogs(CharSequence msg) {
        mDebugLogView.append("\n");
       // mDebugLogView.setTextColor(getResources().getColor(R.color.black));
        mDebugLogView.append(DateFormat.format("hh:mm", System.currentTimeMillis()) + ": ");
        mDebugLogView.append(msg);
    }

    private static CharSequence toColor(String msg, int color) {
        SpannableString spannable = new SpannableString(msg);
        spannable.setSpan(new ForegroundColorSpan(color), 0, msg.length(), 0);
        return spannable;
    }

    private static String generateRandomName() {
        String name = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            name += random.nextInt(10);
        }
        return name;
    }

    @SuppressWarnings("unchecked")
    private static <T> T pickRandomElem(Collection<T> collection) {
        return (T) collection.toArray()[new Random().nextInt(collection.size())];
    }


    /** States that the UI goes through. */
    public enum State implements CharSequence {
        UNKNOWN,
        DISCOVERING,
        ADVERTISING,
        CONNECTED;

        @Override
        public int length() {
            return 0;
        }

        @Override
        public char charAt(int index) {
            return 0;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }
    }
}
