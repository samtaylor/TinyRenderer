public class Vertex
{
  public float x, y, z;

  Vertex( float x, float y, float z )
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public String toString()
  {
    return "{ " + this.x + ", " + this.y + ", " + this.x + " }";
  }
}
