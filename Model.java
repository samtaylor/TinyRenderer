import java.io.*;
import java.util.*;

public class Model
{
  private ArrayList<Vertex> vertices;
  private ArrayList<Face> faces;

  public Model( String filename ) throws FileNotFoundException, IOException
  {
    this.vertices = new ArrayList<>();
    this.faces = new ArrayList<>();

    this.readModel( filename );
  }

  public void render( int width, int height, TinyRenderer renderer )
  {
    for ( Face face : this.faces )
    {
      for ( int j = 0; j < 3; j ++ )
      {
        Vertex v0 = face.vertices[j];
        Vertex v1 = j + 1 > 2 ? face.vertices[ 0 ] : face.vertices[(j + 1)];

        int x0 = ( int )( ( v0.x + 1.0f ) * width / 2.0f );
        int y0 = ( int )( ( v0.y + 1.0f ) * height / 2.0f );

        int x1 = ( int )( ( v1.x + 1.0f ) * width / 2.0f );
        int y1 = ( int )( ( v1.y + 1.0f ) * height / 2.0f );

        renderer.line( x0, y0, x1, y1, 0xFFFFFFFF );
      }
    }
  }

  private void readModel( String filename ) throws FileNotFoundException, IOException
  {
    File file = new File( filename );
		FileReader fileReader = new FileReader( new File( filename ) );
		BufferedReader bufferedReader = new BufferedReader( fileReader );

		String line;
		while ( ( line = bufferedReader.readLine() ) != null )
    {
      if ( line.startsWith( "v " ) )
      {
        Vertex vertex = this.parseVertex( line );
        this.vertices.add( vertex );
      }
      else if ( line.startsWith( "f " ) )
      {
        Face face = this.parseFace( line );
        this.faces.add( face );
      }
		}
		fileReader.close();
  }

  private Vertex parseVertex( String line )
  {
    StringTokenizer st = new StringTokenizer( line, " " );
    st.nextToken(); // drop the first token that identifies the line as a Vertex

    return new Vertex( Float.parseFloat( st.nextToken() ),
                       Float.parseFloat( st.nextToken() ),
                       Float.parseFloat( st.nextToken() ) );
  }

  private Face parseFace( String line )
  {
    StringTokenizer st = new StringTokenizer( line, " " );
    st.nextToken(); // drop the first token that identifies the line as a face

    StringTokenizer st1 = new StringTokenizer( st.nextToken(), "/" );
    Vertex v1 = this.vertices.get( Integer.parseInt( st1.nextToken() ) - 1 );

    StringTokenizer st2 = new StringTokenizer( st.nextToken(), "/" );
    Vertex v2 = this.vertices.get( Integer.parseInt( st2.nextToken() ) - 1 );

    StringTokenizer st3 = new StringTokenizer( st.nextToken(), "/" );
    Vertex v3 = this.vertices.get( Integer.parseInt( st3.nextToken() ) - 1 );

    return new Face( v1, v2, v3 );
  }
}
