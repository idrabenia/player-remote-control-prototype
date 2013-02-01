package idrabenia;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        // Create a connection to the jabber.org server on a specific port.
        ConnectionConfiguration config = new ConnectionConfiguration("jabber.ccc.de");
        config.setReconnectionAllowed(true);
        Connection conn2 = new XMPPConnection(config);
        //Connection.DEBUG_ENABLED = true;
        conn2.connect();

        try {
            conn2.login("drabenia-test", "drabenia-test");
        } catch (XMPPException ex) {
            AccountManager manager = conn2.getAccountManager();
            manager.createAccount("drabenia-test", "drabenia-test");

            conn2.login("drabenia-test", "drabenia-test");
        }

        Chat newChat = conn2.getChatManager().createChat("drabenia-test2@jabber.ccc.de", new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                System.out.println(message.getBody());
            }
        });

        for(;;) {
            Thread.sleep(3000);
        }
    }

}
