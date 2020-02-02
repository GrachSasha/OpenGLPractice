
attribute vec4 a_Position;
uniform mat4 uMatrix;

void main()
{
    gl_Position = uMatrix * a_Position;
    gl_PointSize = 5.0;
}