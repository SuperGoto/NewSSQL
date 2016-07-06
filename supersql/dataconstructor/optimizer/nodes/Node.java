package supersql.dataconstructor.optimizer.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import supersql.dataconstructor.optimizer.attributes.TfePath;
import supersql.dataconstructor.optimizer.tables.OptimizerTable;
import supersql.dataconstructor.optimizer.tables.Table;

public class Node {
	
	private HashSet<OptimizerTable> tables;
	private Collection<TfePath> paths;
	private NodeTfeCoordinate coordinates;
	private boolean isExternalTable;
	
	public Node(){
		tables = new HashSet<OptimizerTable>();
		paths = new ArrayList<TfePath>();
		coordinates = new NodeTfeCoordinate();
		isExternalTable = true;
	}
	
	public Node addTable(OptimizerTable table){
		tables.add(table);
		if(!table.isExternalTable())
			isExternalTable = false;
//		for(Attribute att : table.getAttributes()){
//			if(att instanceof TfeAttribute){
//				for(TfePath tfePath : ((TfeAttribute) att).getTfePaths()){
//					coordinates.addPath(tfePath);
//				}
//			}		
//		}
		return this;
	}
	
	public Node addPath(TfePath path){
		paths.add(path);
		coordinates.addPath(path);
		return this;
	}
	
	public Node addPaths(Collection<TfePath> paths){
		paths.addAll(paths);
		return this;
	}
	
	public HashSet<OptimizerTable> getTables(){
		return tables;
	}
	
	public Collection<TfePath> getPaths(){
		return paths;
	}
	
	public NodeTfeCoordinate getCoordinates(){
		return coordinates;
	}
	
	public int size(){
		return tables.size();
	}
	
	public boolean containsOriginalTable(Table table){
		for(OptimizerTable ot : tables){
			if(ot.getOriginalTable().equals(table))
				return true;
		}
		
		return false;
	}
	
	public boolean containsPath(TfePath path){
		return coordinates.containsPath(path);
	}
	
	public boolean isExternalNode(){	
		return isExternalTable;
	}
	
	public static Node fusionNodes(Collection<Node> nodeList){
		Node result = new Node();
		for(Node node : nodeList){
			result = fusionNodes(node, result);
		}
		
		return result;
	}
	
	public static Node fusionNodes(Node node1, Node node2){
		Node result = new Node();
		for(OptimizerTable table: node1.tables){
			result.addTable(table);
		}
		
		return result;
	}
	
	public String toString(){
		return tables.toString();
	}
}
