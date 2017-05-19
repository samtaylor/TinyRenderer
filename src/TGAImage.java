import java.io.*;

public class TGAImage
{
  private int [] pixels;
  private int width;
  private int height;

  public TGAImage( int width, int height, int backgroundColour )
  {
    this.width = width;
    this.height = height;
    this.pixels = new int[ width * height ];

    this.initialisePixels( backgroundColour );
  }

  public int getWidth()
  {
    return this.width;
  }

  public int getHeight()
  {
    return this.height;
  }

  public void set( int x, int y, int colour )
  {
    if ( x < this.width && y < this.height )
    {
      y = this.height - 1 - y;
      this.pixels[ x + ( y * width )] = colour;
    }
  }

  public void write( String filename ) throws FileNotFoundException, IOException
  {
    byte [] output = TGAWriter.write( this.pixels, this.width, this.height, TGAReader.ARGB );

    FileOutputStream fos = new FileOutputStream( filename );
    fos.write( output );
    fos.close();
  }

  private void initialisePixels( int backgroundColour )
  {
    for ( int x = 0; x < this.width; x ++ )
    {
      for ( int y = 0; y < this.height; y ++ )
      {
        this.set( x, y, backgroundColour );
      }
    }
  }
}
