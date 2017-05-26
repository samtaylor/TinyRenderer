import java.util.Random

public class TinyRenderer constructor( width: Int, height: Int, model: String )
{
  private val model: Model
  private val width: Int
  private val height: Int

  private val DEBUG = false

  init
  {
    this.model = Model( model )

    this.width = width
    this.height = height
  }

  public fun render( output: String )
  {
    val tgaImage = TGAImage( this.width, this.height, 0xFF000000.toInt() )

    /*debugTriangles( tgaImage )*/

    fillModel( model, 0xFFFF0000.toInt(), tgaImage )
    /*drawModel( model, 0xFFFFFFFF.toInt(), tgaImage )*/

    tgaImage.write( output )
  }

  private fun drawModel( model: Model, colour: Int, image: TGAImage )
  {
    for ( face in model.faces )
    {
      drawFace( face, colour, image )
    }
  }

  private fun fillModel( model: Model, colour: Int, image: TGAImage )
  {
    for ( face in model.faces )
    {
      fillFace( face, colour, image )
    }
  }

  private fun drawFace( face: Face, colour: Int, image: TGAImage ): Vector3?
  {
    val canvasWidth = image.getWidth()
    val canvasHeight = image.getHeight()

    var worldCoords = arrayOfNulls<Vector3>( 3 )

    for ( i in 0 .. 2 )
    {
      val v0 = face.vertices[ i ]
      val v1 = if ( i + 1 > 2 ) face.vertices[ 0 ] else face.vertices[ i + 1 ]

      val x0 = ( ( v0.x + 1.0F ) * canvasWidth / 2.0F )
      val y0 = ( ( v0.y + 1.0F ) * canvasHeight / 2.0F )

      val x1 = ( ( v1.x + 1.0F ) * canvasWidth / 2.0F )
      val y1 = ( ( v1.y + 1.0F ) * canvasHeight / 2.0F )

      worldCoords[ i ] = Vector3( v0.x, v0.y, v0.z )

      drawLine( x0.toInt(), y0.toInt(), x1.toInt(), y1.toInt(), colour, image )
    }

    val normal = ( worldCoords[ 2 ]!!.sub( worldCoords[ 0 ] ) ).crs( worldCoords[ 1 ]!!.sub( worldCoords[ 0 ] ) )

    return normal.nor()
  }

  private fun fillFace( face: Face, colour: Int, image: TGAImage )
  {
    val canvasWidth = image.getWidth()
    val canvasHeight = image.getHeight()

    val faceWidth = face.getWidth( canvasWidth.toInt() ) + 1
    val faceHeight = face.getHeight( canvasHeight.toInt() )

    val tempImage = TGAImage( canvasWidth.toInt(), canvasHeight.toInt(), 0 )

    val normal = drawFace( face, colour, tempImage )

    val lightDir = Vector3( 0.0F, 0.0F, -1.0F )
    val intensity = normal?.scl( lightDir )
    val colourCompontent = intensity!!.z * 255.0F

    if ( colourCompontent > 0.0F )
    {
      val shadeColour = getIntFromColor( colourCompontent.toInt(),
                                         colourCompontent.toInt(),
                                         colourCompontent.toInt() )

      val faceMinX = face.getMinX( canvasWidth.toInt() )
      val faceMinY = face.getMinY( canvasHeight.toInt() )

      for ( y in faceMinY .. faceMinY + faceHeight - 1 )
      {
        var insideTriangle = false

        var lastX = faceMinX
        var firstX = faceMinX

        for ( x in faceMinX .. faceMinX + faceWidth - 1 )
        {
          if ( insideTriangle )
          {
            if ( tempImage.get( x, y ) != 0 )
            {
              lastX = x
            }
          }
          else
          {
            if ( tempImage.get( x, y ) != 0 )
            {
              insideTriangle = true
              lastX = x
              firstX = x
            }
          }
        }

        drawLine( firstX, y, lastX + 1, y, shadeColour, image )
      }
    }
  }

  private fun drawLine( x1: Int, y1: Int, x2: Int, y2: Int, colour: Int, image: TGAImage )
  {
    debugPrint( "from { $x1, $y1 } to { $x2, $y2 }" )

    val deltaX = ( x2 - x1 ).toFloat()
    val deltaY = ( y2 - y1 ).toFloat()

    debugPrint( "deltaX = $deltaX, deltaY = $deltaY" )

    var stepX:Float = if ( deltaX < 0 ) -1.0F else 1.0F
    var stepY:Float = if ( deltaY < 0 ) -1.0F else 1.0F

    if ( Math.abs( deltaX ) > Math.abs( deltaY ) )
    {
      stepY = deltaY / deltaX
      if ( deltaY > 0.0f ) stepY = Math.abs( stepY )
      else stepY = -Math.abs( stepY )
    }
    else
    {
      stepX = deltaX / deltaY
      if ( deltaX > 0.0f ) stepX = Math.abs( stepX )
      else stepX = -Math.abs( stepX )
    }

    debugPrint( "stepX = $stepX, stepY = $stepY" )

    var x:Float = x1.toFloat()
    var y:Float = y1.toFloat()

    while ( !lineFinished( x, y, x1, y1, x2, y2 ) )
    {
      image.set( x.toInt(), y.toInt(), colour )

      debugPrint( "draw { ${ x.toInt() }, ${ y.toInt() } }" )

      x += stepX
      y += stepY
    }
  }

  private fun lineFinished( currentX: Float,
                            currentY: Float,
                            startX: Int,
                            startY: Int,
                            endX: Int,
                            endY: Int ): Boolean
  {
    var finishedX: Boolean
    var finishedY: Boolean

    if ( startX > endX )
    {
      finishedX = currentX <= endX
    }
    else
    {
      finishedX = currentX >= endX
    }

    if ( startY > endY )
    {
      finishedY = currentY <= endY
    }
    else
    {
      finishedY = currentY >= endY
    }

    return finishedX && finishedY
  }

  private fun debugPrint( string: String )
  {
    if ( DEBUG )
    {
      println( string )
    }
  }

  private fun debugTriangles( image: TGAImage )
  {
    val f1 = Face( Vertex( -0.75F, 0.75F, 0.0F ),
                   Vertex( 0.0F, -0.2F, 0.0F ),
                   Vertex( -0.4F, 0.1F, 0.0F ) )
    fillFace( f1, 0xFFFF0000.toInt(), image )
    drawFace( f1, 0xFFFFFFFF.toInt(), image )

    /*val f2 = Face( Vertex( 0.75F, 0.3F, 0.0F ),
                   Vertex( 0.4F, 0.2F, 0.0F ),
                   Vertex( 0.4F, 0.1F, 0.0F ) )
    fillFace( f2, 0xFF00FF00.toInt(), image )
    drawFace( f2, 0xFFFFFFFF.toInt(), image )*/

    /*val f3 = Face( Vertex( 0.0F, 0.0F, 0.0F ),
                   Vertex( 0.25F, -0.25F, 0.0F ),
                   Vertex( 0.0F, -0.5F, 0.0F ) )
    fillFace( f3, 0xFF0000FF.toInt(), image )
    drawFace( f3, 0xFFFFFFFF.toInt(), image )*/

    val f4 = Face( Vertex( 0.4F, 0.0F, 0.0F ),
                   Vertex( 0.3F, -0.25F, 0.1F ),
                   Vertex( 0.4F, -0.5F, 0.0F ) )
    fillFace( f4, 0xFFFFFF00.toInt(), image )
    drawFace( f4, 0xFFFFFFFF.toInt(), image )
  }

  public fun getIntFromColor( red: Int, green: Int, blue: Int ): Int
  {
    val redHex = ( red.toInt() shl 16 ) and 0x00FF0000.toInt();
    val greenHex = ( green shl 8 ) and 0x0000FF00;
    val blueHex = blue and 0x000000FF;

    return 0xFF000000.toInt() or redHex.toInt() or greenHex.toInt() or blueHex.toInt();
}
}
