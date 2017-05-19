import java.io.*;
import java.util.*;

public class TinyRenderer
{
  private static final boolean DEBUG = false;

  private TGAImage tgaImage;
  private Model model;

  public TinyRenderer( int width, int height, String model ) throws FileNotFoundException, IOException
  {
    this.tgaImage = new TGAImage( width, height, 0xFF000000 );

    this.model = new Model( model );
  }

  public void render( String output ) throws FileNotFoundException, IOException
  {
    this.drawModel( this.model );

    this.tgaImage.write( output );
  }

  private void drawModel( Model model )
  {
    float width = this.tgaImage.getWidth();
    float height = this.tgaImage.getHeight();

    for ( Face face : model.faces )
    {
      for ( int j = 0; j < 3; j ++ )
      {
        Vertex v0 = face.vertices[j];
        Vertex v1 = j + 1 > 2 ? face.vertices[ 0 ] : face.vertices[(j + 1)];

        int x0 = ( int )( ( v0.x + 1.0f ) * width / 2.0f );
        int y0 = ( int )( ( v0.y + 1.0f ) * height / 2.0f );

        int x1 = ( int )( ( v1.x + 1.0f ) * width / 2.0f );
        int y1 = ( int )( ( v1.y + 1.0f ) * height / 2.0f );

        this.drawLine( x0, y0, x1, y1, 0xFFFFFFFF );
      }
    }
  }

  public void drawLine( int x1, int y1, int x2, int y2, int colour )
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

    while ( !this.lineFinished( x, y, x1, y1, x2, y2 ) )
    {
      this.tgaImage.set( ( int )x, ( int )y, 0xFFFFFF );

      this.debugPrint( "xy {" + x + "," + y + "}" );

      x += stepX;
      y += stepY;
    }

    if ( DEBUG )
    {
      this.tgaImage.set( x1, y1, 0xFFFF0000 );
      this.tgaImage.set( x2, y2, 0xFF0000FF );
    }
  }

  private void debugPrint( String line )
  {
    if ( DEBUG )
    {
      System.out.println( line );
    }
  }

  private boolean lineFinished( float currentX, float currentY, float startX, float startY, float endX, float endY )
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
