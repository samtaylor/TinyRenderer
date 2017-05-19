import java.io.*;

public class Main
{
  public static void main( String [] argv ) throws FileNotFoundException, IOException
  {
    if ( argv.length != 4 )
    {
      showHelp();
    }
    if ( argv.length > 0 && argv[ 0 ].equals( "-help" ) )
    {
      showHelp();
    }
    else if ( argv.length == 4 )
    {
      int width = Integer.parseInt( argv[0] );
      int height = Integer.parseInt( argv[1] );
      String model = argv[2];
      String output = argv[3];

      new TinyRenderer( width, height, model ).render( output );
    }
  }

  private static void showHelp()
  {
    System.out.println( "Usage: TinyRenderer [width] [height] [model file] [output file]" );
  }
}
