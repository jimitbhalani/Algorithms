using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Node{

    private GameObject nodeGraphic;
    private string name;
    private string NodeType;
    private List<Edge> outgoingEdges;
    private int row = 0;
    private int column = 0;

    public Node(string nodename,GameObject nodeGrpic,List<Edge> edges, int rowIndex,int colIndex,string type)
    {
        name = nodename;
        nodeGraphic = nodeGrpic;
        outgoingEdges = edges;
        row = rowIndex;
        column = colIndex;
        NodeType = type;
    }

    public void rendererOn()
    {
        nodeGraphic.renderer.enabled = true;
    }

    public void rendererOff()
    {
        nodeGraphic.renderer.enabled = false;
    }

    public GameObject getNodeGraphic()
    {
        return nodeGraphic;
    }

    public string getNodeName()
    {
        return name;
    }

    public int getNodeRow()
    {
        return row;
    }

    public int getNodeCol()
    {
        return column;
    }

    public string getNodeType()
    {
        return NodeType;
    }

    public void AddEgdes(Node neighbor,int cost)
    {
        outgoingEdges.Add(new Edge(neighbor, cost));
    }

    public List<Edge> getEdges()
    {
        return outgoingEdges;
    }

    public List<Edge> getNodeEdges()
    {
        return outgoingEdges;
    }
}
