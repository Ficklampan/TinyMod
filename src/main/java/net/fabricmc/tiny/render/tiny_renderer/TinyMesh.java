package net.fabricmc.tiny.render.tiny_renderer;

import net.fabricmc.tiny.render.api.MeshQuad;
import net.fabricmc.tiny.render.api.Vertex;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TinyMesh {

    private final Identifier identifier;
    private final FloatBuffer vertices;
    private final IntBuffer indices;
    private final List<TinyMaterial> materials;

    public TinyMesh(Identifier identifier, FloatBuffer vertices, IntBuffer indices, List<TinyMaterial> materials)
    {
        this.identifier = identifier;
        this.vertices = vertices;
        this.indices = indices;
        this.materials = materials;
    }

    public Identifier identifier()
    {
        return identifier;
    }

    public FloatBuffer vertices()
    {
        return vertices;
    }

    public IntBuffer indices()
    {
        return indices;
    }

    public List<TinyMaterial> materials()
    {
        return materials;
    }

    public static TinyMesh build(Identifier identifier, List<MeshQuad> quads)
    {
        IntBuffer indices = BufferUtils.createIntBuffer(quads.size() + ((quads.size() * 4) * 3));
        List<TinyMaterial> materials = new ArrayList<>();
        Map<Vertex, Integer> indexMap = new LinkedHashMap<>();
        int index = 0;
        for (MeshQuad quad : quads)
        {
            int materialIndex;
            if (!materials.contains(quad.material))
            {
                materialIndex = materials.size();
                materials.add(quad.material);
            }else
                materialIndex = materials.indexOf(quad.material);
            for (Vertex vertex : quad.vertices)
            {
                if (indexMap.containsKey(vertex))
                {
                    indices.put(indexMap.get(vertex));
                    indices.put(indexMap.get(vertex));
                    indices.put(indexMap.get(vertex));
                    //indices.put(indexMap.get(vertex));
                }else
                {
                    indexMap.put(vertex, index);
                    indices.put(index);
                    indices.put(index);
                    indices.put(index);
                    //indices.put(index);
                    index++;
                }
            }
            indices.put(materialIndex);
        }
        FloatBuffer vertices = BufferUtils.createFloatBuffer(indexMap.size() * Vertex.SIZE);
        indexMap.forEach((vertex, integer) -> vertex.put(vertices));
        return new TinyMesh(identifier, vertices.flip(), indices.flip(), materials);
    }
}
