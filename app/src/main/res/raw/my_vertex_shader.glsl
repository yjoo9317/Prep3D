uniform mat4 u_MVPMatrix;
attribute vec4 vPosition;

void main()
{
    gl_Position = u_MVPMatrix * vPosition;
}