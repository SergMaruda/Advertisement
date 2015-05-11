package activities;

import java.io.IOException;
import java.io.OptionalDataException;
import java.io.Serializable;

public class Filter implements Serializable
  {
  public String m_titile = "";
  public String m_url = "";
  public boolean m_active = false;

  public String toString() {
  return m_titile;
  }

  private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
    out.writeObject(m_titile);
    out.writeObject(m_url);
    out.writeBoolean(m_active);
    }

  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
    m_titile = (String)in.readObject();
    m_url =(String)in.readObject();
    m_active = in.readBoolean();

    }



  }
