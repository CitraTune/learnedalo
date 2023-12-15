
import org.lwjgl.opengl.GL30;


import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class ShaderProgram {

    private final int programId;
    //Creates a new program, creates shaders in shaderModules from the DataList, links those shaders to the program.
    public ShaderProgram(List<ShaderModuleData> shaderModuleDataList) {
        //create a new openGL shader program
        programId = glCreateProgram();
        //confirmation
        if (programId == 0) {
            throw new RuntimeException("Could not create Shader");
        }
        //create a list of what shaders we are using
        List<Integer> shaderModules = new ArrayList<>();
        //associate the imported shaderModuleDataList(raw) with the list shaderModules(not as raw)
        shaderModuleDataList.forEach(s ->
                //Create the actual shader
                shaderModules.add(createShader(Utils.readFile(s.shaderFile), s.shaderType)));
        //call the link method with the semi processed data
        link(shaderModules);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    protected int createShader(String shaderCode, int shaderType) {
        //Effectively all this does is makes a shader with the colors given and attaches it to the opengl program
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);
        //Reference to the shader
        return shaderId;
    }

    public int getProgramId() {
        return programId;
    }

    private void link(List<Integer> shaderModules) {
        //takes semi processed data in

        //links program we previously created in the constructor
        glLinkProgram(programId);
        //confirmation
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
        //go through every object in the list. Detaches the program from every shader in the list. not too sure whats happening here.
        shaderModules.forEach(s -> glDetachShader(programId, s));
        //deletes the shaders. i really don't understand why
        shaderModules.forEach(GL30::glDeleteShader);

        //ultimately I think this code just links up the processed data from the files with the program created in the constructor.
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void validate() {
        //Simply a debugging method
        glValidateProgram(programId);
        //confirmation
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException("Error validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }
    //returns a record which is used in the list of shaders. record holds the shader file and the filetype.
    public record ShaderModuleData(String shaderFile, int shaderType) {

    }
}