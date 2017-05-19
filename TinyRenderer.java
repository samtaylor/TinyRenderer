import java.io.*;

public class TinyRenderer
{
  private static final boolean DEBUG = false;

  public static void main( String [] argv ) throws FileNotFoundException, IOException
  {
    new TinyRenderer( 100, 100 ).render();
  }

  private TGAImage tgaImage;

  private TinyRenderer( int width, int height )
  {
    this.tgaImage = new TGAImage( width, height, 0xFF000000 );
  }

  private void render() throws FileNotFoundException, IOException
  {
    this.rect( 20, 20, 20, 10, 0xFFFF0000 );

    this.tgaImage.write( "./image.tga" );
  }

  private void rect( int x, int y, int width, int height, int colour )
  {
    this.line( x,         y,          x + width, y,          colour );
    this.line( x + width, y,          x + width, y + height, colour );
    this.line( x + width, y + height, x,         y + height, colour );
    this.line( x,         y + height, x,         y,          colour );
  }

  private void line( int x1, int y1, int x2, int y2, int colour )
  {
    float startX = x1 < x2 ? x1 : x2;
    float startY = y1 < y2 ? y1 : y2;

    float endX = x1 < x2 ? x2 : x1;
    float endY = y1 < y2 ? y2 : y1;

    float xDiff = Math.abs( startX - endX );
    float yDiff = Math.abs( startY - endY );

    float yStep = xDiff == 0.0f ? 1.0f : yDiff / xDiff;
    float xStep = yDiff == 0.0f ? 1.0f : xDiff / yDiff;

    if ( DEBUG )
    {
      System.out.println( "-- " + xDiff + ", " + yDiff + " --" );
      System.out.println( "-- " + xStep + ", " + yStep + " --" );
    }

    yStep = xStep < 1.0f ? 1.0f : yStep;
    xStep = yStep < 1.0f ? 1.0f : xStep;

    if ( DEBUG )
    {
      System.out.println( "-- " + xStep + ", " + yStep + " --" );
      System.out.println( "x: " + startX + " -> " + endX );
      System.out.println( "y: " + startY + " -> " + endY );
    }

    for ( float x = startX, y = startY; x <= endX && y <= endY; x += xStep, y += yStep )
    {
      if ( DEBUG )
      {
        System.out.println( x + ", " + y + " = " + ( x <= endX && y <= endY ) );
      }
      this.tgaImage.set( (int)x, (int)y, colour );
    }
  }
}
