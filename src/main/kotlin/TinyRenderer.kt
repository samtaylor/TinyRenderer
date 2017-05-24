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

    drawLine( 10, 10, 50, 60, 0xFFFF0000.toInt(), tgaImage )

    tgaImage.write( output )
  }

  public fun drawLine( x1: Int, y1: Int, x2: Int, y2: Int, colour: Int, image: TGAImage )
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
}
