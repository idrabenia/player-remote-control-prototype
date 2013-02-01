package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ToggleButton;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

public class MyActivity extends Activity {

    private Chat playerChar;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            initJabber();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void onPlayButtonClicked(View view) {
        try {
            playerChar.sendMessage("Play!");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public void onStopButtonClicked(View view) {
        try {
            playerChar.sendMessage("Stop!");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initJabber() throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        // Create a connection to the jabber.org server on a specific port.
        ConnectionConfiguration config = new ConnectionConfiguration("jabber.ccc.de");
        config.setReconnectionAllowed(true);
        XMPPConnection conn2 = new XMPPConnection(config);
        //Connection.DEBUG_ENABLED = true;

        try {
            conn2.connect();
            conn2.login("drabenia-test2", "drabenia-test2");
        } catch (XMPPException ex) {
            AccountManager manager = conn2.getAccountManager();
            manager.createAccount("drabenia-test2", "drabenia-test2");

            conn2.login("drabenia-test2", "drabenia-test2");
        }

        playerChar = conn2.getChatManager().createChat("drabenia-test@jabber.ccc.de", new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                System.out.println(message.getBody());
            }
        });
    }

}
