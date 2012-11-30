/**
 * Advertise - Package: syam.advertise.announce
 * Created: 2012/11/30 8:54:55
 */
package syam.advertise.announce;

import java.util.logging.Logger;

import syam.advertise.Advertise;
import syam.advertise.database.Database;
import syam.advertise.util.Util;

/**
 * AdvertiseManager (AdvertiseManager.java)
 * @author syam(syamn)
 */
public class AdvertiseManager {
    // Logger
    private static final Logger log = Advertise.log;
    private static final String logPrefix = Advertise.logPrefix;
    private static final String msgPrefix = Advertise.msgPrefix;
    private final Advertise plugin;

    // taskID
    private int taskID = -1;
    Database db = Advertise.getDatabases();

    public AdvertiseManager (final Advertise plugin){
        this.plugin = plugin;
    }

    public String getNextMessage(){
        return "&cSAKURA ADVERTISEMENT! Hello, &b%player%&c! (testing!)";
    }

    /**
     * 広告を追加
     * @param playerName
     * @param days
     * @param text
     */
    public void addAdvertise(final String playerName, final int days, final String text){
        final int playerID = getUserID(playerName, true);

        Long registered = Util.getCurrentUnixSec();
        Long expired = registered + (days * 86400);

        db.write("INSERT INTO " + db.dataTable + " (player_id, registered, expired, `text`) VALUES (?, ?, ?, ?)",
                playerID, registered.intValue(), expired.intValue(), text);
    }

    /**
     * Get player_id from Database
     * @param playerName
     * @param addNew
     * @return
     */
    public int getUserID(final String playerName, final boolean addNew){
        // プレイヤーID(DB割り当て)を読み出す
        int playerID = db.getInt("SELECT player_id FROM " + db.userTable + " WHERE player_name = ?", playerName);

        // 存在確認
        if (playerID <= 0){
            // not found
            if (addNew) {
                // adding
                db.write("INSERT INTO " + db.userTable + " (player_name) VALUES (?)", playerName); // usersテーブル
                playerID = db.getInt("SELECT player_id FROM " + db.userTable + " WHERE player_name = ?", playerName);
            }else{
                return 0;
            }
        }

        return playerID;
    }
}
