package ch.heigvd.res.lab01.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
  private int noLine = 0;
  private boolean rDetected = false;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
     StringBuilder s = new StringBuilder(str.substring(off, off + len));
     
     // Insert a number if it is the first line.
     if (noLine == 0)
        s.insert(0, ++noLine + "\t");
     
     for (int i = 0; i < s.length(); ++i)
        if (s.charAt(i) == '\n')
           s.insert(i + 1, ++noLine + "\t");
        else if (s.charAt(i) == '\r')
        {
           if (i < s.length() && s.charAt(i + 1) == '\n')
              ++i;
           
           s.insert(i + 1, ++noLine + "\t");
        }
     
     out.write(s.toString());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
     write(new String(cbuf), off, len);
  }

  @Override
  public void write(int c) throws IOException {
    if (noLine == 0)
       out.write(++noLine + "\t");
    
    if (rDetected && c != '\n')
       out.write(++noLine + "\t");
    rDetected = false;
           
    out.write(c);

    if (c == '\r')
       rDetected = true;
    else if (c == '\n')
       out.write(++noLine + "\t");
  }

}
