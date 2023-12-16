
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.util.*;

import static org.lwjgl.opengl.GL20.*;

public class UniformsMap {

    private int programId;
    //Map to associate the name of the uniform with the uniform location integer,
    //derived from programId and OpenGL's glGetUniformLocation method
    private Map<String, Integer> uniforms;

    //Recieves identifier of the shader program
    public UniformsMap(int programId) {
        //Sets the private variable in this object
        this.programId = programId;
        //Creates a map that stores references as integers.
        //Each integer will refer to a uniform later created with the createUniform method.
        uniforms = new HashMap<>();
    }


    //This method creates and adds the uniforms to the above created map
    public void createUniform(String uniformName) {
        //uniform location, the integer, will be what refers to the uniform that we put in.
        //Takes in the name from the createUniform method, gets the location(important) with the OpenGL method,
        //then keeps it as the integer called uniform location, and later adds it to a map along with the uniformName.
        //The integer will be the "uniform object" except it's just a reference to where it is in a buffer (guess kinda)
        int uniformLocation = glGetUniformLocation(programId, uniformName);

        //Confirming the OpenGL method gave us a value.
        if (uniformLocation < 0) {
            throw new RuntimeException("Could not find uniform [" + uniformName + "] in shader program [" +
                    programId + "]");
        }
        //Adds created uniform to the map with its name as the key and the location being the grabbed value
        uniforms.put(uniformName, uniformLocation);
    }

    //Load a 4x4 Matrix. Takes
    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            //Grabs the location from the map
            Integer location = uniforms.get(uniformName);
            //Confirming we grabbed a value
            if (location == null) {
                throw new RuntimeException("Could not find uniform [" + uniformName + "]");
            }
            //Goes to the location provided to get the uniform, and applies
            glUniformMatrix4fv(location.intValue(), false, value.get(stack.mallocFloat(16)));
        }
    }
}