import java.io.*;
import java.util.*;

public class TinyRenderer
{
  private static final boolean DEBUG = false;

  private Model model;

  private int width, height;

  public TinyRenderer( int width, int height, String model ) throws FileNotFoundException, IOException
  {
    this.model = new Model( model );

    this.width = width;
    this.height = height;
  }

  public void render( String output ) throws FileNotFoundException, IOException
  {
    TGAImage tgaImage = new TGAImage( this.width, this.height, 0xFF000000 );
    this.fillModel( this.model, 0xFFFFFFFF, tgaImage );

    tgaImage.write( output );
  }

  private void drawModel( Model model, int colour, TGAImage tgaImage )
  {
    for ( Face face : model.faces )
    {
      this.drawFace( face, colour, tgaImage );
    }
  }

  private void fillModel( Model model, int colour, TGAImage tgaImage )
  {
    for ( Face face : model.faces )
    // Face face = new Face( new Vertex( 0.25f, 0.4f, 0.0f ),
    //                       new Vertex( 0.0f, 1.0f, 0.0f ),
    //                       new Vertex( 0.7f, 0.7f, 0.0f ) );
    {
      this.fillFace( face, colour, tgaImage );
    }
  }

  public void drawFace( Face face, int colour, TGAImage tgaImage )
  {
    float canvasWidth = tgaImage.getWidth();
    float canvasHeight = tgaImage.getHeight();

    for ( int j = 0; j < 3; j ++ )
    {
      Vertex v0 = face.vertices[j];
      Vertex v1 = j + 1 > 2 ? face.vertices[ 0 ] : face.vertices[(j + 1)];

      int x0 = ( int )( ( v0.x + 1.0f ) * canvasWidth / 2.0f );
      int y0 = ( int )( ( v0.y + 1.0f ) * canvasHeight / 2.0f );

      int x1 = ( int )( ( v1.x + 1.0f ) * canvasWidth / 2.0f );
      int y1 = ( int )( ( v1.y + 1.0f ) * canvasHeight / 2.0f );

      this.drawLine( x0, y0, x1, y1, colour, tgaImage );
    }
  }

  public void fillFace( Face face, int colour, TGAImage tgaImage )
  {
    float canvasWidth = tgaImage.getWidth();
    float canvasHeight = tgaImage.getHeight();

    int faceWidth = face.getWidth( ( int )canvasWidth );
    int faceHeight = face.getHeight( ( int )canvasHeight );

    TGAImage tempImage = new TGAImage( ( int )canvasWidth, ( int )canvasHeight, 0x0 );

    this.drawFace( face, colour, tempImage );

    int faceMinX = face.getMinX( ( int )canvasWidth );
    int faceMinY = face.getMinY( ( int )canvasHeight );

    boolean toggle = false;
    for ( int y = faceMinY; y < faceMinY + faceHeight; y ++ )
    {
      boolean insideTriangle = false;

      int lastX = faceMinX;
      int firstX = faceMinX;

      // look ahead to see if this row has a start and end
      for ( int x = faceMinX; x < faceMinX + faceWidth; x ++ )
      {
        if ( insideTriangle )
        {
          if ( tempImage.get( x, y ) != 0 )
          {
            lastX = x;
          }
        }
        else
        {
          if ( tempImage.get( x, y ) != 0 )
          {
            insideTriangle = true;
            lastX = x;
            firstX = x;
          }
        }
      }

      this.drawLine( firstX, y, lastX, y, colour, tgaImage );
    }
  }

  public void drawLine( int x1, int y1, int x2, int y2, int colour, TGAImage tgaImage )
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
      tgaImage.set( ( int )x, ( int )y, colour );

      this.debugPrint( "xy {" + x + "," + y + "}" );

      x += stepX;
      y += stepY;
    }

    if ( DEBUG )
    {
      tgaImage.set( x1, y1, 0xFFFF0000 );
      tgaImage.set( x2, y2, 0xFF0000FF );
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

  public int getIntFromColor(int Red, int Green, int Blue){
    Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
    Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
    Blue = Blue & 0x000000FF; //Mask out anything not blue.

    return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
}
}
