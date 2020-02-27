package genericnode;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

/**
 * A model for key-value store. A Hashtable is encapsulated, so that it can be reused by servers of multiple protocols.
 * put(), get(), del(), and store() methods are implemented.
 */
public class DataStore {
    public Hashtable<String, String> data = new Hashtable<>();

    public synchronized String put(String key, String value) {
        data.put(key, value);
        return "server response: put key=" + key;
    }

    public synchronized String get(String key) {
        String value = data.get(key);
        if (value == null) {
            value = "<No such key>";
        }
        return "server response: get key=" + key + " val=" + value;
    }

    public synchronized String del(String key) {
        String value = data.remove(key);
        if (value != null) {
            return "server response: delete key=" + key;
        } else {
            return "server response: delete key=" + key + " <No such key>";
        }
    }

    public synchronized String store() {
        StringBuilder sb = new StringBuilder();
        sb.append("server response:\n");

        Set<String> keys = data.keySet();
        for(String key: keys){
            sb.append("key:").append(key).append(":value:").append(data.get(key)).append('\n');
        }
        String s = sb.toString();
        s = s.substring(0, s.length() - 1);     // remove trailing empty line
        if (s.length() > 65000) {
            s = "TRIMMED:\n" + s.substring(0, 65000);
        }
        return s;
    }
}
