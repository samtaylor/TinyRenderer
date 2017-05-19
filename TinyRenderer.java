import java.io.*;
import java.util.*;

public class TinyRenderer
{
  private static final boolean DEBUG = false;

  public static void main( String [] argv ) throws FileNotFoundException, IOException
  {
    new TinyRenderer( 800, 800, "./head.obj" ).render();
  }

  private TGAImage tgaImage;
  private Model model;

  private TinyRenderer( int width, int height, String model ) throws FileNotFoundException, IOException
  {
    this.tgaImage = new TGAImage( width, height, 0xFF000000 );
    this.model = new Model( model );
  }

  private void render() throws FileNotFoundException, IOException
  {
    this.model.render( this.tgaImage.getWidth(), this.tgaImage.getHeight(), this );

    this.tgaImage.write( "./image.tga" );
  }

  public void line( int x1, int y1, int x2, int y2, int colour )
  {
    this.debugPrint( "from {" + x1 + "," + y1 + "}" );
    this.debugPrint( "to   {" + x2 + "," + y2 + "}" );

    float deltaX = x2 - x1;
    float deltaY = y2 - y1;

    float stepX = deltaX < 0.0f ? -1.0f : 1.0f;
    float stepY = deltaY < 0.0f ? -1.0f : 1.0f;

    this.debugPrint( "delta {" + deltaX + ", " + deltaY + "}" );
    if ( Math.abs( deltaX ) > Math.abs( deltaY ) )
    {
      stepY = deltaY / deltaX;
      if ( deltaY > 0.0f ) stepY = Math.abs( stepY );
      else stepY = -Math.abs( stepY );
    }
    else
    {
      stepX = deltaX / deltaY;
      if ( deltaX > 0.0f ) stepX = Math.abs( stepX );
      else stepX = -Math.abs( stepX );
    }
    this.debugPrint( "step {" + stepX + ", " + stepY + "}" );

    float x = x1;
    float y = y1;

    while ( !this.finished( x, y, x1, y1, x2, y2 ) )
    {
      this.tgaImage.set( ( int )x, ( int )y, 0xFFFFFF );

      this.debugPrint( "xy {" + x + "," + y + "}" );

      x += stepX;
      y += stepY;
    }

    this.tgaImage.set( x1, y1, 0xFFFF0000 );
    this.tgaImage.set( x2, y2, 0xFF0000FF );
  }

  private void debugPrint( String line )
  {
    if ( DEBUG )
    {
      System.out.println( line );
    }
  }

  private boolean finished( float currentX, float currentY, float startX, float startY, float endX, float endY )
  {
    boolean finishedX, finishedY;
    if ( startX > endX )
    {
      finishedX = currentX <= endX;
    }
    else
    {
      finishedX = currentX >= endX;
    }

    if ( startY > endY )
    {
      finishedY = currentY <= endY;
    }
    else
    {
      finishedY = currentY >= endY;
    }

    return finishedX && finishedY;
  }
}
