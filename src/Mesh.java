import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int numVertices;
    private int vaoId;
    private List<Integer> vboIdList;

    //Big Idea- send vertices and the ordering of how they create triangles to the GPU memory
    public Mesh(float[] positions, float[] textCoords, int[] indices) {
        //The MemoryStack is used to give this code to the OpenGl library by putting it offHeap whatever that means. Garbage collector will clean it up.
        try (MemoryStack stack = MemoryStack.stackPush()) {
            numVertices = indices.length;
            vboIdList = new ArrayList<>();

            //creates the VAO
            vaoId = glGenVertexArrays();
            //bind it to the next actions
            glBindVertexArray(vaoId);

            //Create VBO, list of coordinates
            int vboId = glGenBuffers();
            vboIdList.add(vboId);

            //The Vbo is a buffer(storage) that holds vertices(or other things) in the 3d plane. it holds it on the GPU RAM. this one holds the mesh vertices.

            //Creates a FloatBuffer. Gives it the length of the positions array. This will hold coordinates.
            FloatBuffer positionsBuffer = stack.callocFloat(positions.length);
            //Adds the positions array to this newly made buffer
            positionsBuffer.put(0, positions);
            //This buffer is mainly for working with C, the OpenGL language. It uses that datatype.

            //Binds this buffer to the current VBO.
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            //Gives it data- the FloatBuffer we just filled gives it there.
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
            //Enables it
            glEnableVertexAttribArray(0);


            //Most important part- Explains the entire VAO buffer.
            //Index- talks with the shader. Notice the vertices shader(used for these vertices) has it stated that inPosition is at location 0. That is communicated here with index: 0
            //Size- We are passing 3 variables, which is effectively 3 dimensional. Works closely with the data type and the strides.
            //Data type- Specifies that these variables are all floats
            //Normalized- No idea what it means, but they definitely don't need to be normalized.
            //Stride- At 0, it auto calculates. Can be passed off as irrelevant.
            //Stride- However, you can calculate that with floats (4 bytes each) times size 3, meaning that it would be 12 bytes per stride, to get to the next value
            //Pointer- This is an offset for the first component. We don't have anything stored before it, so no offset.
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
//
//            // Normals VBO. Same stuff as above, just instead of coordinate points its the normal values that areas have.
//            vboId = glGenBuffers();
//            vboIdList.add(vboId);
//            FloatBuffer normalsBuffer = stack.callocFloat(normals.length);
//            normalsBuffer.put(0, normals);
//            glBindBuffer(GL_ARRAY_BUFFER, vboId);
//            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
//            glEnableVertexAttribArray(1);
//            //location of 1 now. matches with scene.vert
//            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);


            //Same thing, this time its Colors/texture coordinates
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer textCoordsBuffer = stack.callocFloat(textCoords.length);
            textCoordsBuffer.put(0, textCoords);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            //location of 2 now. matches with scene.vert
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            //Same thing, this time indices
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            IntBuffer indicesBuffer = stack.callocInt(indices.length);
            indicesBuffer.put(0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);



            //Explanations
            //VAOs hold vbos, as they are arrays of vbos. those can be things like color, textures, or in this case, position coordinates.
            //Vbos are often called attribute lists because they hold, well, attributes. It's an array of lists that represents a 3d model.


            //Unbinds vbo, by binding to 0
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            //Unbind vao
            glBindVertexArray(0);
        }
    }

    public void cleanup() {
        //Deletes what we were working with, clearing up space for the next object,
        vboIdList.forEach(GL30::glDeleteBuffers);
        //Clears the specific vaoid. this vaoid can be used later and referenced
        glDeleteVertexArrays(vaoId);
    }

    public int getNumVertices() {
        //gets vertices of the mesh
        return numVertices;
    }

    public final int getVaoId() {
        //get the reference for the vao
        return vaoId;
    }
}