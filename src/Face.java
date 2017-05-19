public class Face
{
  public Vertex [] vertices;

  Face( Vertex v1, Vertex v2, Vertex v3 )
  {
    vertices = new Vertex[] { v1, v2, v3 };
  }

  public int getWidth( int canvasWidth )
  {
    Vertex vertex = this.vertices[ 0 ];
    int x = ( int )( ( vertex.x + 1.0f ) * canvasWidth / 2.0f );

    int minX = x;
    int maxX = x;

    for ( int i = 1; i < 3; i ++ )
    {
      vertex = this.vertices[ i ];
      x = ( int )( ( vertex.x + 1.0f ) * canvasWidth / 2.0f );

      minX = ( int )( minX > x ? x : minX );
      maxX = ( int )( maxX < x ? x : maxX );
    }

    return maxX - minX;
  }

  public int getHeight( int canvasHeight )
  {
    Vertex vertex = this.vertices[ 0 ];
    int y = ( int )( ( vertex.y + 1.0f ) * canvasHeight / 2.0f );

    int minY = y;
    int maxY = y;

    for ( int i = 1; i < 3; i ++ )
    {
      vertex = this.vertices[ i ];
      y = ( int )( ( vertex.y + 1.0f ) * canvasHeight / 2.0f );

      minY = ( int )( minY > y ? y : minY );
      maxY = ( int )( maxY < y ? y : maxY );
    }

    return maxY - minY;
  }

  public int getMinX( int canvasWidth )
  {
    Vertex vertex = this.vertices[ 0 ];
    int x = ( int )( ( vertex.x + 1.0f ) * canvasWidth / 2.0f );

    int minX = x;

    for ( int i = 1; i < 3; i ++ )
    {
      vertex = this.vertices[ i ];
      x = ( int )( ( vertex.x + 1.0f ) * canvasWidth / 2.0f );

      minX = ( int )( minX > x ? x : minX );
    }

    return minX;
  }

  public int getMinY( int canvasHeight )
  {
    Vertex vertex = this.vertices[ 0 ];
    int y = ( int )( ( vertex.y + 1.0f ) * canvasHeight / 2.0f );

    int minY = y;

    for ( int i = 1; i < 3; i ++ )
    {
      vertex = this.vertices[ i ];
      y = ( int )( ( vertex.y + 1.0f ) * canvasHeight / 2.0f );

      minY = ( int )( minY > y ? y : minY );
    }

    return minY;
  }
}
