package DB2;
import java.io.File;
import java.util.*;

public class DB {

    // used to store indexes and its associated list of keys
    private static Map<String, Set<String>> INDEX_LIST_OF_KEYS;

    // used to store key and film info
    private static Map<String, Film> KEY_FILM;

    // a file variable which is used to store indexes information.
    private static File INDEX_FILE = new File("indexFile");

    // shared by all db instances
    private static File DATA_FILE = new File("cs542.db");

    // used to help retrieve the index given the key
    private static Map<String, Set<String>> KEY_LIST_OF_INDEXES;

    private static DB m_instance = null;
    public static DB getInstance()
    {
        if (m_instance == null)
        {
            m_instance = new DB();
            m_instance.initializeDB();
        }

        return m_instance;
    }

    private DB(){}
   // call functions to read data from cs542.db, to read index from indexFilm.
    private void initializeDB()
    {
        System.out.println("Initializing database...");

        Util util = Util.getInstance();
        try
        {
            if (!DATA_FILE.exists())
            {
                DATA_FILE.createNewFile();
                KEY_FILM = new HashMap<>();
            }
            else
            {
                KEY_FILM = util.readDataFile(DATA_FILE.getAbsolutePath());
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed to read data from file, " + e);
            System.exit(1);
        }

        try
        {
            if (!INDEX_FILE.exists())
            {
                INDEX_FILE.createNewFile();
                INDEX_LIST_OF_KEYS = new HashMap<>();
            }
            else
            {
                INDEX_LIST_OF_KEYS = util.readIndexFile(INDEX_FILE);
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed to read index from file, " + e);
            System.exit(1);
        }

        KEY_LIST_OF_INDEXES = new HashMap<>();
        for (String index: INDEX_LIST_OF_KEYS.keySet())
        {
            Set<String> keys = INDEX_LIST_OF_KEYS.get(index);
            for (String key: keys)
            {
                if (!KEY_LIST_OF_INDEXES.containsKey(key))
                {
                    KEY_LIST_OF_INDEXES.put(key, new HashSet<>());
                }
                KEY_LIST_OF_INDEXES.get(key).add(index);
            }
        }

        System.out.println("Database is running");
    }

    /**
     * adds the index entry. If the index already exists, then add the key into INDEX_LIST_OF_KEYS and KEY_LIST_OF_INDEXES. 
     * Then, update index film
     *
     * @param key
     * @param dataValue
     */
    public void put(String key, String dataValue)
    {
        if (!KEY_LIST_OF_INDEXES.containsKey(key))
        {
            KEY_LIST_OF_INDEXES.put(key, new HashSet<>());
        }
        KEY_LIST_OF_INDEXES.get(key).add(dataValue);

        if (!INDEX_LIST_OF_KEYS.containsKey(dataValue))
        {
            INDEX_LIST_OF_KEYS.put(dataValue, new HashSet<>());
        }
        INDEX_LIST_OF_KEYS.get(dataValue).add(key);
        Util.getInstance().writeIndexFile(INDEX_FILE, INDEX_LIST_OF_KEYS);
    }

    /**
     * retrieves the list of keys given the index
     *
     * @param dataValue
     * @return
     */
    public Set<String> get(String dataValue)
    {
        Set<String> keys = new HashSet<>();
        if (INDEX_LIST_OF_KEYS.containsKey(dataValue))
        {
            keys = INDEX_LIST_OF_KEYS.get(dataValue);
            keys.stream().forEach(key -> System.out.println(KEY_FILM.get(key).toString()));
        }
        else
        {
            System.out.println("Index: " + dataValue + " doesn't exist!");
            System.out.flush();
        }

        return keys;
    }

    /**
     * deletes the index given the key. First, get the indexs given the key by KEY_LIST_OF_INDEXES. Then remove the associated
     * entry in KEY_LIST_OF_INDEXES and KEY_FILM, and update cs542.db.
     * For each index, delete the associated key in INDEX_LIST_OF_KEYS. If this entry is empty after deleting the key, 
     * then delete the index,otherwise, only delete the key.After done that, update the index file.
     *
     * @param key
     */
    public void remove(String key)
    {
        if (KEY_LIST_OF_INDEXES.containsKey(key))
        {
            Set<String> indexes = KEY_LIST_OF_INDEXES.get(key);
            KEY_LIST_OF_INDEXES.remove(key);
            KEY_FILM.remove(key);
            Util.getInstance().writeDateFile(DATA_FILE, KEY_FILM.values());

            for (String index: indexes)
            {
                Set<String> keys = INDEX_LIST_OF_KEYS.get(index);
                keys.remove(key);

                if (keys.isEmpty())
                {
                    INDEX_LIST_OF_KEYS.remove(index);
                    System.out.println("Index: " + index + " has been removed.");
                }
                else
                {
                    INDEX_LIST_OF_KEYS.put(index, keys);
                    System.out.println("There are other keys associated with index " + index + ". Index has been refreshed.");
                }
            }
            Util.getInstance().writeIndexFile(INDEX_FILE, INDEX_LIST_OF_KEYS);
        }
        else
        {
            System.out.println("Key: " + key + " does not exist.");
        }
    }
}


