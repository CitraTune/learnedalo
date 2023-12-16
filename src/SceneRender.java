


import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class SceneRender {

    private ShaderProgram shaderProgram;
    private UniformsMap uniformsMap;

    public SceneRender() {
        //Create a shaderModuleDataList
        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        //Create two ShaderModuleData instances, one for vertices, one for fragments(colors)
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("src/resources/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("src/resources/scene.frag", GL_FRAGMENT_SHADER));
        //Creates the ShaderProgram object with the filled dataList. It will process
        shaderProgram = new ShaderProgram(shaderModuleDataList);
        createUniforms();
    }
    //just uses the cleanup from ShaderProgram.
    public void cleanup() {
        shaderProgram.cleanup();
    }

    //Uniforms method
    private void createUniforms(){
        uniformsMap = new UniformsMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("modelMatrix");

    }

    //Does all the drawing
    public void render(Scene scene) {
        //glUseProgram(programId)
        shaderProgram.bind();

        //Needs commenting!
        uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());

        //Iterate over the models, then iterates for each mesh then iterate for each entity
        Collection<Model> models = scene.getModelMap().values();
        for(Model model : models){
            model.getMeshList().stream().forEach(mesh ->{
                glBindVertexArray(mesh.getVaoId());
                List<Entity> entities = model.getEntitiesList();
                for(Entity entity : entities){
                    //For all entities that have meshes from models, associates the uniform named modelMatrix with the proper data
                    uniformsMap.setUniform("modelMatrix", entity.getModelMatrix());
                    //Draws
                    //mode: Specifies the type of primitive for rendering. Triangles in this scenario, as we are rendering a mesh
                    //count: chooses amounts of elements, vertices in this scenario, to be rendered
                    //type: specifies the type of value for the indices
                    //indices: just the offset, which will be 0
                    glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
                }
            });
        }

        //Unbinding
        glBindVertexArray(0);
        shaderProgram.unbind();
    }
}