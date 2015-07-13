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

//class Vertex implements Comparable<Vertex>
//{
//    public final String name;
//    public List<Edge> adjacencies;
//    public double minDistance = Double.POSITIVE_INFINITY;
//    public double[] location;
//    public Vertex previous;
//    public Vertex(String argName) { name = argName; }
//    public String toString() { return name; }
//    public int compareTo(Vertex other)
//    {
//        return Double.compare(minDistance, other.minDistance);
//    }
//}
////Storage of Connections of 
//class Edge
//{
//    public final Vertex target;
//    public final double weight;
//    public Edge(Vertex argTarget, double argWeight){ 
//    	target = argTarget; 
//    	weight = argWeight; 
//    }
//}

public class Dijkstra_dumped
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
     * 
     * 	get the path by order then.
     */
    public String get_path_poi(long source_ind, long target_ind){
    	int source = this.reference.indexOf(poi_vertices.get(source_ind));
    	int target = this.reference.indexOf(poi_vertices.get(target_ind));
    	List<Vertex> res_path = _get_path(source, target);
    	return Dijkstra_Json._to_json(res_path); 
    }
    

    public void load_config(String map_path, String poi_path) throws IOException{
    	Dijkstra_Json d_json = new Dijkstra_Json();
    	d_json._load_config(map_path, poi_path);
    	map_vertices = d_json.map_vertices;
    	reference = d_json.reference;
    	poi_vertices = d_json.poi_vertices;
        Load_Flag = true;
    }
    
//  public String get_path(int source_ind, int target_ind){
//	System.out.println("Source: "+map_vertices.get(source_ind).name);
//	System.out.println("Target: "+map_vertices.get(target_ind).name);
//	List<Vertex> res_path = _get_path(source_ind, target_ind);
//	System.out.println("PATH: "+ res_path);
//	return Dijkstra_Json._to_json(res_path);
//}
//
//public String get_path_by_name(String source_name, String target_name){
//	int source_ind = this.reference.indexOf(source_name);
//	int target_ind = this.reference.indexOf(target_name);
//	List<Vertex> res_path = _get_path(source_ind, target_ind);
//	System.out.println("PATH: "+ res_path);
//	return Dijkstra_Json._to_json(res_path);  	
//}
    
    /*
     * Unit Test Method
     */
//    public void call_dijkstra_unit_test(String file_path) throws IOException
//    {
//    	int NodeSize = 1000;
//    	int AveConn = 8;
//    	
//    	List<Vertex> vertices = Dijkstra_Unit_Test.buildGraph(NodeSize, Dijkstra_Unit_Test.createGraph(NodeSize,AveConn)); 
//    	
//    	int vertex_ind = 1;
//    	long startTime = System.currentTimeMillis();
//        computePaths(vertices.get(vertex_ind));
//        long midTime = System.currentTimeMillis();
//        for (Vertex v : vertices)
//		{
//		    System.out.println("Distance to " + v + ": " + v.minDistance);
//		    List<Vertex> path = getShortestPathTo(v);
//		    System.out.println("Path: " + path);
//		}
//        long endTime = System.currentTimeMillis();
//        
//        System.out.println("PathComputing: " + (midTime-startTime) + ":" + startTime + " ; " + midTime);
//        System.out.println("PathGet: " + (endTime - midTime)); 
//    }
//    
//    public void get_path_unit_test(String file_path){
//    	int NodeSize = 1000;
//    	int AveConn = 8;
//    	// Simulation of configuration loading
//    	List<Vertex> vertices = Dijkstra_Unit_Test.buildGraph(NodeSize, Dijkstra_Unit_Test.createGraph(NodeSize,AveConn)); 
//    	this.map_vertices = vertices;
//    	for(Vertex v : map_vertices){
//    		this.reference.add(v.name);
//    	}
//    	this.Load_Flag = true;
//    	
//    	int source_ind = 1;
//    	int target_ind = 198; 
//    			//this.reference.indexOf(map_vertices.get(source_ind).adjacencies.get(0).target.name);
//    	long startTime = System.currentTimeMillis();
//    	String output_json = this.get_path(source_ind, target_ind);
//        long endTime = System.currentTimeMillis();
//        Dijkstra_Unit_Test.validate_path(this._get_path(source_ind, target_ind));
//        System.out.println("PathComputing: " + (endTime-startTime));
//        System.out.println("Output :\n "+ output_json);
//    }
}

