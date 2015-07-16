/*
 *  Author: Xiaolong_Shen @ Nexd_Tech
 *  Reference: ALGOLIST
 *  Briefs:
 *  	Realization of Dijkstra Algorithm for shortest path search
 *  	@Vertex is like the link map
 * 		@Edge store the connection relationship
 * 		@Dijkstra is the main class for getting the path computation and the path creation
 * 
 * 	Efficiency Update:
 * 		(i) Speed with Estimated edges and vertexes:
 * 				100 Vertices with 10 connection average: 3 millisecond. [Done]
 * 		(ii) Loading Speed with many edges and vertexes:
 * 				Liang Yuan should give a sample json format road map.
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Vertex implements Comparable<Vertex>
{
    public final String name;
    public List<Edge> adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public double[] location;
    public Vertex previous;
    public Vertex(String argName) { name = argName; }
    public String toString() { return name; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}
//Storage of Connections of 
class Edge
{
    public final Vertex target;
    public final double weight;
    public Edge(Vertex argTarget, double argWeight){ 
    	target = argTarget; 
    	weight = argWeight; 
    }
}

public class Dijkstra
{
	/*
	 *  Private Variables should be loaded in by function _load_in(file_path)
	 */

	// Vertices loaded from config json file
	private List<Vertex> map_vertices = new ArrayList<Vertex>(); 
	
	// Flag for whether loaded the data.
	private boolean Load_Flag = false; 
	
	// Link the name and the number with eachother. actually not necessary. could be updated.
	private List<String> reference = new ArrayList<String>();
	
	private Map<Long,String> poi_vertices = new HashMap<Long,String>();
	
    /*
     * Calling Method.
     * Get index of source vertex and target_vertex:
     * 
     * 	load_config(file_path) to load in roadmap information.
     *  String output_json = get_path(source_index, target_index).
     *  String output_json = get_path_by_name(source_name, target_name).
     *  
     *  Output Json Format:
     *  {
     *  	"index":{
     *  		"Name": "Vertex***",
     *  		"Location": ["x_cord(double)","y_cord(double)"],
     *  		"Distance": "double"   // The distance between this point and the previous point, so the first distance is 0.
     *  		},
     *  	...
     *  }
     *  
     *  Changed to:
     *  
     *  {
     *  	"array":[
     *  		{
     *  			"Name": "Vertex***",
     *  			"Location": ["x_cord(double)","y_cord(double)"],
     *  			"Distance": "double"   // The distance between this point and the previous point, so the first distance is 0.
     *  		},
     *  		{
     *  			"Name": "Vertex***",
     *  			"Location": ["x_cord(double)","y_cord(double)"],
     *  			"Distance": "double"   // The distance between this point and the previous point, so the first distance is 0.
     *  		},
     *  	]
     *  }
     *  		
     *  	
     * 
     * 	get the path by order then.
     */
	
    public String get_path_poi(long source_ind, long target_ind){
    	int source = this.reference.indexOf(poi_vertices.get(source_ind));
    	int target = this.reference.indexOf(poi_vertices.get(target_ind));
    	System.out.println("src:" + source + "; "+ target);
    	
    	if(source == -1 || target == -1){
    		return "";
    	}
    	else{
    	    List<Vertex> res_path = _get_path(source, target);
//            System.out.println("PATH: "+res_path);
            String res_json = Dijkstra_Json._to_json(res_path);
            for(Vertex v: map_vertices){
                v.previous = null;
                v.minDistance = Double.POSITIVE_INFINITY;
            }
    	    return res_json;
    	}
	}
    
    // map_path 为路网信息
    // poi_path 为poi映射信息
    public void load_config(String map_path, String poi_path) throws IOException{
    	Dijkstra_Json d_json = new Dijkstra_Json();
    	d_json._load_config(map_path, poi_path);
    	map_vertices = d_json.map_vertices;
    	reference = d_json.reference;
    	poi_vertices = d_json.poi_vertices;
        Load_Flag = true;
    }
    
	/*
	 * computePaths and getShortestPathTo is the core function to calculate results
	 */
    private void computePaths(Vertex source)
    {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
      	vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
		    Vertex u = vertexQueue.poll();
	
            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
				    vertexQueue.remove(v);
				    v.minDistance = distanceThroughU ;
				    v.previous = u;
			    	vertexQueue.add(v);
				}
            }
        }
    }

    private List<Vertex> getShortestPathTo(Vertex target)
    {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
    
    // private funciton for get path. Intergrated compute and get path.
    
    private List<Vertex> _get_path(int source_ind, int target_ind){
    	if(Load_Flag){
    		computePaths(map_vertices.get(source_ind));
    		return getShortestPathTo(map_vertices.get(target_ind));
    	}
    	else{
    		return null;
    	}
    }
    
}

