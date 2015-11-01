package DB2;
import java.util.*;

public class Test
{
    private static Map<String, Film> FILMS = new HashMap<>();
    private static final String DATA_FILE_PATH = "cs542.db";

    public static void main(String[] args)
    {
        try
        {
            FILMS = Util.getInstance().readDataFile(DATA_FILE_PATH);
        }
        catch (Exception e)
        {
            System.out.println("Failed to read data file, " + e);
            System.exit(1);
        }

        testCase1();
        testCase2();
        testCase3();
    }

    private static void testCase1()
    {
        DB db = DB.getInstance();
        for (String key: FILMS.keySet())
        {
            Film film = FILMS.get(key);
            String[] attributes = film.getAttributes();
            String index = attributes[Film.Attributes.Year.ordinal()] + "|" + attributes[Film.Attributes.Format.ordinal()];
            db.put(film.getKey(), index);
        }

        System.out.println("\nStarting Test 1...");
        System.out.println("------------------------------------");

        System.out.println("\nAll DVD movies made in 1977:");
        db.get("1977|DVD");

        System.out.println("\nAll VHS movies made in 1990:");
        db.get("1990|VHS");

        System.out.println("\nAll DVD movies made in 2001:");
        db.get("2001|DVD");
    }

    private static void testCase2()
    {
        System.out.println();
        System.out.println("Starting Test 2...");
        System.out.println("------------------------------------");

        DB db = DB.getInstance();
        for (String key: FILMS.keySet())
        {
            Film film = FILMS.get(key);
            String[] attributes = film.getAttributes();
            String index = attributes[Film.Attributes.Year.ordinal()];
            db.put(film.getKey(), index);
        }

        System.out.println("\nFind all movies made in 2000:");
        db.get("2000");

        System.out.println("\nFind all movies made in 2005:");
        db.get("2005");

        System.out.println("\nFind all movies made in 2010:");
        db.get("2010");
    }

    private static void testCase3()
    {
        System.out.println();
        System.out.println("Starting Test 3...");
        System.out.println("------------------------------------");

        DB db = DB.getInstance();
        for (String key: FILMS.keySet())
        {
            Film film = FILMS.get(key);
            String[] attributes = film.getAttributes();
            String index = attributes[Film.Attributes.Year.ordinal()];
            db.put(film.getKey(), index);
        }

        String title = "Jack the Giant Killer";
        String year = "1977";
        String key = title + "|" + year;

        System.out.println("All movies made in " + year);
        System.out.println("\nBefore removing:");
        db.get(year);

        System.out.println("\nRemoving entry: " + key);
        db.remove(key);
        System.out.println("\nAfter removing:");
        db.get(year);
    }
}
