package DB2;
import java.io.*;
import java.util.*;

/**
 */
public class Util
{
    private static Util m_instance;

    private Util()
    {
    }

    public static Util getInstance()
    {
        if (m_instance == null)
        {
            m_instance = new Util();
        }
        return m_instance;
    }
    // read data from cs542.db and return map of films of which key is its title and year.
    public Map<String, Film> readDataFile(String filePath) throws Exception
    {
        Map<String, Film> result = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                Film film = new Film(line.split(","));
                result.put(film.getKey(), film);
            }

            return result;
        }
    }

    public void writeDateFile(File file, Collection<Film> filmList)
    {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file, false)))
        {
            for (Film film: filmList)
            {
                out.write(film.toString());
                out.write("\n");
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to write to data file, " + e);
            System.exit(1);
        }
    }
   
    public Map<String, Set<String>> readIndexFile(File file) throws Exception
    {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            return (Map<String, Set<String>>) ois.readObject();
        }
    }

    public void writeIndexFile(File file, Map<String, Set<String>> indexes)
    {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false)))
        {
            oos.writeObject(indexes);
        }
        catch (Exception e)
        {
            System.out.println("Failed to write to index file, " + e);
            System.exit(1);
        }
    }
}
