package DB2;
public class Film
{
    public enum Attributes
    {
        Title,
        Year,
        Format,
        Genre,
        Director,
        Writer,
        Country,
        Studio,
        Price,
        CatalogNo;
    }

    private String[] m_attributes;

    Film(String[] attributes)
    {
        m_attributes = attributes;
    }

    public String[] getAttributes()
    {
        return m_attributes;
    }

    @Override
    public String toString()
    {
        return String.join(",", m_attributes);
    }
   // create key which contains its title and year for every film, assuming combination of title and year is unique.
    public String getKey()
    {
        return m_attributes[Attributes.Title.ordinal()] + "|" + m_attributes[Attributes.Year.ordinal()];
    }
}
