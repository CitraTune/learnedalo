#version 330

//Correlates to Mesh.java
layout (location=0) in vec3 position;
layout (location=1) in vec3 color;

//Outputs to fragment shader
out vec3 outColor;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    gl_Position = projectionMatrix * modelMatrix * vec4(position, 1.0);
    outColor = color;
}