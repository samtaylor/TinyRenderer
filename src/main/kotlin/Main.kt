fun main( args: Array<String> )
{
  if ( args.size != 4 )
  {
    showHelp()
  }
  else if ( args.size > 0 && args[ 0 ].equals( "-help" ) )
  {
    showHelp()
  }
  else if ( args.size == 4 )
  {
    val width = Integer.parseInt( args[ 0 ] )
    val height = Integer.parseInt( args[ 1 ] )
    val model = args[ 2 ]
    val output = args[ 3 ]

    TinyRenderer( width, height, model ).render( output )
  }
}

fun showHelp()
{
  println( "Usage: TinyRendererKt [width] [height] [model file] [output file]" )
}
