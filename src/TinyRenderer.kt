public class TinyRenderer constructor( width: Int, height: Int, model: String )
{
  private val model: Model
  private val width: Int
  private val height: Int

  init
  {
    this.model = Model( model )

    this.width = width
    this.height = height
  }

  fun render( output: String )
  {
    val tgaImage = TGAImage( this.width, this.height, 0xFF000000.toInt() )

    tgaImage.write( output )
  }
}
