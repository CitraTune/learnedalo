


import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class SceneRender {

    private ShaderProgram shaderProgram;

    public SceneRender() {
        //Create a shaderModuleDataList
        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        //Create two ShaderModuleData instances, one for vertices, one for fragments(colors)
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("src/resources/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("src/resources/scene.frag", GL_FRAGMENT_SHADER));
        //Creates the ShaderProgram object with the filled dataList. It will process
        shaderProgram = new ShaderProgram(shaderModuleDataList);
    }
    //just uses the cleanup from ShaderProgram.
    public void cleanup() {
        shaderProgram.cleanup();
    }

    //Does all the drawing
    public void render(Scene scene) {
        //glUseProgram(programId)
        shaderProgram.bind();

        //Iterates over the meshes stored in the Scene instances
        scene.getMeshMap().values().forEach(mesh -> {
            //Binds them
            glBindVertexArray(mesh.getVaoId());

            //Draws
            //mode: Specifies the type of primitive for rendering. Triangles in this scenario, as we are rendering a mesh
            //count: chooses amounts of elements, vertices in this scenario, to be rendered
            //type: specifies the type of value for the indices
            //indices: just the offset, which will be 0
            glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
            }
        );


        //Unbinding
        glBindVertexArray(0);
        shaderProgram.unbind();

    }
}